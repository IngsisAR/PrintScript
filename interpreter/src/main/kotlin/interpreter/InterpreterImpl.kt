package interpreter

import utils.ASTNode
import utils.AssignmentExpression
import utils.BinaryExpression
import utils.CallExpression
import utils.ConditionalStatement
import utils.EnvironmentProvider
import utils.ExpressionStatement
import utils.Identifier
import utils.InputProvider
import utils.OutputProvider
import utils.VariableDeclaration
import utils.VersionChecker

class InterpreterImpl(
    val variableMap: Map<String, VariableInfo> = emptyMap(),
    val version: String,
    val outputProvider: OutputProvider,
    val inputProvider: InputProvider,
    private val environmentProvider: EnvironmentProvider,
) {
    fun interpret(node: ASTNode): InterpreterImpl {
        var internalVariableMap: Map<String, VariableInfo> = variableMap

        when {
            node is BinaryExpression ->
                BinaryExpressionInterpreter(
                    variableMap,
                    version,
                    outputProvider,
                    inputProvider,
                    environmentProvider,
                ).interpret(node)
            node is AssignmentExpression ->
                internalVariableMap =
                    AssignmentExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                        node,
                    )
            node is CallExpression ->
                CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                    node,
                )
            node is Identifier -> IdentifierInterpreter(variableMap, version).interpret(node)
            node is ExpressionStatement -> return interpret(node.expression)
            node is ConditionalStatement &&
                VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version) ->
                return ConditionalStatementInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                    node,
                )
            node is VariableDeclaration ->
                internalVariableMap =
                    VariableDeclarationInterpreter(
                        variableMap,
                        version,
                        outputProvider,
                        inputProvider,
                        environmentProvider,
                    ).interpret(node)
            else -> throw IllegalArgumentException("Invalid node type: ${node::class.simpleName} at (${node.line}:${node.start})")
        }
        return InterpreterImpl(internalVariableMap, version, outputProvider, inputProvider, environmentProvider)
    }
}

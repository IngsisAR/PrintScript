package interpreter

import utils.ASTNode
import utils.BinaryExpression
import utils.CallExpression
import utils.EnvironmentProvider
import utils.Identifier
import utils.InputProvider
import utils.Literal
import utils.OutputProvider
import utils.VariableDeclarator
import utils.VersionChecker

class VariableDeclaratorInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val kind: String,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
    private val environmentProvider: EnvironmentProvider,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is VariableDeclarator) { "Node must be a VariableDeclarator" }
        val id = node.id.name
        require(variableMap[id] == null) { "Variable '$id' already exists at (${node.id.line}:${node.id.start})" }

        val type = node.type.type
        val value =
            node.init?.let {
                when (it) {
                    is BinaryExpression ->
                        BinaryExpressionInterpreter(
                            variableMap,
                            version,
                            outputProvider,
                            inputProvider,
                            environmentProvider,
                        ).interpret(it)
                    is Identifier -> IdentifierInterpreter(variableMap, version).interpret(it)
                    is CallExpression ->
                        CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                            it,
                        )
                    is Literal -> it.value
                    else -> throw IllegalArgumentException("Unsupported init type: ${it::class.simpleName} at (${it.line}:${it.start})")
                }
            }

        checkTypeMatches(type, value, node.init ?: node.id)
        return mapOf(id to VariableInfo(type, value?.toString(), kind == "let"))
    }

    private fun checkTypeMatches(
        expectedType: String,
        newValue: Any?,
        assignedNode: ASTNode,
    ) {
        val gottenType =
            when {
                newValue is Number -> "number"
                newValue is String -> "string"
                newValue is Boolean && VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version) -> "boolean"
                newValue == null -> expectedType
                else -> throw IllegalArgumentException(
                    "Unsupported value type: ${newValue::class.simpleName} " +
                        "at (${assignedNode.line}:${assignedNode.start})",
                )
            }
        require(expectedType == gottenType) {
            "Type mismatch: expected $expectedType, got $gottenType at (${assignedNode.line}:${assignedNode.start})"
        }
    }
}

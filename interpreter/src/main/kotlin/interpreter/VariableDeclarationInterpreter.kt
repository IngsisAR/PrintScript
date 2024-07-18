package interpreter

import utils.ASTNode
import utils.EnvironmentProvider
import utils.InputProvider
import utils.OutputProvider
import utils.VariableDeclaration

class VariableDeclarationInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
    private val environmentProvider: EnvironmentProvider,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is VariableDeclaration) { "Node must be a VariableDeclaration" }

        return node.declarations.fold(variableMap) { acc, declaration ->
            val newVariableMap =
                VariableDeclaratorInterpreter(acc, node.kind, version, outputProvider, inputProvider, environmentProvider).interpret(
                    declaration,
                )
            acc + newVariableMap
        }
    }
}

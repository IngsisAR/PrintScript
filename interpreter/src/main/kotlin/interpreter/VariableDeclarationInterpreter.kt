package interpreter

import utils.ASTNode
import utils.VariableDeclaration

class VariableDeclarationInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is VariableDeclaration) { "Node must be a VariableDeclaration" }

        return node.declarations.fold(variableMap) { acc, declaration ->
            val newVariableMap = VariableDeclaratorInterpreter(acc, node.kind, version).interpret(declaration)
            acc + newVariableMap
        }
    }
}
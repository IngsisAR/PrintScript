package formatter

import utils.ASTNode
import utils.VariableDeclaration

class VariableDeclarationFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as VariableDeclaration
        return astNode.kind + " " + astNode.declarations.joinToString(", ") { VariableDeclaratorFormatter().format(it, configMap) } + ";\n"
    }
}

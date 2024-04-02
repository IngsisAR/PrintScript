package formatter

import ASTNode
import VariableDeclaration

class VariableDeclarationFormatter : FormatterInterface {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as VariableDeclaration
        return astNode.kind + " " + astNode.declarations.joinToString(", ") { VariableDeclaratorFormatter().format(it, configMap) } + ";\n"
    }
}

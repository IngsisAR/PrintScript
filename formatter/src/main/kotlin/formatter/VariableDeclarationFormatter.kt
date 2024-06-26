package formatter

import utils.ASTNode
import utils.VariableDeclaration

class VariableDeclarationFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is VariableDeclaration) {
            "VariableDeclarationFormatter can only format VariableDeclaration nodes."
        }
        return astNode.kind + " " +
            astNode.declarations.joinToString(", ") {
                VariableDeclaratorFormatter().format(it, configMap, version)
            } + ";\n"
    }
}

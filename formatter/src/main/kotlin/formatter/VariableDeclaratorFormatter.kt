package formatter

import utils.ASTNode
import utils.VariableDeclarator

class VariableDeclaratorFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is VariableDeclarator) {
            "VariableDeclaratorFormatter can only format VariableDeclarator nodes."
        }
        val spaceAfterColon = (configMap["spaceAfterColon"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0
        val spaceBeforeColon = (configMap["spaceBeforeColon"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0
        val spacesInAssignSymbol = (configMap["spacesInAssignSymbol"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0

        return astNode.id.name + spaces(spaceBeforeColon) + ':' + spaces(spaceAfterColon) + astNode.type.type +
            when (astNode.init) {
                null -> ""

                else ->
                    spaces(spacesInAssignSymbol) + '=' + spaces(spacesInAssignSymbol) +
                        ExpressionFormatter().format(astNode.init!!, configMap, version)
            }
    }

    private fun spaces(n: Int): String = " ".repeat(n)
}

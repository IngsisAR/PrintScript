package formatter

import ASTNode
import VariableDeclarator

class VariableDeclaratorFormatter : FormatterInterface {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as VariableDeclarator
        val spaceAfterColon = (configMap["spaceAfterColon"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0
        val spaceBeforeColon = (configMap["spaceBeforeColon"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0
        val spacesInAssignSymbol = (configMap["spacesInAssignSymbol"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0

        return astNode.id.name + spaces(spaceBeforeColon) + ':' + spaces(spaceAfterColon) + astNode.type.type +
            when (astNode.init) {
                null -> ""

                else ->
                    spaces(spacesInAssignSymbol) + '=' + spaces(spacesInAssignSymbol) +
                        ExpressionFormatter().format(astNode.init!!, configMap)
            }
    }

    private fun spaces(n: Int): String = " ".repeat(n)
}

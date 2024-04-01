package formatter

import ASTNode
import VariableDeclarator


class VariableDeclaratorFormatter : FormatterInterface {
    override fun format(astNode: ASTNode, configMap: Map<String, Any?>): String {
        astNode as VariableDeclarator
        val spaceBeforeColon = configMap["spaceBeforeColon"]?.let { it as Int } ?: 0
        val spaceAfterColon = configMap["spaceAfterColon"]?.let { it as Int } ?: 0
        val spacesInAssignSymbol = configMap["spacesInAssignSymbol"]?.let { it as Int } ?: 0

        return astNode.id.name + spaces(spaceBeforeColon) + ':' + spaces(spaceAfterColon) + astNode.type.type + when(astNode.init) {
            null -> ""
            else -> spaces(spacesInAssignSymbol) + '=' + spaces(spacesInAssignSymbol) + ExpressionFormatter().format(astNode.init!!, configMap)
        }
    }

    private fun spaces(n: Int): String {
        return " ".repeat(n)
    }
}

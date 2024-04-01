package formatter

import ASTNode
import AssigmentExpression


class AssigmentExpressionFormatter : FormatterInterface {
    override fun format(astNode: ASTNode, configMap: Map<String, Any?>): String {
        astNode as AssigmentExpression
        val spacesInAssignSymbol = configMap["spacesInAssignSymbol"]?.let { it as Int } ?: 0
        return astNode.left.name + spaces(spacesInAssignSymbol) + '=' + spaces(spacesInAssignSymbol) + ExpressionFormatter().format(astNode.right,configMap)
    }

    private fun spaces(n: Int): String {
        return " ".repeat(n)
    }
}

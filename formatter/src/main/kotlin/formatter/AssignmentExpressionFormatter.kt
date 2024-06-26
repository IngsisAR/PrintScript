package formatter

import utils.ASTNode
import utils.AssignmentExpression

class AssignmentExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is AssignmentExpression) {
            "astNode must be AssignmentExpression"
        }
        val spacesInAssignSymbol = (configMap["spacesInAssignSymbol"] as? Number)?.toInt()?.takeIf { it >= 0 } ?: 0
        return astNode.left.name + spaces(spacesInAssignSymbol) + '=' + spaces(spacesInAssignSymbol) +
            ExpressionFormatter().format(astNode.right, configMap, version)
    }

    private fun spaces(n: Int): String = " ".repeat(n)
}

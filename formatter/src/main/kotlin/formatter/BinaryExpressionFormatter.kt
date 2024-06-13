package formatter

import utils.ASTNode
import utils.BinaryExpression

class BinaryExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as BinaryExpression
        return ExpressionFormatter().format(astNode.left, configMap) + " " + astNode.operator + " " +
            ExpressionFormatter().format(astNode.right, configMap)
    }
}

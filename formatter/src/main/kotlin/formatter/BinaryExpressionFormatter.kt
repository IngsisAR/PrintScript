package formatter

import utils.ASTNode
import utils.BinaryExpression

class BinaryExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is BinaryExpression) {
            "astNode is not BinaryExpression"
        }
        return ExpressionFormatter().format(astNode.left, configMap, version) + " " + astNode.operator + " " +
            ExpressionFormatter().format(astNode.right, configMap, version)
    }
}

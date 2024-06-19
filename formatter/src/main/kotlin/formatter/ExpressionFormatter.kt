package formatter

import utils.ASTNode
import utils.AssignmentExpression
import utils.BinaryExpression
import utils.CallExpression
import utils.Expression
import utils.Identifier
import utils.Literal
import utils.StringLiteral

class ExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as Expression
        return when (astNode) {
            is BinaryExpression -> BinaryExpressionFormatter().format(astNode, configMap)
            is Literal -> if (astNode is StringLiteral) "\"${astNode.value}\"" else astNode.value.toString()
            is CallExpression -> CallExpressionFormatter().format(astNode, configMap)
            is Identifier -> astNode.name
            is AssignmentExpression -> AssignmentExpressionFormatter().format(astNode, configMap)
        }
    }
}

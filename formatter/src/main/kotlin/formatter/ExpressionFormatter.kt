package formatter

import ASTNode
import AssignmentExpression
import BinaryExpression
import BooleanLiteral
import CallExpression
import Expression
import Identifier
import Literal
import StringLiteral

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
            is BooleanLiteral -> ""
        }
    }
}

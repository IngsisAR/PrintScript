package formatter

import ASTNode
import AssignmentExpression
import BinaryExpression
import BooleanLiteral
import CallExpression
import Expression
import Identifier
import NumberLiteral
import StringLiteral

class ExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as Expression
        return when (astNode) {
            is BinaryExpression -> BinaryExpressionFormatter().format(astNode, configMap)
            is NumberLiteral -> astNode.value.toString()
            is StringLiteral -> "\"${astNode.value}\""
            is CallExpression -> CallExpressionFormatter().format(astNode, configMap)
            is Identifier -> astNode.name
            is AssignmentExpression -> AssignmentExpressionFormatter().format(astNode, configMap)
            is BooleanLiteral -> ""
        }
    }
}

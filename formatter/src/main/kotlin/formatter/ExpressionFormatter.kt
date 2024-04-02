package formatter

import ASTNode
import AssigmentExpression
import BinaryExpression
import CallExpression
import Expression
import Identifier
import NumberLiteral
import StringLiteral

class ExpressionFormatter : FormatterInterface {
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
            is AssigmentExpression -> AssigmentExpressionFormatter().format(astNode, configMap)
        }
    }
}

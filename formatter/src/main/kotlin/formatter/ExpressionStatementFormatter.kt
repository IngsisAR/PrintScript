package formatter

import ASTNode
import ExpressionStatement


class ExpressionStatementFormatter : FormatterInterface {
    override fun format(astNode: ASTNode, configMap: Map<String, Any?>): String {
        astNode as ExpressionStatement
        return ExpressionFormatter().format(astNode.expression, configMap) + ";\n"
    }
}

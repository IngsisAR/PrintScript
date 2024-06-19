package formatter

import utils.ASTNode
import utils.ExpressionStatement

class ExpressionStatementFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as ExpressionStatement
        return ExpressionFormatter().format(astNode.expression, configMap) + ";\n"
    }
}

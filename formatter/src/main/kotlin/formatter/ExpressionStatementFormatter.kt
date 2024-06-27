package formatter

import utils.ASTNode
import utils.ExpressionStatement

class ExpressionStatementFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is ExpressionStatement) {
            "astNode must be a ExpressionStatement"
        }
        return ExpressionFormatter().format(astNode.expression, configMap, version) + ";\n"
    }
}

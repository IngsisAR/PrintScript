package formatter

import utils.ASTNode
import utils.AssignmentExpression
import utils.BinaryExpression
import utils.BooleanLiteral
import utils.CallExpression
import utils.Expression
import utils.Identifier
import utils.Literal
import utils.StringLiteral
import utils.VersionChecker

class ExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is Expression) {
            "ExpressionFormatter can only format Expression nodes."
        }
        return when (astNode) {
            is BinaryExpression -> BinaryExpressionFormatter().format(astNode, configMap, version)
            is Literal -> {
                if (astNode is StringLiteral) {
                    "\"${astNode.value}\""
                } else if (astNode is BooleanLiteral && !VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
                    error("Unknown ASTNode type")
                } else {
                    astNode.value.toString()
                }
            }
            is CallExpression -> CallExpressionFormatter().format(astNode, configMap, version)
            is Identifier -> astNode.name
            is AssignmentExpression -> AssignmentExpressionFormatter().format(astNode, configMap, version)
        }
    }
}

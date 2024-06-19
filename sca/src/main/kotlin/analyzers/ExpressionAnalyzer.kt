package analyzers

import utils.ASTNode
import utils.CallExpression
import utils.Identifier

class ExpressionAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String =
        when (astNode) {
            is CallExpression -> CallExpressionAnalyzer().analyze(astNode, configMap, version)
            is Identifier -> IdentifierAnalyzer().analyze(astNode, configMap, version)
            else -> ""
        }
}

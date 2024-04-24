package analyzers

import ASTNode
import CallExpression
import Identifier

class ExpressionAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
        version: String,
    ): String =
        when (astNode) {
            is CallExpression -> CallExpressionAnalyzer().analyze(astNode, configMap, lineIndex, version)
            is Identifier -> IdentifierAnalyzer().analyze(astNode, configMap, lineIndex, version)
            else -> ""
        }
}

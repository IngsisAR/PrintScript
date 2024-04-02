package analyzers

import ASTNode
import CallExpression
import Identifier

class ExpressionAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
    ): String =
        when (astNode) {
            is CallExpression -> CallExpressionAnalyzer().analyze(astNode, configMap, lineIndex)
            is Identifier -> IdentifierAnalyzer().analyze(astNode, configMap, lineIndex)
            else -> ""
        }
}

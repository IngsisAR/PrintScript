package analyzers

import utils.ASTNode
import utils.ExpressionStatement

class ExpressionStatementAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        var result = ""
        if (astNode !is ExpressionStatement) return result
        val expression = astNode.expression
        result += ExpressionAnalyzer().analyze(expression, configMap, version)
        return result
    }
}

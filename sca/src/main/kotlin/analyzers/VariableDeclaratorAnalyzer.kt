package analyzers

import ASTNode
import VariableDeclarator

class VariableDeclaratorAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        var result = ""
        if (astNode !is VariableDeclarator) {
            return result
        }
        result += IdentifierAnalyzer().analyze(astNode.id, configMap, version)
        if (astNode.init != null) {
            result += ExpressionAnalyzer().analyze(astNode.init!!, configMap, version)
        }
        return result
    }
}

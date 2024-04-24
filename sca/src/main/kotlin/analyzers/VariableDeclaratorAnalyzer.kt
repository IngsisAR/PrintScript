package analyzers

import ASTNode
import VariableDeclarator

class VariableDeclaratorAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
        version: String,
    ): String {
        var result = ""
        if (astNode !is VariableDeclarator) {
            return result
        }
        result += IdentifierAnalyzer().analyze(astNode.id, configMap, lineIndex, version)
        if (astNode.init != null) {
            result += ExpressionAnalyzer().analyze(astNode.init!!, configMap, lineIndex, version)
        }
        return result
    }
}

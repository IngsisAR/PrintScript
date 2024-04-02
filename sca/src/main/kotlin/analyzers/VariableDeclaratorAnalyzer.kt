package analyzers

import ASTNode
import VariableDeclarator

class VariableDeclaratorAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
    ): String {
        var result = ""
        if (astNode !is VariableDeclarator) {
            return result
        }
        result += IdentifierAnalyzer().analyze(astNode.id, configMap, lineIndex)
        if (astNode.init != null) {
            result += ExpressionAnalyzer().analyze(astNode.init!!, configMap, lineIndex)
        }
        return result
    }
}

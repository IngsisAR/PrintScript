package analyzers

import utils.ASTNode
import utils.VariableDeclaration

class VariableDeclarationAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        var result = ""
        if (astNode is VariableDeclaration) {
            for (declaration in astNode.declarations) {
                result += VariableDeclaratorAnalyzer().analyze(declaration, configMap, version)
            }
        }
        return result
    }
}

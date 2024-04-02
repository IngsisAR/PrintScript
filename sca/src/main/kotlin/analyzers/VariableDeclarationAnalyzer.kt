package analyzers

import ASTNode
import VariableDeclaration

class VariableDeclarationAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
    ): String {
        var result = ""
        if (astNode is VariableDeclaration) {
            for (declaration in astNode.declarations) {
                result += VariableDeclaratorAnalyzer().analyze(declaration, configMap, lineIndex)
            }
        }
        return result
    }
}

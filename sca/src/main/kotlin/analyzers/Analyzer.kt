package analyzers

import ASTNode

interface Analyzer {
    fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
        version: String,
    ): String
}

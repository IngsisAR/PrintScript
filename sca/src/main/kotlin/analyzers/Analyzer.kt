package analyzers

import utils.ASTNode

interface Analyzer {
    fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String
}

package formatter

import utils.ASTNode

interface Formatter {
    fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String
}

package formatter

import ASTNode

interface Formatter {
    fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String
}

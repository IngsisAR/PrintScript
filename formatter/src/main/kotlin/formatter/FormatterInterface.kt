package formatter

import ASTNode


interface FormatterInterface {
    fun format(astNode: ASTNode, configMap: Map<String, Any?>): String
}

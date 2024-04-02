package analyzers

import ASTNode
import Identifier

class IdentifierAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
    ): String {
        if (astNode !is Identifier) {
            return ""
        }
        val identifierCasing = configMap["identifierCasing"]?.let { it as String } ?: "camel case"
        return when (identifierCasing) {
            "camel case" -> checkCamelCase(astNode, lineIndex)
            "snake case" -> checkSnakeCase(astNode, lineIndex)
            else -> ""
        }
    }

    private fun checkSnakeCase(
        astNode: Identifier,
        lineIndex: Int,
    ): String {
        val name = astNode.name
        if (!name.matches(Regex("^[a-z0-9]+(_[a-z0-9]+)*$"))) {
            return "Identifier: $name is not in snake case at (${lineIndex + 1}:${astNode.start})\n"
        }
        return ""
    }

    private fun checkCamelCase(
        astNode: Identifier,
        lineIndex: Int,
    ): String {
        val name = astNode.name
        if (!name.matches(Regex("^[a-z0-9]+(?:[A-Z][a-z0-9]*)*$"))) {
            return "Identifier: $name is not in camel case at (${lineIndex + 1}:${astNode.start})\n"
        }
        return ""
    }
}

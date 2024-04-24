package analyzers

import ASTNode
import Identifier

class IdentifierAnalyzer : Analyzer {
    private val allowedCasing = listOf("camel case", "snake case")

    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
        version: String,
    ): String {
        if (astNode !is Identifier) {
            return ""
        }
        val identifierCasing = configMap["identifierCasing"] ?: "camel case"
        if (identifierCasing !in allowedCasing) {
            return "Invalid identifier casing configuration: $identifierCasing, expected one of: ${allowedCasing.joinToString()}\n"
        }
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

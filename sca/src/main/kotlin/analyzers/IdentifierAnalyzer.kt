package analyzers

import utils.ASTNode
import utils.Identifier

class IdentifierAnalyzer : Analyzer {
    private val allowedCasing = listOf("camel case", "snake case")

    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        if (astNode !is Identifier) {
            return ""
        }
        val identifierCasing = configMap["identifierCasing"] ?: "camel case"
        if (identifierCasing !in allowedCasing) {
            return "Invalid identifier casing configuration: $identifierCasing at (${astNode.line}:${astNode.start}), " +
                "expected one of: ${allowedCasing.joinToString()}"
        }
        return when (identifierCasing) {
            "camel case" -> checkCamelCase(astNode)
            "snake case" -> checkSnakeCase(astNode)
            else -> ""
        }
    }

    private fun checkSnakeCase(astNode: Identifier): String {
        val name = astNode.name
        if (!name.matches(Regex("^[a-z0-9]+(_[a-z0-9]+)*$"))) {
            return "Identifier: $name is not in snake case at (${astNode.line}:${astNode.start})\n"
        }
        return ""
    }

    private fun checkCamelCase(astNode: Identifier): String {
        val name = astNode.name
        if (!name.matches(Regex("^[a-z0-9]+(?:[A-Z][a-z0-9]*)*$"))) {
            return "Identifier: $name is not in camel case at (${astNode.line}:${astNode.start})\n"
        }
        return ""
    }
}

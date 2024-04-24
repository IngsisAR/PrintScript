package analyzers

import ASTNode
import CallExpression
import Identifier
import Literal
import VersionChecker

class CallExpressionAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
        version: String,
    ): String {
        var result = ""
        if (astNode !is CallExpression) return result
        val callee = astNode.callee
        result += IdentifierAnalyzer().analyze(callee, configMap, lineIndex, version)

        val configPrintln = configMap["printlnNoExpressionArguments"] ?: true
        var aux = checkIfAttributeIsInConfigMapAndIsCorrect(configPrintln, "printlnNoExpressionArguments")
        if (aux.isNotEmpty()) {
            return aux
        }

        val arguments = astNode.arguments
        when (callee.name) {
            "println" -> {
                if (arguments.isNotEmpty()) {
                    for (argument in arguments) {
                        if (configPrintln as Boolean &&
                            argument !is Literal &&
                            argument !is Identifier
                        ) {
                            result += "No expressions in println function rule violated at (${lineIndex + 1}:${argument.start})\n"
                        }
                    }
                }
            }
        }

        if (VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
            val configReadInput = configMap["readInputNoExpressionArguments"] ?: true
            aux = checkIfAttributeIsInConfigMapAndIsCorrect(configReadInput, "readInputNoExpressionArguments")
            if (aux.isNotEmpty()) {
                return aux
            }
            when (callee.name) {
                "readInput" -> {
                    if (arguments.isNotEmpty()) {
                        for (argument in arguments) {
                            if (configReadInput as Boolean &&
                                argument !is Literal &&
                                argument !is Identifier
                            ) {
                                result += "No expressions in readInput function rule violated at (${lineIndex + 1}:${argument.start})\n"
                            }
                        }
                    }
                }
            }
        }

        for (argument in arguments) {
            result += ExpressionAnalyzer().analyze(argument, configMap, lineIndex, version)
        }
        return result
    }

    private fun checkIfAttributeIsInConfigMapAndIsCorrect(
        config: Any,
        attribute: String,
    ): String {
        if (config !is Boolean) {
            if (config is String) {
                return "Invalid $attribute configuration value: \"$config\", expected a boolean\n"
            }
            return "Invalid $attribute configuration value: $config, expected a boolean\n"
        }
        return ""
    }
}

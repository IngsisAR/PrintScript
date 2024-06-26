package formatter

import utils.ASTNode
import utils.CallExpression
import utils.ConditionalStatement
import utils.ExpressionStatement
import utils.Statement

class ConditionalStatementFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is ConditionalStatement) {
            "ConditionalStatementFormatter can only format ConditionalStatement nodes."
        }
        return "if(${astNode.test.name}) {\n" +
            contentFormatter(astNode.consequent, configMap, version) +
            (
                if (astNode.alternate.isNotEmpty()) {
                    "} else {\n" +
                        contentFormatter(astNode.alternate, configMap, version) + "\n" +
                        "}\n"
                } else {
                    "}"
                }
                )
    }

    private fun repeatIndentation(configMap: Map<String, Any?>): String {
        val indentationCount = (configMap["identationInsideConditionals"] as? Number)?.toInt()?.takeIf { it >= 1 } ?: 1
        return "    ".repeat(indentationCount)
    }

    private fun contentFormatter(
        astNode: List<Statement>,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        return if (astNode.isNotEmpty()) {
            val aux: MutableList<String> = mutableListOf()
            for (statement in astNode) {
                when (statement) {
                    is ConditionalStatement -> {
                        aux.add(
                            repeatIndentation(configMap) + "if(${statement.test.name}) {\n" +
                                contentFormatter(statement.consequent, configMap, version).split("\n")
                                    .joinToString("\n") { repeatIndentation(configMap) + it } + "\n" +
                                (
                                    if (statement.alternate.isNotEmpty()) {
                                        repeatIndentation(configMap) + "} else {\n" +
                                            contentFormatter(statement.alternate, configMap, version).split("\n")
                                                .joinToString("\n") { repeatIndentation(configMap) + it } + "\n" +
                                            repeatIndentation(configMap) + "}\n"
                                    } else {
                                        repeatIndentation(configMap) + "}"
                                    }
                                    ),
                        )
                        continue
                    }

                    is ExpressionStatement -> {
                        if (statement.expression is CallExpression) {
                            val aux2 =
                                CallExpressionFormatter().format(statement.expression as CallExpression, configMap, version)
                                    .split("\n").toMutableList()
                            aux2.replaceAll { repeatIndentation(configMap) + it }
                            aux += aux2.joinToString("\n") + ";"
                        } else {
                            aux.add(repeatIndentation(configMap) + StatementFormatter().format(statement, configMap, version))
                        }
                        continue
                    }

                    else -> {
                        aux.add(repeatIndentation(configMap) + StatementFormatter().format(statement, configMap, version))
                    }
                }
            }
            aux.joinToString("\n")
        } else {
            ""
        }
    }
}

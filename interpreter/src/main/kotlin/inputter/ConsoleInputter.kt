package inputter

import interpreter.CallExpressionInterpreter
import interpreter.IdentifierInterpreter
import interpreter.VariableInfo
import utils.ASTNode
import utils.CallExpression
import utils.EnvironmentProvider
import utils.Identifier
import utils.InputProvider
import utils.OutputProvider
import utils.StringLiteral

class ConsoleInputter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
    private val environmentProvider: EnvironmentProvider,
) : Inputter {
    override fun readInput(node: ASTNode): Any {
        val text =
            when (node) {
                is StringLiteral -> node.value
                is Identifier -> {
                    val result =
                        IdentifierInterpreter(variableMap, version).interpret(node)
                            ?: throw IllegalArgumentException("Variable ${node.name} is not initialized at (${node.line}:${node.start})")
                    if (result is String) {
                        result
                    } else {
                        throw IllegalArgumentException(
                            "Expected String argument but was ${result::class.java.simpleName} " +
                                "at (${node.line}:${node.start})",
                        )
                    }
                }
                is CallExpression -> {
                    val result =
                        CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                            node,
                        )
                    if (result is String) {
                        result
                    } else {
                        throw IllegalArgumentException(
                            "Expected String argument but was ${result::class.java.simpleName} " +
                                "at (${node.line}:${node.start})",
                        )
                    }
                }
                else -> ""
            }
        outputProvider.print(text)
        val input = inputProvider.readInput()
        return when {
            input.matches("true|false".toRegex()) -> input.toBoolean()
            input.matches("\\d+(\\.\\d+)?".toRegex()) -> {
                input.toIntOrNull() ?: (input.toDoubleOrNull() ?: "")
            }
            else -> input
        }
    }
}

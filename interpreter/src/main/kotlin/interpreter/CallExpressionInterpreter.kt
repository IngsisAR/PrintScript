package interpreter

import inputter.ConsoleInputter
import utils.ASTNode
import utils.BinaryExpression
import utils.CallExpression
import utils.EnvironmentProvider
import utils.Identifier
import utils.InputProvider
import utils.Literal
import utils.OutputProvider
import utils.StringLiteral
import utils.VersionChecker

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
    private val environmentProvider: EnvironmentProvider,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is CallExpression) { "Node at (${node.line}:${node.start}) must be an CallExpression" }
        return when (node.callee.name) {
            "println" -> handlePrintln(node)
            "readEnv" -> handleReadEnv(node) ?: ""
            "readInput" -> handleReadInput(node)
            else -> throw IllegalArgumentException("Unsupported function '${node.callee.name}' at (${node.line}:${node.start})")
        }
    }

    private fun handlePrintln(node: CallExpression) {
        if (node.arguments.size > 1) {
            throw IllegalArgumentException("Function 'println' expects at most 1 argument at (${node.line}:${node.start})")
        }
        if (node.arguments.isEmpty()) {
            outputProvider.print("")
            return
        }
        when (val argument = node.arguments[0]) {
            is Literal -> outputProvider.print(argument.value.toString())
            is Identifier -> {
                val value = IdentifierInterpreter(variableMap, version).interpret(argument)
                outputProvider.print(value.toString())
            }
            is CallExpression -> {
                val value =
                    CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                        argument,
                    )
                outputProvider.print(value.toString())
            }
            is BinaryExpression -> {
                val value =
                    BinaryExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                        argument,
                    )
                outputProvider.print(value.toString())
            }
            else -> throw IllegalArgumentException("Function 'println' expects a string argument at (${argument.line}:${argument.start})")
        }
    }

    private fun handleReadEnv(function: CallExpression): String? {
        val versionChecker = VersionChecker()
        if (versionChecker.versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
            if (function.arguments.size != 1) {
                if (function.arguments.isEmpty()) {
                    throw IllegalArgumentException("Function 'readEnv' expects 1 argument at (${function.line}:${function.callee.end})")
                }
                throw IllegalArgumentException("Function 'readEnv' expects 1 argument at (${function.line}:${function.arguments[0].start})")
            }
            return when (val arg = function.arguments[0]) {
                is StringLiteral -> {
                    environmentProvider.getEnv(arg.value)
                }
                is Identifier -> {
                    val value =
                        IdentifierInterpreter(variableMap, version).interpret(arg)
                            ?: throw IllegalArgumentException("Variable ${arg.name} is not initialized at (${arg.line}:${arg.start})")
                    if (value is String) {
                        environmentProvider.getEnv(value)
                    } else {
                        throw IllegalArgumentException("Function 'readEnv' expects a string argument at (${arg.line}:${arg.start})")
                    }
                }
                is CallExpression -> {
                    val value =
                        CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                            arg,
                        )
                    if (value is String) {
                        environmentProvider.getEnv(value)
                    } else {
                        throw IllegalArgumentException("Function 'readEnv' expects a string argument at (${arg.line}:${arg.start})")
                    }
                }
                is BinaryExpression -> {
                    val value =
                        BinaryExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                            arg,
                        )
                    if (value is String) {
                        environmentProvider.getEnv(value)
                    } else {
                        throw IllegalArgumentException("Function 'readEnv' expects a string argument at (${arg.line}:${arg.start})")
                    }
                }
                else -> throw IllegalArgumentException("Function 'readEnv' expects a string argument at (${arg.line}:${arg.start})")
            }
        } else {
            throw IllegalArgumentException("Unsupported function '${function.callee.name}' at (${function.line}:${function.start})")
        }
    }

    private fun handleReadInput(function: CallExpression): Any {
        val versionChecker = VersionChecker()
        if (versionChecker.versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
            if (function.arguments.size != 1) {
                if (function.arguments.isEmpty()) {
                    throw IllegalArgumentException("Function 'readInput' expects 1 argument at (${function.line}:${function.callee.end})")
                }
                throw IllegalArgumentException(
                    "Function 'readInput' expects 1 argument at (${function.line}:${function.arguments[0].start})",
                )
            }
            return ConsoleInputter(variableMap, version, outputProvider, inputProvider, environmentProvider).readInput(
                function.arguments[0],
            )
        } else {
            throw IllegalArgumentException("Unsupported function '${function.callee.name}' at (${function.line}:${function.start})")
        }
    }
}

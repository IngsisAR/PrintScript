import inputter.ConsoleInputter
import outputter.ConsoleOutputter

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is CallExpression) { "Node at (${node.line}:${node.start}) must be an CallExpression" }
        return when (node.callee.name) {
            "println" -> ConsoleOutputter(variableMap, version).output(node)
            "readEnv" -> handleReadEnv(node)
            "readInput" -> handleReadInput(node)
            else -> throw IllegalArgumentException("Unsupported function '${node.callee.name}' at (${node.line}:${node.start})")
        }
    }

    private fun handleReadEnv(function: CallExpression): String {
        val versionChecker = VersionChecker()
        if (versionChecker.versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
            if (function.arguments.size != 1) {
                throw IllegalArgumentException("Function 'readEnv' expects 1 argument at (${function.line}:${function.start})")
            }
            return when (val arg = function.arguments[0]) {
                is StringLiteral -> {
                    System.getenv(arg.value)
                }
                is Identifier -> {
                    val value = IdentifierInterpreter(variableMap, version).interpret(arg)
                    if (value is String) {
                        System.getenv(value)
                    } else {
                        throw IllegalArgumentException("Function 'readEnv' expects a string argument at (${arg.line}:${arg.start})")
                    }
                }
                is CallExpression -> {
                    val value = CallExpressionInterpreter(variableMap, version).interpret(arg)
                    if (value is String) {
                        System.getenv(value)
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
            return ConsoleInputter(variableMap, version).readInput(function.arguments[0])
        } else {
            throw IllegalArgumentException("Unsupported function '${function.callee.name}' at (${function.line}:${function.start})")
        }
    }
}

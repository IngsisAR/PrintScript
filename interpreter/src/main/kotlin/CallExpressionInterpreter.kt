import outputter.ConsoleOutputter

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is CallExpression) { "Node must be an CallExpression" }
        return when (node.callee.name) {
            "println" -> ConsoleOutputter(variableMap).output(node)
            "readEnv" -> return readEnvironmentVariables(node.arguments[0] as StringLiteral)
            "readInput" -> return readUserInput(node.arguments[0] as StringLiteral)
            else -> throw IllegalArgumentException("Function '${node.callee.name}' not found")
        }
    }

    private fun readUserInput(text: StringLiteral?): Any {
        print(text?.value ?: "")
        val input = readlnOrNull()
        return when {
            input?.matches("true|false".toRegex()) == true -> input.toBoolean()
            input?.matches("\\d+(\\.\\d+)?".toRegex()) == true -> input.toDoubleOrNull() ?: ""
            input?.matches("\"[^\"]*\"|'[^']*'".toRegex()) == true -> input.removeSurrounding("\"").removeSurrounding("'")
            else -> input ?: ""
        }
    }

    private fun readEnvironmentVariables(key: StringLiteral): String {
        return System.getenv(key.value)
    }
}

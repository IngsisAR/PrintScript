import outputter.ConsoleOutputter

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is CallExpression) { "Node must be an CallExpression" }
        return when (node.callee.name) {
            "println" -> ConsoleOutputter(variableMap).output(node)
            "readEnv" -> readEnvironmentVariables(node.arguments[0] as StringLiteral)
            "readInput" -> readUserInput(node.arguments[0] as StringLiteral)
            else -> throw IllegalArgumentException("Function '${node.callee.name}' not found")
        }
    }

    private fun readUserInput(text: StringLiteral?): Any {
        print(text?.value ?: "")
        val input = readLine()
        println(input)
        return input!!
    }

    private fun readEnvironmentVariables(key: StringLiteral): String {
        return System.getenv(key.value)
    }
}

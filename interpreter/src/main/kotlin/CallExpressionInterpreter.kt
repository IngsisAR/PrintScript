import outputter.ConsoleOutputter

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is CallExpression) { "Node must be an CallExpression" }
        return when (node.callee.name) {
            "println" -> ConsoleOutputter(variableMap).output(node)
            "readEnv" -> return readEnvironmentVariables(node.arguments[0] as StringLiteral)
            else -> throw IllegalArgumentException("Function '${node.callee.name}' not found")
        }
    }

    private fun readEnvironmentVariables(key: StringLiteral): String{
        return System.getenv(key.value)
    }
}

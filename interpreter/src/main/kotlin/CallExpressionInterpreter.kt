import outputter.ConsoleOutputter

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode) {
        require(node is CallExpression) { "Node must be an CallExpression" }
        if (node.callee.name == "println") {
            ConsoleOutputter(variableMap).output(node)
            return
        }
        throw IllegalArgumentException("Function '${node.callee.name}' not found")
    }
}

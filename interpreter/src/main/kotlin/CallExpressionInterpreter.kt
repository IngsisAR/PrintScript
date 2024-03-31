class CallExpressionInterpreter(
    val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode) {
        node as CallExpression
        if (node.callee.name == "println") { // should be an outputter
            val output: StringBuilder = StringBuilder()
            node.arguments.forEach { arg ->
                when (arg) {
                    is NumberLiteral -> output.append(arg.value)
                    is StringLiteral -> output.append(arg.value)
                    is BinaryExpression -> output.append(BinaryExpressionInterpreter(variableMap).interpret(arg))
                    is Identifier -> output.append(IdentifierInterpreter(variableMap).interpret(arg))
                    else -> throw IllegalArgumentException("Function not found")
                }
            }
            print(output)
            return
        }
        throw IllegalArgumentException("Function not found")
    }
}

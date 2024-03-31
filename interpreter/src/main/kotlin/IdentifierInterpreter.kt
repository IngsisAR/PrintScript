class IdentifierInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        node as Identifier
        val variableInfo = variableMap[node.name] ?: throw IllegalArgumentException("Variable not found")

        return when (variableInfo.type) {
            "string" -> variableInfo.value.toString()
            "number" -> variableInfo.value!!.toBigDecimal()
            else -> throw IllegalArgumentException("Unsupported variable type")
        }
    }
}

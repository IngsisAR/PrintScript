class IdentifierInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is Identifier) { "Node must be an Identifier" }
        val variableInfo = variableMap[node.name] ?: throw IllegalArgumentException("Variable not found")
        require(variableInfo.value != null) { "Variable value is null: ${node.name}" }
        return when (variableInfo.type) {
            "string" -> variableInfo.value.toString()
            "number" -> variableInfo.value.toBigDecimal()
            else -> throw IllegalArgumentException("Unsupported variable type")
        }
    }
}

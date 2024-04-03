class AssigmentExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        node as AssigmentExpression
        val id = node.left.name
        val variable = variableMap[id]
        if (variable == null || variable.isMutable == false) throw IllegalArgumentException("Variable not mutable or not found")
        val newValue =
            when (val right = node.right) {
                is NumberLiteral -> right.value.toString()
                is StringLiteral -> right.value
                is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(right)
                is Identifier -> IdentifierInterpreter(variableMap).interpret(right)
                else -> throw IllegalArgumentException("Function not found")
            }
        val updatedMap = variableMap.toMutableMap()
        updatedMap[id] = variable.copy(value = newValue.toString())
        return updatedMap
    }
}

class AssigmentExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is AssigmentExpression) { "Node must be an AssignmentExpression" }
        val id = node.left.name
        val variable = variableMap[id] ?: throw IllegalArgumentException("Variable not found")
        require(variable.isMutable == true) { "Variable is not mutable: $id" }
        val newValue =
            when (val right = node.right) {
                is NumberLiteral -> right.value
                is StringLiteral -> right.value
                is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(right)
                is Identifier -> IdentifierInterpreter(variableMap).interpret(right)
                else -> throw IllegalArgumentException("Node not found")
            }
        checkTypeMatches(variable, newValue)
        val updatedMap = variableMap.toMutableMap()
        updatedMap[id] = variable.copy(value = newValue.toString())
        return updatedMap
    }

    private fun checkTypeMatches(
        variable: VariableInfo,
        newValue: Any,
    ) {
        val expectedType =
            when (newValue) {
                is Number -> "number"
                is String -> "string"
                else -> throw IllegalArgumentException("Unsupported value type: ${newValue::class.simpleName}")
            }
        require(variable.type == expectedType) {
            "Type mismatch: expected ${variable.type}, got ${newValue::class.simpleName}"
        }
    }
}

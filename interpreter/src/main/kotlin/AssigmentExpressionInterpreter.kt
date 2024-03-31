class AssigmentExpressionInterpreter(
    val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        node as AssigmentExpression
        val id = node.left.name
        val variable = variableMap[id]
        if (variable == null || variable.isMutable == false) throw IllegalArgumentException("Variable not mutable or not found")

        return variableMap
    }
}

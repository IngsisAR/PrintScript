class InterpreterImpl(
    val variableMap: Map<String, VariableInfo> = emptyMap(),
) {
    fun interpret(node: ASTNode): InterpreterImpl {
        var internalVariableMap: Map<String, VariableInfo> = variableMap

        when (node) {
            is BinaryExpression -> BinaryExpressionInterpreter(internalVariableMap).interpret(node)
            is AssigmentExpression -> internalVariableMap = AssigmentExpressionInterpreter(internalVariableMap).interpret(node)
            is CallExpression -> CallExpressionInterpreter(internalVariableMap).interpret(node)
            is Identifier -> IdentifierInterpreter(variableMap).interpret(node)
            is NumberLiteral -> node.value
            is StringLiteral -> node.value
            is ExpressionStatement -> return interpret(node.expression) // should check this return
            is VariableDeclaration -> internalVariableMap = VariableDeclarationInterpreter(internalVariableMap).interpret(node)
            is TypeReference -> node.type
            else -> throw IllegalArgumentException("Invalid node type: ${node::class.simpleName}")
        }
        return InterpreterImpl(internalVariableMap)
    }
}

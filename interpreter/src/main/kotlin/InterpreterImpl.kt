class InterpreterImpl(
    val variableMap: Map<String, VariableInfo> = emptyMap(),
) {
    fun interpret(node: ASTNode): InterpreterImpl {
        var internalVariableMap: Map<String, VariableInfo> = variableMap

        when (node) {
            is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(node)
            is AssigmentExpression -> internalVariableMap = AssigmentExpressionInterpreter(variableMap).interpret(node)
            is CallExpression -> CallExpressionInterpreter(variableMap).interpret(node)
            is Identifier -> IdentifierInterpreter(variableMap).interpret(node)
            is ExpressionStatement -> return interpret(node.expression)
            is VariableDeclaration -> internalVariableMap = VariableDeclarationInterpreter(variableMap).interpret(node)
            else -> throw IllegalArgumentException("Invalid node type: ${node::class.simpleName}")
        }
        return InterpreterImpl(internalVariableMap)
    }
}

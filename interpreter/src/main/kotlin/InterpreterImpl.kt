class InterpreterImpl(private val variableMap: Map<String, VariableInfo> = emptyMap()) {

    fun interpret(node: ASTNode) : InterpreterImpl{
        var internalVariableMap: Map<String, VariableInfo> = variableMap

        when (node) {
            is BinaryExpression -> BinaryExpressionInterpreter(internalVariableMap).interpret(node)

            is AssigmentExpression -> AssigmentExpressionInterpreter(internalVariableMap).interpret(node)

            is CallExpression -> CallExpressionInterpreter(internalVariableMap).interpret(node)

            is Identifier -> node.name

            is NumberLiteral -> node.value

            is StringLiteral -> node.value

            is Program -> TODO()

            is ExpressionStatement -> interpret(node.expression)

            is VariableDeclaration -> internalVariableMap = VariableDeclarationInterpreter(internalVariableMap).interpret(node)

            is TypeReference -> node.type

            else -> throw IllegalArgumentException("Invalid node type: ${node::class.simpleName}")
        }
        return InterpreterImpl(internalVariableMap)
    }
}

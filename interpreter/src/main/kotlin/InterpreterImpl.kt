class InterpreterImpl {
    private val variableMap: Map<String, VariableInfo> = mapOf()
    fun interpret(node: ASTNode){
        when(node){
            is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(node)
            is AssigmentExpression -> TODO()
            is CallExpression -> TODO()
            is Identifier -> node.name
            is NumberLiteral -> node.value
            is StringLiteral -> node.value
            is Program -> TODO()
            is ExpressionStatement -> TODO()
            is VariableDeclaration -> node.declarations.forEach{ declaration -> VariableDeclaratorInterpreter(variableMap, node.kind).interpret(declaration)}
            is TypeReference -> node.type
            else -> throw IllegalArgumentException("Invalid node type: ${node::class.simpleName}")
        }
    }
}

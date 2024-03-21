class Interpreter {
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
            is VariableDeclaration -> TODO()
            is TypeReference -> node.type
            is VariableDeclarator -> VariableDeclaratorInterpreter(variableMap).interpret(node)
        }
    }
}

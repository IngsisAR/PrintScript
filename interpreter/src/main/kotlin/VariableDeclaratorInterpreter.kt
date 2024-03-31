class VariableDeclaratorInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    val kind: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        node as VariableDeclarator
        val id = node.id.name // should check if already exists
        val type = node.type.type
        val value =
            when (val init = node.init) {
                is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(init)
                is Identifier -> IdentifierInterpreter(variableMap).interpret(init)
                is NumberLiteral -> init.value
                is StringLiteral -> init.value
                else -> throw IllegalArgumentException("Unsupported init type")
            }
        return mapOf(id to VariableInfo(type, value as String, kind == "let"))
    }
}

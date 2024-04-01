class VariableDeclaratorInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    val kind: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        node as VariableDeclarator
        val id = node.id.name // should check if already exists
        val type = node.type.type
        val value =
            node.init?.let {
                when (it) {
                    is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(it)
                    is Identifier -> IdentifierInterpreter(variableMap).interpret(it)
                    is NumberLiteral -> it.value
                    is StringLiteral -> it.value
                    else -> throw IllegalArgumentException("Unsupported init type")
                }
            }
        return mapOf(id to VariableInfo(type, value?.toString(), kind == "let"))
    }
}

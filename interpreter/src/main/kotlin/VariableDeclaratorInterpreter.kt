class VariableDeclaratorInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val kind: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is VariableDeclarator) { "Node must be a VariableDeclarator" }
        val id = node.id.name
        require(variableMap[id] == null) { "Variable '$id' already exists" }

        val type = node.type.type
        val value =
            node.init?.let {
                when (it) {
                    is BinaryExpression -> BinaryExpressionInterpreter(variableMap).interpret(it)
                    is Identifier -> IdentifierInterpreter(variableMap).interpret(it)
                    is Literal -> it.value
                    else -> throw IllegalArgumentException("Unsupported init type: ${it::class.simpleName}")
                }
            }

        return mapOf(id to VariableInfo(type, value?.toString(), kind == "let"))
    }
}

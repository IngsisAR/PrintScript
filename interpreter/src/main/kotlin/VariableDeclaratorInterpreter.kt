

class VariableDeclaratorInterpreter(
    val variableMap: Map<String, VariableInfo>,
) : InterpreterInterface {
    override fun interpret(node: ASTNode): Any {
        node as VariableDeclarator
        val id = node.id.name
        val type = node.type?.type
        val value = interpret(node.init as ASTNode) // should go to Interpreter().interpret(node.init)

        return variableMap + (id to VariableInfo(type as String, value.toString()))
    }
}

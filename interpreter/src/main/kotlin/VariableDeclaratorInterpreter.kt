
class VariableDeclaratorInterpreter(private val variableMap:Map<String, VariableInfo>, val kind:String) : Interpreter {

    override fun interpret(node: ASTNode): Any {
        node as VariableDeclarator
        val id = node.id.name
        val type = node.type.type
        val value = InterpreterImpl().interpret(node.init as ASTNode) // should go to Interpreter().interpret(node.init)

        return variableMap + (id to VariableInfo(type, value.toString(), kind == "LET" ))
    }
}
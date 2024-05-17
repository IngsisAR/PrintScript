sealed interface Interpreter {
    fun interpret(node: ASTNode): Any?
}

sealed interface InterpreterInterface {
    fun interpret(node: ASTNode): Any
}

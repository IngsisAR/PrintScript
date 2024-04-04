class VariableDeclarationInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is VariableDeclaration) { "Node must be a VariableDeclaration" }

        return node.declarations.fold(variableMap) { acc, declaration ->
            val newVariableMap = VariableDeclaratorInterpreter(acc, node.kind).interpret(declaration)
            acc + newVariableMap
        }
    }
}

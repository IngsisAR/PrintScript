class VariableDeclarationInterpreter(
    private val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        node as VariableDeclaration
        val updatedVariableMap = mutableMapOf<String, VariableInfo>()
        updatedVariableMap.putAll(variableMap)

        node.declarations.forEach { declaration ->
            val newVariableMap = VariableDeclaratorInterpreter(variableMap, node.kind).interpret(declaration)
            updatedVariableMap.putAll(newVariableMap)
        }
        return updatedVariableMap
    }
}

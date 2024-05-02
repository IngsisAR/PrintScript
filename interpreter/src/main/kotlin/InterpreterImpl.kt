class InterpreterImpl(
    val variableMap: Map<String, VariableInfo> = emptyMap(),
    val version: String,
) {
    fun interpret(node: ASTNode): InterpreterImpl {
        var internalVariableMap: Map<String, VariableInfo> = variableMap

        when {
            node is BinaryExpression -> BinaryExpressionInterpreter(variableMap, version).interpret(node)
            node is AssignmentExpression -> internalVariableMap = AssignmentExpressionInterpreter(variableMap, version).interpret(node)
            node is CallExpression -> CallExpressionInterpreter(variableMap, version).interpret(node)
            node is Identifier -> IdentifierInterpreter(variableMap, version).interpret(node)
            node is ExpressionStatement -> return interpret(node.expression)
            node is ConditionalStatement &&
                VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version) ->
                return ConditionalStatementInterpreter(variableMap, version).interpret(node)
            node is VariableDeclaration -> internalVariableMap = VariableDeclarationInterpreter(variableMap, version).interpret(node)
            else -> throw IllegalArgumentException("Invalid node type: ${node::class.simpleName}")
        }
        return InterpreterImpl(internalVariableMap, version)
    }
}

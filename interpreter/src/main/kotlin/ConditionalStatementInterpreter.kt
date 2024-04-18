class ConditionalStatementInterpreter(
    val variableMap: Map<String, VariableInfo>,
) : Interpreter {
    override fun interpret(node: ASTNode): InterpreterImpl {
        require(node is ConditionalStatement) { "Node must be a ConditionalStatement" }
        val conditionValue = IdentifierInterpreter(variableMap).interpret(node.test)
        if (conditionValue !is Boolean) {
            throw IllegalArgumentException("Condition must be a boolean")
        }
        var interpreter = InterpreterImpl(variableMap)
        if (conditionValue) {
            for (statement in node.consequent) {
                interpreter = interpreter.interpret(statement)
            }
        } else {
            for (statement in node.alternate) {
                interpreter = interpreter.interpret(statement)
            }
        }
        return interpreter
    }
}

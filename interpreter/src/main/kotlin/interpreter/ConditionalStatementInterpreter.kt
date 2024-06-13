package interpreter

import utils.ASTNode
import utils.ConditionalStatement

class ConditionalStatementInterpreter(
    val variableMap: Map<String, VariableInfo>,
    private val version: String,
) : Interpreter {
    override fun interpret(node: ASTNode): InterpreterImpl {
        require(node is ConditionalStatement) { "Node must be a ConditionalStatement" }
        val conditionValue = IdentifierInterpreter(variableMap, version).interpret(node.test)
        if (conditionValue !is Boolean) {
            throw IllegalArgumentException("Condition must be a boolean at (${node.test.line}:${node.test.start})")
        }
        var interpreter = InterpreterImpl(variableMap, version)
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

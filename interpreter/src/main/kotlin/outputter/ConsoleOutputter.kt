package outputter

import ASTNode
import BinaryExpression
import BinaryExpressionInterpreter
import CallExpression
import Identifier
import IdentifierInterpreter
import Literal
import VariableInfo

class ConsoleOutputter(
    private val variableMap: Map<String, VariableInfo>,
) : Outputter {
    override fun output(node: ASTNode) {
        require(node is CallExpression) { "Node must be a CallExpression" }
        val output: StringBuilder = StringBuilder()
        node.arguments.forEach { arg ->
            when (arg) {
                is Literal -> output.append(arg.value)
                is BinaryExpression -> output.append(BinaryExpressionInterpreter(variableMap).interpret(arg))
                is Identifier -> output.append(IdentifierInterpreter(variableMap).interpret(arg))
                else -> throw IllegalArgumentException("Function not found")
            }
        }
        print(output.toString() + "\n")
    }
}

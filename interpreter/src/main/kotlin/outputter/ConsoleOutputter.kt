package outputter

import ASTNode
import BinaryExpression
import BinaryExpressionInterpreter
import CallExpression
import Identifier
import IdentifierInterpreter
import NumberLiteral
import StringLiteral
import VariableInfo

class ConsoleOutputter(
    private val variableMap: Map<String, VariableInfo>,
) : Outputter {
    override fun output(node: ASTNode) {
        node as CallExpression
        val output: StringBuilder = StringBuilder()
        node.arguments.forEach { arg ->
            when (arg) {
                is NumberLiteral -> output.append(arg.value)
                is StringLiteral -> output.append(arg.value)
                is BinaryExpression -> output.append(BinaryExpressionInterpreter(variableMap).interpret(arg))
                is Identifier -> output.append(IdentifierInterpreter(variableMap).interpret(arg))
                else -> throw IllegalArgumentException("Function not found")
            }
        }
        print(output.toString() + "\n")
    }
}

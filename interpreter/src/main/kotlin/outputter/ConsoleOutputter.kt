package outputter

import interpreter.BinaryExpressionInterpreter
import interpreter.IdentifierInterpreter
import interpreter.VariableInfo
import utils.ASTNode
import utils.BinaryExpression
import utils.CallExpression
import utils.Identifier
import utils.Literal
import utils.OutputProvider

class ConsoleOutputter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    private val outputProvider: OutputProvider,
) : Outputter {
    override fun output(node: ASTNode) {
        require(node is CallExpression) { "Node must be a CallExpression at (${node.line}:${node.start})" }
        val output: StringBuilder = StringBuilder()
        node.arguments.forEach { arg ->
            when (arg) {
                is Literal -> output.append(arg.value)
                is BinaryExpression -> output.append(BinaryExpressionInterpreter(variableMap, version).interpret(arg))
                is Identifier -> output.append(IdentifierInterpreter(variableMap, version).interpret(arg))
                else -> throw IllegalArgumentException("Function not found at (${arg.line}:${arg.start})")
            }
        }
        outputProvider.print(output.toString())
    }
}

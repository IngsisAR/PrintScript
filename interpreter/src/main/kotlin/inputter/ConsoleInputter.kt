package inputter

import ASTNode
import CallExpression
import CallExpressionInterpreter
import Identifier
import IdentifierInterpreter
import StringLiteral
import VariableInfo

class ConsoleInputter(private val variableMap: Map<String, VariableInfo>, private val version: String) : Inputter {
    override fun readInput(node: ASTNode): Any {
        val text =
            when (node) {
                is StringLiteral -> node.value
                is Identifier -> return IdentifierInterpreter(variableMap, version).interpret(node)
                is CallExpression -> return CallExpressionInterpreter(variableMap, version).interpret(node)
                else -> ""
            }
        print(text)
        val input = readlnOrNull()
        return when {
            input?.matches("true|false".toRegex()) == true -> input.toBoolean()
            input?.matches("\\d+(\\.\\d+)?".toRegex()) == true -> {
                input.toIntOrNull() ?: (input.toDoubleOrNull() ?: "")
            }
            input?.matches("\"[^\"]*\"|'[^']*'".toRegex()) == true -> input.removeSurrounding("\"").removeSurrounding("'")
            else -> input ?: ""
        }
    }
}

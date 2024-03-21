import java.math.BigDecimal

class BinaryExpressionInterpreter(val variableMap:Map<String, VariableInfo>) : Interpreter {
    override fun interpret(node: ASTNode) : Any{
        node as BinaryExpression
        val leftValue = when(val left = node.left){
            is NumberLiteral -> left.value
            is StringLiteral -> left.value
            is BinaryExpression -> interpret(left)
            else -> TODO()
        }
        val rightValue = when(val right = node.right){
            is NumberLiteral -> right.value
            is StringLiteral -> right.value
            is BinaryExpression -> interpret(right)
            else -> TODO()
        }
        return handleOperation(leftValue, rightValue, node.operator)
    }

    @Throws(IllegalArgumentException::class)
    private fun handleOperation(leftValue: Any, rightValue: Any, operator: String): Any {
        return when (operator) {
            "+" -> when {
                leftValue is String && rightValue is String -> leftValue + rightValue
                leftValue is Number && rightValue is Number -> leftValue as BigDecimal + rightValue as BigDecimal
                leftValue is Number && rightValue is String || leftValue is String && rightValue is Number -> leftValue.toString() + rightValue.toString()
                else -> throw IllegalArgumentException("Invalid operands for '+': $leftValue, $rightValue")
            }
            "-" -> when {
                leftValue is Number && rightValue is Number -> leftValue.toDouble() - rightValue.toDouble()
                else -> throw IllegalArgumentException("Invalid operands for '-': $leftValue, $rightValue")
            }
            "*" -> when {
                leftValue is Number && rightValue is Number -> leftValue.toDouble() * rightValue.toDouble()
                else -> throw IllegalArgumentException("Invalid operands for '*': $leftValue, $rightValue")
            }
            "/" -> when {
                leftValue is Number && rightValue is Number -> leftValue.toDouble() / rightValue.toDouble()
                else -> throw IllegalArgumentException("Invalid operands for '/': $leftValue, $rightValue")
            }
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }
}
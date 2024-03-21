
data class VariableInfo(val type:String, val value:String?)
class Interpreter {
    private val variableMap: Map<String, VariableInfo> = mapOf()
    fun interpret(node: ASTNode){
        when(node){
            is BinaryExpression -> interpretBinaryExpression(node)
            is AssigmentExpression -> TODO()
            is CallExpression -> TODO()
            is Identifier -> node.name
            is NumberLiteral -> node.value
            is StringLiteral -> node.value
            is Program -> TODO()
            is ExpressionStatement -> TODO()
            is VariableDeclaration -> node.declarations.forEach{ declaration -> interpretVariableDeclarator(declaration)}
            is TypeReference -> node.type
            is VariableDeclarator -> interpretVariableDeclarator(node)
        }
    }

    private fun interpretVariableDeclarator(node: VariableDeclarator): Map<String, VariableInfo> {
        val id = node.id.name
        val type = node.type.type
        val value = interpret(node.init as ASTNode)

        return variableMap + (id to VariableInfo(type as String, value.toString()))
    }

    private fun interpretBinaryExpression(node: BinaryExpression) : Any{
        val leftValue = when(val left = node.left){
            is NumberLiteral -> left.value
            is StringLiteral -> left.value
            is BinaryExpression -> interpretBinaryExpression(left)
            else -> TODO()
        }
        val rightValue = when(val right = node.right){
            is NumberLiteral -> right.value
            is StringLiteral -> right.value
            is BinaryExpression -> interpretBinaryExpression(right)
            else -> TODO()
        }
        return handleOperation(leftValue, rightValue, node.operator)
    }

    private fun handleOperation(leftValue: Any, rightValue: Any, operator: String): Any {
        return when (operator) {
            "+" -> when {
                leftValue is String && rightValue is String -> leftValue + rightValue
                leftValue is Number && rightValue is Number -> leftValue.toDouble() + rightValue.toDouble()
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

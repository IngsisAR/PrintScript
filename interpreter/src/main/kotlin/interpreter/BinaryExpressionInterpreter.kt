package interpreter

import utils.ASTNode
import utils.BinaryExpression
import utils.CallExpression
import utils.Expression
import utils.Identifier
import utils.InputProvider
import utils.NumberLiteral
import utils.OutputProvider
import utils.StringLiteral
import java.math.BigDecimal

class BinaryExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    val outputProvider: OutputProvider,
    val inputProvider: InputProvider,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is BinaryExpression) { "Node must be an BinaryExpression" }
        val leftValue = getOperandValue(node.left)
        val rightValue = getOperandValue(node.right)
        return handleOperation(leftValue, rightValue, node.operator, node.line, node.start)
    }

    private fun getOperandValue(operand: Expression): Any =
        when (operand) {
            is NumberLiteral -> operand.value
            is StringLiteral -> operand.value
            is BinaryExpression -> interpret(operand)
            is CallExpression -> {
                val operandValue =
                    CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider)
                        .interpret(operand)
                if (operandValue is String || operandValue is Number) {
                    operandValue
                } else {
                    throw IllegalArgumentException("Invalid operand at (${operand.line}:${operand.start})")
                }
            }
            is Identifier ->
                IdentifierInterpreter(variableMap, version).interpret(operand)
                    ?: throw IllegalArgumentException("Null value operand at (${operand.line}:${operand.start})")
            else -> throw IllegalArgumentException("Invalid operand at (${operand.line}:${operand.end})")
        }

    @Throws(IllegalArgumentException::class)
    private fun handleOperation(
        leftValue: Any,
        rightValue: Any,
        operator: String,
        line: Int,
        start: Int,
    ): Any {
        val result =
            when (operator) {
                "+" ->
                    when {
                        leftValue is String && rightValue is String -> leftValue + rightValue

                        leftValue is Number && rightValue is Number -> leftValue as BigDecimal + rightValue as BigDecimal

                        leftValue is Number && rightValue is String || leftValue is String && rightValue is Number ->
                            leftValue.toString() +
                                rightValue.toString()

                        else -> throw IllegalArgumentException("Invalid operands for '+': $leftValue, $rightValue at ($line:$start)")
                    }

                "-", "*", "/" ->
                    when {
                        leftValue is Number && rightValue is Number ->
                            when (operator) {
                                "-" -> (leftValue as BigDecimal) - (rightValue as BigDecimal)
                                "*" -> (leftValue as BigDecimal) * (rightValue as BigDecimal)
                                "/" -> (leftValue as BigDecimal) / (rightValue as BigDecimal)
                                else -> throw IllegalArgumentException("Invalid operator: $operator")
                            }

                        else -> throw IllegalArgumentException(
                            "Invalid operands for '$operator': $leftValue, $rightValue at ($line:$start)",
                        )
                    }

                else -> throw IllegalArgumentException("Invalid operator: $operator at ($line:$start)")
            }
        return result
    }
}

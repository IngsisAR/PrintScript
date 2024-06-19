package astbuilder

import utils.BinaryExpression
import utils.CallExpression
import utils.Expression
import utils.Identifier
import utils.NumberLiteral
import utils.Position
import utils.StringLiteral
import utils.Token
import java.util.Stack

class BinaryExpressionBuilder(
    val tokens: List<Token>,
    private val astProviderFactory: ASTProviderFactory,
) : ASTBuilder {
    override fun verifyAndBuild(): ASTBuilderResult {
        when {
            tokens.size < 3 -> {
                return ASTBuilderFailure("Binary expression must have at least 3 tokens")
            }

            else -> {
                return if (tokens.any {
                        it.type == "PLUS" ||
                            it.type == "MINUS" ||
                            it.type == "MUL" ||
                            it.type == "DIV" ||
                            it.type == "MODULE"
                    }
                ) {
                    performShuntingYardAndPostFix()
                } else {
                    ASTBuilderFailure("No operator found in binary expression")
                }
            }
        }
    }

    private fun performShuntingYardAndPostFix(): ASTBuilderResult {
        val operatorStack = Stack<Token>()
        val outputQueue = mutableListOf<Token>()
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]
            when (token.type) {
                "NUMBER", "STRING" -> {
                    outputQueue.add(token)
                    i++
                }

                "ID" -> {
                    if (i + 1 < tokens.size && tokens[i + 1].type == "OPAREN") {
                        // Buscamos el índice del próximo CPAREN.
                        val endIndex = tokens.subList(i, tokens.size).indexOfFirst { it.type == "CPAREN" } + i
                        if (endIndex == -1) {
                            return ASTBuilderFailure(
                                "Mismatched parenthesis in call expression at " +
                                    "(${tokens[i + 1].position.line}:${tokens[i + 1].position.end})",
                            )
                        }
                        // Creamos la sublista de tokens para la CallExpression.
                        val callExpressionTokens = tokens.subList(i, endIndex + 1)
                        val callExpressionResult =
                            CallExpressionBuilder(callExpressionTokens, astProviderFactory).verifyAndBuild()
                        if (callExpressionResult is ASTBuilderSuccess) {
                            outputQueue.add(
                                Token(
                                    "CALL_EXPRESSION",
                                    Position(tokens[i].position.line, i, endIndex),
                                    callExpressionResult.astNode.toString(),
                                ),
                            )
                            i = endIndex + 1 // Saltamos todos los tokens procesados por CallExpressionBuilder.
                        } else {
                            return callExpressionResult
                        }
                    } else {
                        outputQueue.add(token)
                        i++
                    }
                }

                in listOf("PLUS", "MINUS", "MUL", "DIV", "MODULE") -> {
                    while (operatorStack.isNotEmpty() &&
                        operatorStack.peek().type in listOf("PLUS", "MINUS", "MUL", "DIV", "MODULE") &&
                        precedence(token.type) <= precedence(operatorStack.peek().type)
                    ) {
                        outputQueue.add(operatorStack.pop())
                    }
                    operatorStack.push(token)
                    i++
                }

                "OPAREN" -> {
                    operatorStack.push(token)
                    i++
                }

                "CPAREN" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek().type != "OPAREN") {
                        outputQueue.add(operatorStack.pop())
                    }
                    if (operatorStack.isEmpty()) {
                        return ASTBuilderFailure("Mismatched parenthesis at (${token.position.line}:${token.position.start})")
                    }
                    operatorStack.pop() // Descarta el paréntesis abierto.
                    i++
                }

                else -> {
                    i++
                }
            }
        }

        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.pop())
        }

        return buildBinaryExpressionFromPostFix(outputQueue)
    }

    private fun precedence(operator: String): Int =
        when (operator) {
            "PLUS", "MINUS" -> 1
            "MUL", "DIV", "MODULE" -> 2
            else -> 0
        }

    private fun buildBinaryExpressionFromPostFix(postFixTokens: List<Token>): ASTBuilderResult {
        val stack = Stack<Expression>()

        for (token in postFixTokens) {
            when (token.type) {
                "NUMBER" -> {
                    val numberResult = NumberLiteralBuilder(listOf(token)).verifyAndBuild()
                    if (numberResult is ASTBuilderFailure) {
                        return numberResult
                    }
                    stack.push((numberResult as ASTBuilderSuccess).astNode as NumberLiteral)
                }

                "ID" -> {
                    val identifierResult = IdentifierBuilder(listOf(token)).verifyAndBuild()
                    if (identifierResult is ASTBuilderFailure) {
                        return identifierResult
                    }
                    stack.push((identifierResult as ASTBuilderSuccess).astNode as Identifier)
                }

                "STRING" -> {
                    val stringResult = StringLiteralBuilder(listOf(token)).verifyAndBuild()
                    if (stringResult is ASTBuilderFailure) {
                        return stringResult
                    }
                    stack.push((stringResult as ASTBuilderSuccess).astNode as StringLiteral)
                }

                "CALL_EXPRESSION" -> {
                    val callExpressionResult =
                        CallExpressionBuilder(
                            tokens.subList(token.position.start, token.position.end + 1),
                            astProviderFactory,
                        ).verifyAndBuild()
                    if (callExpressionResult is ASTBuilderFailure) {
                        return callExpressionResult
                    }
                    stack.push((callExpressionResult as ASTBuilderSuccess).astNode as CallExpression)
                }

                in listOf("PLUS", "MINUS", "MUL", "DIV", "MODULE") -> {
                    if (stack.size < 2) {
                        return ASTBuilderFailure("Invalid postfix expression at (${token.position.line}:${token.position.start})")
                    }
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(BinaryExpression(left, right, token.value, left.line, left.start, right.end))
                }
            }
        }

        if (stack.size != 1) {
            return ASTBuilderFailure(
                "Invalid postfix expression " +
                    "at (${tokens.first().position.line}:${tokens.first().position.start})",
            )
        }

        return ASTBuilderSuccess(stack.pop())
    }
}

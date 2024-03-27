package astbuilder

import BinaryExpression
import Expression
import Token

class BinaryExpressionBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    private lateinit var left: Expression
    private lateinit var right: Expression
    private lateinit var operator: String

    override fun verify(): ASTBuilderResult {
        when {
            tokens.size < 3 -> {
                return ASTBuilderFailure("Binary expression must have at least 3 tokens")
            }

            else -> {
                if (tokens.any {
                        it.type == "PLUS" ||
                            it.type == "MINUS" ||
                            it.type == "MUL" ||
                            it.type == "DIV" ||
                            it.type == "MODULE"
                    }
                ) {
                    if (tokens.any { it.type == "PLUS" || it.type == "MINUS" }) {
                        val plusMinusIndex = tokens.indexOfFirst { it.type == "PLUS" || it.type == "MINUS" }
                        return verifyAndBuildLeftAndRight(plusMinusIndex)
                    } else {
                        val mulDivModIndex = tokens.indexOfFirst { it.type == "MUL" || it.type == "DIV" || it.type == "MODULE" }
                        return verifyAndBuildLeftAndRight(mulDivModIndex)
                    }
                } else {
                    return ASTBuilderFailure("No operator found in binary expression")
                }
            }
        }
    }

    private fun verifyAndBuildLeftAndRight(operatorIndex: Int): ASTBuilderResult {
        val leftTokens = tokens.subList(0, operatorIndex)
        val rightTokens = tokens.subList(operatorIndex + 1, tokens.size)
        val leftExpressionResult =
            ExpressionProvider(leftTokens)
                .getVerifiedExpressionResult()
        if (leftExpressionResult is ASTBuilderFailure) {
            return ASTBuilderFailure("Left expression of binary expression is invalid: ${leftExpressionResult.errorMessage}")
        }
        left = (leftExpressionResult as ASTBuilderSuccess).astNode as Expression
        val rightExpressionResult =
            ExpressionProvider(rightTokens)
                .getVerifiedExpressionResult()
        if (rightExpressionResult is ASTBuilderFailure) {
            return ASTBuilderFailure("Right expression of binary expression is invalid: ${rightExpressionResult.errorMessage}")
        }
        right = (rightExpressionResult as ASTBuilderSuccess).astNode as Expression
        operator = tokens[operatorIndex].value
        return leftExpressionResult
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                BinaryExpression(
                    left = left,
                    right = right,
                    operator = operator,
                    start = tokens.first().position.start,
                    end = tokens.last().position.end,
                ),
            )
        } else {
            result
        }
    }
}

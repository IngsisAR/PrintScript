package astbuilder

import CallExpression
import Expression
import Identifier
import Token

class CallExpressionBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    private var arguments: List<Expression> = emptyList()
    private var identifierResult: ASTBuilderResult = ASTBuilderFailure("")

    override fun verify(): ASTBuilderResult {
        if (tokens.size < 2) {
            return ASTBuilderFailure("Not enough members for call expression")
        }

        identifierResult = IdentifierBuilder(tokens.subList(0, 1), lineIndex).verifyAndBuild()

        if (identifierResult is ASTBuilderFailure || identifierResult is ASTBuilderSuccess && tokens[1].type != "OPAREN") {
            return ASTBuilderFailure("Not enough members for call expression")
        }
        if (identifierResult is ASTBuilderSuccess && tokens[1].type == "OPAREN" && tokens.last().type != "CPAREN") {
            return ASTBuilderFailure("Call expression does not have close parenthesis at ($lineIndex, ${tokens.last().position.start})")
        }

        if (tokens.size > 3) {
            val commaCount = tokens.count { it.type == "COMMA" }
            if (commaCount == 0) {
                val expressionResult =
                    ExpressionProvider(tokens.subList(2, tokens.size - 1), lineIndex)
                        .getVerifiedExpressionResult()
                if (expressionResult is ASTBuilderFailure) {
                    if (expressionResult.errorMessage.isNotBlank() || expressionResult.errorMessage.isNotEmpty()) {
                        return ASTBuilderFailure("Call expression does not have valid argument: ${expressionResult.errorMessage}")
                    }
                    return ASTBuilderFailure("Call expression does not have valid argument")
                }
                arguments = listOf((expressionResult as ASTBuilderSuccess).astNode as Expression)
                return expressionResult
            } else {
                var tokensAux = tokens.subList(2, tokens.size - 1)
                for (i in 0 until commaCount) {
                    val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
                    val expressionResult =
                        AssignableExpressionProvider(tokensAux.subList(0, commaIndex), lineIndex)
                            .getAssignableExpressionResult()
                    if (expressionResult is ASTBuilderFailure) {
                        return ASTBuilderFailure("Call expression does not have valid argument: ${expressionResult.errorMessage}")
                    }
                    arguments += (expressionResult as ASTBuilderSuccess).astNode as Expression
                    tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
                }
                val expressionResult =
                    AssignableExpressionProvider(tokensAux, lineIndex)
                        .getAssignableExpressionResult()
                if (expressionResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Call expression does not have valid argument: ${expressionResult.errorMessage}")
                }
                arguments += (expressionResult as ASTBuilderSuccess).astNode as Expression
            }
            return identifierResult
        } else {
            return identifierResult
        }
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                CallExpression(
                    callee = (identifierResult as ASTBuilderSuccess).astNode as Identifier,
                    arguments = arguments,
                    start = tokens.first().position.start,
                    end = tokens.last().position.end,
                ),
            )
        } else {
            result
        }
    }
}

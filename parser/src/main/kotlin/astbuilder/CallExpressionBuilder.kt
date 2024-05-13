package astbuilder

import CallExpression
import Expression
import Identifier
import Token

class CallExpressionBuilder(
    val tokens: List<Token>,
    private val astProviderFactory: ASTProviderFactory,
) : ASTBuilder {
    private var arguments: List<Expression> = emptyList()
    private var identifierResult: ASTBuilderResult = ASTBuilderFailure("")

    override fun verifyAndBuild(): ASTBuilderResult {
        if (tokens.size < 2) {
            return ASTBuilderFailure("Not enough members for call expression")
        }

        identifierResult = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()

        if (identifierResult is ASTBuilderFailure || identifierResult is ASTBuilderSuccess && tokens[1].type != "OPAREN") {
            return ASTBuilderFailure("Not enough members for call expression")
        }
        if (identifierResult is ASTBuilderSuccess && tokens[1].type == "OPAREN" && tokens.last().type != "CPAREN") {
            return ASTBuilderFailure("Call expression does not have close parenthesis at (${tokens.last().position.line}, ${tokens.last().position.end})")
        }

        if (tokens.size > 3) {
            val commaCount = tokens.count { it.type == "COMMA" }
            if (commaCount == 0) {
                val expressionResult =
                    astProviderFactory.changeTokens(tokens.subList(2, tokens.size - 1))
                        .getProviderByType("assignableExpression").getASTBuilderResult()
                if (expressionResult is ASTBuilderFailure) {
                    if (expressionResult.errorMessage.isNotBlank() || expressionResult.errorMessage.isNotEmpty()) {
                        return ASTBuilderFailure("Call expression does not have valid argument: ${expressionResult.errorMessage}")
                    }
                    return ASTBuilderFailure("Call expression does not have valid argument")
                }
                arguments = listOf((expressionResult as ASTBuilderSuccess).astNode as Expression)
                return ASTBuilderSuccess(
                    CallExpression(
                        callee = (identifierResult as ASTBuilderSuccess).astNode as Identifier,
                        arguments = arguments,
                        line = tokens.first().position.line,
                        start = tokens.first().position.start,
                        end = tokens.last().position.end,
                    ),
                )
            } else {
                var tokensAux = tokens.subList(2, tokens.size - 1)
                for (i in 0 until commaCount) {
                    val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
                    val expressionResult =
                        astProviderFactory.changeTokens(tokensAux.subList(0, commaIndex))
                            .getProviderByType("assignableExpression").getASTBuilderResult()
                    if (expressionResult is ASTBuilderFailure) {
                        return ASTBuilderFailure("Call expression does not have valid argument: ${expressionResult.errorMessage}")
                    }
                    arguments += (expressionResult as ASTBuilderSuccess).astNode as Expression
                    tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
                }
                val expressionResult =
                    astProviderFactory.changeTokens(tokensAux)
                        .getProviderByType("assignableExpression").getASTBuilderResult()
                if (expressionResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Call expression does not have valid argument: ${expressionResult.errorMessage}")
                }
                arguments += (expressionResult as ASTBuilderSuccess).astNode as Expression
            }
            return ASTBuilderSuccess(
                CallExpression(
                    callee = (identifierResult as ASTBuilderSuccess).astNode as Identifier,
                    arguments = arguments,
                    line = tokens.first().position.line,
                    start = tokens.first().position.start,
                    end = tokens.last().position.end,
                ),
            )
        } else {
            return ASTBuilderFailure("Call expression does not have valid argument")
        }
    }
}

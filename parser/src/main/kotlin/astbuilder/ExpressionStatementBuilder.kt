package astbuilder

import Expression
import ExpressionStatement
import Token

class ExpressionStatementBuilder(
    val tokens: List<Token>,
    private val astProviderFactory: ASTProviderFactory,
) : ASTBuilder {
    override fun verifyAndBuild(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Empty tokens")
        }
        if (tokens.last().type != "SEMICOLON") {
            return ASTBuilderFailure("Missing semicolon at (${tokens.last().position.line}:${tokens.last().position.end})")
        }
        if (tokens.size == 1) {
            return ASTBuilderFailure("No expression found at (${tokens.first().position.line}:${tokens.first().position.start})")
        }
        val expressionResult =
            astProviderFactory.changeTokens(tokens.subList(0, tokens.size - 1))
                .getProviderByType("expression").getASTBuilderResult()
        return if (expressionResult is ASTBuilderFailure) {
            if (expressionResult.errorMessage.isNotEmpty()) {
                return ASTBuilderFailure("Invalid expression: ${expressionResult.errorMessage}")
            }
            ASTBuilderFailure("Invalid expression at (${tokens.first().position.line}:${tokens.first().position.start})")
        } else {
            ASTBuilderSuccess(
                ExpressionStatement(
                    (expressionResult as ASTBuilderSuccess).astNode as Expression,
                    tokens.first().position.line,
                    tokens.first().position.start,
                    tokens.last().position.end,
                ),
            )
        }
    }
}

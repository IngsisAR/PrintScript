package astbuilder

import Expression
import ExpressionStatement
import Token

class ExpressionStatementBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    override fun verify(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Empty tokens")
        }
        if (tokens.last().type != "SEMICOLON") {
            return ASTBuilderFailure("Missing semicolon at ($lineIndex, ${tokens.last().position.start}")
        }
        if (tokens.size == 1) {
            return ASTBuilderFailure("No expression found at ($lineIndex, ${tokens.first().position.start})")
        }
        val expressionResult = ExpressionProvider(tokens.subList(0, tokens.size - 1), lineIndex).getVerifiedExpressionResult()
        return if (expressionResult is ASTBuilderFailure) {
            if (expressionResult.errorMessage.isNotEmpty()) {
                return ASTBuilderFailure("Invalid expression: ${expressionResult.errorMessage}")
            }
            ASTBuilderFailure("Invalid expression at ($lineIndex, ${tokens.first().position.start})")
        } else {
            expressionResult
        }
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                ExpressionStatement(
                    result.astNode as Expression,
                    tokens.first().position.start,
                    tokens.last().position.end,
                ),
            )
        } else {
            result
        }
    }
}

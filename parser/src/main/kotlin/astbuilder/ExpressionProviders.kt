package astbuilder

import Expression
import Statement
import Token

class AssignableExpressionProvider(
    tokens: List<Token>,
) {
    private val assignableExpressionBuilders: List<AbstractASTBuilder> =
        listOf(
            CallExpressionBuilder(tokens),
            BinaryExpressionBuilder(tokens),
            NumberLiteralBuilder(tokens),
            StringLiteralBuilder(tokens),
            IdentifierBuilder(tokens),
        )

    fun getAssignableExpressionResult(): ASTBuilderResult {
        for (expressionBuilder in assignableExpressionBuilders) {
            val astBuilderResult = expressionBuilder.verifyAndBuild()
            if (astBuilderResult is ASTBuilderSuccess && astBuilderResult.astNode is Expression) {
                return astBuilderResult
            }
        }
        return ASTBuilderFailure("No valid assignable expression found")
    }
}

class ExpressionProvider(
    tokens: List<Token>,
) {
    private val expressionBuilders: List<AbstractASTBuilder> =
        listOf(
            AssignmentExpressionBuilder(tokens),
            CallExpressionBuilder(tokens),
            BinaryExpressionBuilder(tokens),
            NumberLiteralBuilder(tokens),
            StringLiteralBuilder(tokens),
            IdentifierBuilder(tokens),
        )

    fun getVerifiedExpressionResult(): ASTBuilderResult {
        var errorMessages = ""
        for (expressionBuilder in expressionBuilders) {
            val astBuilderResult = expressionBuilder.verifyAndBuild()
            if (astBuilderResult is ASTBuilderSuccess && astBuilderResult.astNode is Expression) {
                return astBuilderResult
            }
            if (astBuilderResult is ASTBuilderFailure) {
                errorMessages += astBuilderResult.errorMessage + "\n"
            }
        }
        return ASTBuilderFailure("No valid expression found: \n$errorMessages")
    }
}

class StatementProvider(
    tokens: List<Token>,
) {
    private val statementBuilders: List<AbstractASTBuilder> =
        listOf(
            VariableDeclarationBuilder(tokens),
            ExpressionStatementBuilder(tokens),
        )

    fun getVerifiedStatementResult(): ASTBuilderResult {
        var errorMessages = ""
        for (statementBuilder in statementBuilders) {
            val astBuilderResult = statementBuilder.verifyAndBuild()
            if (astBuilderResult is ASTBuilderSuccess && astBuilderResult.astNode is Statement) {
                return astBuilderResult
            }
            if (astBuilderResult is ASTBuilderFailure) {
                errorMessages += astBuilderResult.errorMessage + "\n"
            }
        }
        return ASTBuilderFailure("No valid statement found, errors: \n$errorMessages")
    }
}

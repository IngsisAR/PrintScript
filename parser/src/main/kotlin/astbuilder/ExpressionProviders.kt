package astbuilder

import Expression
import Statement
import Token

val REDUNDANT_ERRORS =
    listOf(
        "No operator found in binary expression",
        "Not enough tokens to build assignment expression",
        "Invalid assignment expression",
        "Not enough members for call expression",
        "Binary expression must have at least 3 tokens",
    )

fun getASTBuilderResult(builders: List<ASTBuilder>): ASTBuilderResult {
    for (builder in builders) {
        val astBuilderResult = builder.verifyAndBuild()
        if (astBuilderResult is ASTBuilderSuccess && astBuilderResult.astNode is Expression) {
            return astBuilderResult
        }
        if (astBuilderResult is ASTBuilderFailure &&
            builder !is LiteralBuilder &&
            builder !is IdentifierBuilder &&
            !REDUNDANT_ERRORS.contains(astBuilderResult.errorMessage)
        ) {
            return ASTBuilderFailure(astBuilderResult.errorMessage)
        }
    }
    return ASTBuilderFailure("")
}

class AssignableExpressionProvider(
    tokens: List<Token>,
    lineIndex: Int,
) {
    private val assignableExpressionBuilders: List<ASTBuilder> =
        listOf(
            BinaryExpressionBuilder(tokens, lineIndex),
            CallExpressionBuilder(tokens, lineIndex),
            NumberLiteralBuilder(tokens, lineIndex),
            StringLiteralBuilder(tokens, lineIndex),
            IdentifierBuilder(tokens, lineIndex),
            BooleanLiteralBuilder(tokens, lineIndex),
        )

    fun getAssignableExpressionResult(): ASTBuilderResult {
        return getASTBuilderResult(assignableExpressionBuilders)
    }
}

class ExpressionProvider(
    tokens: List<Token>,
    lineIndex: Int,
) {
    private val expressionBuilders: List<ASTBuilder> =
        listOf(
            AssignmentExpressionBuilder(tokens, lineIndex),
            BinaryExpressionBuilder(tokens, lineIndex),
            CallExpressionBuilder(tokens, lineIndex),
            NumberLiteralBuilder(tokens, lineIndex),
            StringLiteralBuilder(tokens, lineIndex),
            IdentifierBuilder(tokens, lineIndex),
            BooleanLiteralBuilder(tokens, lineIndex),
        )

    fun getVerifiedExpressionResult(): ASTBuilderResult {
        return getASTBuilderResult(expressionBuilders)
    }
}

class StatementProvider(
    val tokens: List<Token>,
    val lineIndex: Int,
) {
    private val statementBuilders: List<ASTBuilder> =
        listOf(
            ConditionalStatementBuilder(tokens, lineIndex),
            VariableDeclarationBuilder(tokens, lineIndex),
            ExpressionStatementBuilder(tokens, lineIndex),
        )

    fun getVerifiedStatementResult(): ASTBuilderResult {
        var errorMessages = ""
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Empty tokens")
        }
        for (statementBuilder in statementBuilders) {
            val astBuilderResult = statementBuilder.verifyAndBuild()
            if (astBuilderResult is ASTBuilderSuccess && astBuilderResult.astNode is Statement) {
                return astBuilderResult
            }
            if (astBuilderResult is ASTBuilderFailure) {
                if (astBuilderResult.errorMessage != "Invalid variable declaration" && statementBuilder is VariableDeclarationBuilder) {
                    errorMessages = astBuilderResult.errorMessage
                    break
                } else if (statementBuilder is VariableDeclarationBuilder) {
                    continue
                }
                if (astBuilderResult.errorMessage != "Invalid conditional expression" && statementBuilder is ConditionalStatementBuilder) {
                    errorMessages = astBuilderResult.errorMessage
                    break
                } else if (statementBuilder is ConditionalStatementBuilder) {
                    continue
                }
                errorMessages += astBuilderResult.errorMessage
            }
        }
        return ASTBuilderFailure(errorMessages)
    }
}

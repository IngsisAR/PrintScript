package astbuilder

import Expression
import Statement
import Token

interface ASTProvider {
    fun getASTBuilderResult(): ASTBuilderResult

    fun changeTokens(tokens: List<Token>): ASTProvider
}

abstract class AbstractProvider : ASTProvider {
    fun String.isSameOrOlderThanCurrentVersion(factory: ASTProviderFactory): Boolean {
        val targetVersion = split(".").map { it.toInt() }
        val currentVersion = factory.version.split(".").map { it.toInt() }
        val currentVersionIsNewer = currentVersion.zip(targetVersion).all { (current, target) -> current >= target }
        return currentVersionIsNewer
    }
}

abstract class AbstractExpressionProvider(
    val tokens: List<Token>,
    val lineIndex: Int,
) : AbstractProvider() {
    private val redundantErrors =
        listOf(
            "No operator found in binary expression",
            "Not enough tokens to build assignment expression",
            "Invalid assignment expression",
            "Not enough members for call expression",
            "Binary expression must have at least 3 tokens",
        )

    protected fun getASTBuilderResult(builders: List<ASTBuilder>): ASTBuilderResult {
        for (builder in builders) {
            val astBuilderResult = builder.verifyAndBuild()
            if (astBuilderResult is ASTBuilderSuccess && astBuilderResult.astNode is Expression) {
                return astBuilderResult
            }
            if (astBuilderResult is ASTBuilderFailure &&
                builder !is LiteralBuilder &&
                builder !is IdentifierBuilder &&
                !redundantErrors.contains(astBuilderResult.errorMessage)
            ) {
                return ASTBuilderFailure(astBuilderResult.errorMessage)
            }
        }
        return ASTBuilderFailure("")
    }
}

class AssignableExpressionProvider(
    tokens: List<Token>,
    lineIndex: Int,
    private val factory: ASTProviderFactory,
) : AbstractExpressionProvider(tokens, lineIndex) {
    private val assignableExpressionBuilders: List<ASTBuilder> =
        listOf(
            BinaryExpressionBuilder(tokens, lineIndex, factory),
            CallExpressionBuilder(tokens, lineIndex, factory),
            NumberLiteralBuilder(tokens, lineIndex),
            StringLiteralBuilder(tokens, lineIndex),
            IdentifierBuilder(tokens, lineIndex),
            BooleanLiteralBuilder(tokens, lineIndex),
        )

    override fun getASTBuilderResult(): ASTBuilderResult {
        if (!"1.1.0".isSameOrOlderThanCurrentVersion(factory)) {
            val modifiedBuilderList = assignableExpressionBuilders.toMutableList()
            modifiedBuilderList.removeIf { it is BooleanLiteralBuilder }
            return getASTBuilderResult(modifiedBuilderList)
        }
        return getASTBuilderResult(assignableExpressionBuilders)
    }

    override fun changeTokens(tokens: List<Token>): ASTProvider {
        return AssignableExpressionProvider(tokens, lineIndex, factory)
    }
}

class ExpressionProvider(
    tokens: List<Token>,
    lineIndex: Int,
    private val factory: ASTProviderFactory,
) : AbstractExpressionProvider(tokens, lineIndex) {
    private val expressionBuilders: List<ASTBuilder> =
        listOf(
            AssignmentExpressionBuilder(tokens, lineIndex, factory),
            BinaryExpressionBuilder(tokens, lineIndex, factory),
            CallExpressionBuilder(tokens, lineIndex, factory),
            NumberLiteralBuilder(tokens, lineIndex),
            StringLiteralBuilder(tokens, lineIndex),
            IdentifierBuilder(tokens, lineIndex),
            BooleanLiteralBuilder(tokens, lineIndex),
        )

    override fun getASTBuilderResult(): ASTBuilderResult {
        if (!"1.1.0".isSameOrOlderThanCurrentVersion(factory)) {
            val modifiedBuilderList = expressionBuilders.toMutableList()
            modifiedBuilderList.removeIf { it is BooleanLiteralBuilder }
            return getASTBuilderResult(modifiedBuilderList)
        }
        return getASTBuilderResult(expressionBuilders)
    }

    override fun changeTokens(tokens: List<Token>): ASTProvider {
        return ExpressionProvider(tokens, lineIndex, factory)
    }
}

class StatementProvider(
    val tokens: List<Token>,
    val lineIndex: Int,
    private val factory: ASTProviderFactory,
) : AbstractProvider() {
    private val statementBuilders: List<ASTBuilder> =
        listOf(
            ConditionalStatementBuilder(tokens, lineIndex, factory),
            VariableDeclarationBuilder(tokens, lineIndex, factory),
            ExpressionStatementBuilder(tokens, lineIndex, factory),
        )

    override fun getASTBuilderResult(): ASTBuilderResult {
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
                if ("1.1.0".isSameOrOlderThanCurrentVersion(factory) && astBuilderResult.errorMessage != "Invalid conditional expression" &&
                    statementBuilder is ConditionalStatementBuilder
                ) {
                    errorMessages = astBuilderResult.errorMessage
                    break
                } else if (statementBuilder is ConditionalStatementBuilder) {
                    continue
                }
                if (astBuilderResult.errorMessage != "Invalid variable declaration" && statementBuilder is VariableDeclarationBuilder) {
                    errorMessages = astBuilderResult.errorMessage
                    break
                } else if (statementBuilder is VariableDeclarationBuilder) {
                    continue
                }
                errorMessages += astBuilderResult.errorMessage
            }
        }
        return ASTBuilderFailure(errorMessages)
    }

    override fun changeTokens(tokens: List<Token>): ASTProvider {
        return StatementProvider(tokens, lineIndex, factory)
    }
}

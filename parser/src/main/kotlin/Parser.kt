import astbuilder.ASTBuilderResult
import astbuilder.StatementProvider

class Parser {
    @Throws(IllegalArgumentException::class)
    fun parse(
        tokens: List<Token>,
        lineIndex: Int,
    ): ASTBuilderResult {
        val statementProvider = StatementProvider(tokens, lineIndex)
        return statementProvider.getVerifiedStatementResult()
    }
}

import astbuilder.ASTBuilderResult
import astbuilder.StatementProvider

class Parser {
    fun parse(tokens: List<Token>): ASTBuilderResult {
        val statementProvider = StatementProvider(tokens)
        return statementProvider.getVerifiedStatementResult()
    }
}

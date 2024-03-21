import astbuilder.StatementProvider


class Parser {
    fun parse(tokens : List<Token>): ASTNode?{
        val statementProvider = StatementProvider(tokens)
        return statementProvider.getVerifiedStatementOrNull()
    }
}
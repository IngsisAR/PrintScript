import astbuilder.ASTBuilderResult
import astbuilder.ASTProviderFactory
import astbuilder.StatementProvider

class Parser {
    @Throws(IllegalArgumentException::class)
    fun parse(astProviderFactory: ASTProviderFactory): ASTBuilderResult {
        val statementProviderImpl = astProviderFactory.getProviderByType("statement") as StatementProvider
        return statementProviderImpl.getASTBuilderResult()
    }
}

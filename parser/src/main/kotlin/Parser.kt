import astbuilder.ASTBuilderResult
import astbuilder.ASTProviderFactory
import astbuilder.StatementProvider

class Parser {
    @Throws(IllegalArgumentException::class)
    fun parse(ASTProviderFactory: ASTProviderFactory): ASTBuilderResult {
        val statementProviderImpl = ASTProviderFactory.getProviderByType("statement") as StatementProvider
        return statementProviderImpl.getASTBuilderResult()
    }
}

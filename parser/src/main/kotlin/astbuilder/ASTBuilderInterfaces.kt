package astbuilder

sealed interface ASTBuilder {
    fun verifyAndBuild(): ASTBuilderResult
}

interface LiteralBuilder : ASTBuilder

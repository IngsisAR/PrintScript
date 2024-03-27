package astbuilder

sealed interface ASTBuilder {
    fun verifyAndBuild(): ASTBuilderResult
}

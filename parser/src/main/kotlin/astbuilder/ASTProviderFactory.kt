package astbuilder

import Token

class ASTProviderFactory(val tokens: List<Token>, val lineIndex: Int, val version: String) {
    private var providerByType: Map<String, ASTProvider> =
        mapOf(
            "statement" to StatementProvider(tokens, lineIndex, this),
            "expression" to ExpressionProvider(tokens, lineIndex, this),
            "assignableExpression" to AssignableExpressionProvider(tokens, lineIndex, this),
        )

    fun getProviderByType(type: String): ASTProvider {
        return providerByType[type] ?: throw IllegalArgumentException("No provider found for type $type")
    }

    fun putProvider(
        type: String,
        provider: ASTProvider,
    ): ASTProviderFactory {
        val newProviderByType = providerByType.toMutableMap()
        newProviderByType[type] = provider
        val newASTProviderFactory = ASTProviderFactory(tokens, lineIndex, version)
        newASTProviderFactory.providerByType = newProviderByType
        return newASTProviderFactory
    }

    fun changeTokens(tokens: List<Token>): ASTProviderFactory {
        val newASTProviderFactory = ASTProviderFactory(tokens, lineIndex, version)
        // Maintain the same providers but with new tokens
        newASTProviderFactory.providerByType = providerByType.mapValues { (_, provider) -> provider.changeTokens(tokens) }
        return newASTProviderFactory
    }

    fun getKeys(): Set<String> {
        return providerByType.keys
    }

    fun getValues(): Collection<ASTProvider> {
        return providerByType.values
    }

    fun getEntries(): Set<Map.Entry<String, ASTProvider>> {
        return providerByType.entries
    }
}
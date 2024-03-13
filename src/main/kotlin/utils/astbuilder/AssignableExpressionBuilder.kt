package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Token

class AssignableExpressionBuilderProvider(tokens:List<Token>) {
    private val expressionBuilders: List<AbstractASTBuilder> = listOf(
        CallExpressionBuilder(tokens),
        BinaryExpressionBuilder(tokens)
    )

    fun getAssignableExpressionBuilder(): AbstractASTBuilder? {
        return expressionBuilders.firstOrNull { it.verify() }
    }
}
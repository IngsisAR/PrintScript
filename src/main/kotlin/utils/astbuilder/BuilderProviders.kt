package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Token

class AssignableExpressionBuilderProvider(tokens:List<Token>) {
    private val assignableExpressionBuilders: List<AbstractASTBuilder> = listOf(
        CallExpressionBuilder(tokens),
        BinaryExpressionBuilder(tokens),
        NumberLiteralBuilder(tokens),
        StringLiteralBuilder(tokens),
        IdentifierBuilder(tokens)
    )

    fun getAssignableExpressionBuilder(): AbstractASTBuilder? {
        return assignableExpressionBuilders.firstOrNull { it.build()!=null }
    }
}

class ExpressionBuilderProvider(tokens:List<Token>) {
    private val expressionBuilders: List<AbstractASTBuilder> = listOf(
        AssigmentExpressionBuilder(tokens),
        CallExpressionBuilder(tokens),
        BinaryExpressionBuilder(tokens),
        NumberLiteralBuilder(tokens),
        StringLiteralBuilder(tokens),
        IdentifierBuilder(tokens)
    )

    fun getExpressionBuilder(): AbstractASTBuilder? {
        return expressionBuilders.firstOrNull { it.build()!=null }
    }
}
package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Expression
import australfi.ingsis7.utils.Statement
import australfi.ingsis7.utils.Token

class AssignableExpressionProvider(tokens:List<Token>) {
    private val assignableExpressionBuilders: List<AbstractASTBuilder> = listOf(
        CallExpressionBuilder(tokens),
        BinaryExpressionBuilder(tokens),
        NumberLiteralBuilder(tokens),
        StringLiteralBuilder(tokens),
        IdentifierBuilder(tokens)
    )

    fun getAssignableExpressionOrNull(): Expression? {
        for (expressionBuilder in assignableExpressionBuilders) {
            val expression = expressionBuilder.verifyAndBuild()
            if (expression != null) {
                return expression as Expression
            }
        }
        return null
    }
}

class ExpressionProvider(tokens:List<Token>) {
    private val expressionBuilders: List<AbstractASTBuilder> = listOf(
        AssigmentExpressionBuilder(tokens),
        CallExpressionBuilder(tokens),
        BinaryExpressionBuilder(tokens),
        NumberLiteralBuilder(tokens),
        StringLiteralBuilder(tokens),
        IdentifierBuilder(tokens)
    )

    fun getVerifiedExpressionOrNull(): Expression? {
        for (expressionBuilder in expressionBuilders) {
            val expression = expressionBuilder.verifyAndBuild()
            if (expression != null) {
                return expression as Expression
            }
        }
        return null
    }
}

class StatementProvider(tokens:List<Token>) {
    private val statementBuilders: List<AbstractASTBuilder> = listOf(
        ExpressionStatementBuilder(tokens),
        VariableDeclarationBuilder(tokens)
    )

    fun getVerifiedStatementOrNull(): Statement? {
        for (statementBuilder in statementBuilders) {
            val statement = statementBuilder.verifyAndBuild()
            if (statement != null) {
                return statement as Statement
            }
        }
        return null
    }
}
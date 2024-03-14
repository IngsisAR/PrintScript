package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Expression
import australfi.ingsis7.utils.ExpressionStatement
import australfi.ingsis7.utils.Token

class ExpressionStatementBuilder(tokens:List<Token>): AbstractASTBuilder(tokens) {
    private var expression: Expression? = null
    override fun verify(): Boolean {
        if (tokens.isEmpty()) {
            println("No tokens provided")
            return false
        }
        if (tokens.last().type != "SEMICOLON") {
            println("Last token is not a semicolon")
            return false
        }
        expression = ExpressionProvider(tokens.subList(0,tokens.size-1)).getVerifiedExpressionOrNull()
        return if (expression != null){
            println("Expression Statement verified")
            true
        } else {
            println("Expression is not valid")
            false
        }
    }

    override fun verifyAndBuild(): ExpressionStatement? {
        return if (verify()) ExpressionStatement(
            expression!!,
            tokens.first().position.start,
            tokens.last().position.end
        ) else null
    }
}
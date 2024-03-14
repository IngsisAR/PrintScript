package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.CallExpression
import australfi.ingsis7.utils.Expression
import australfi.ingsis7.utils.Token

class CallExpressionBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    private lateinit var arguments:List<Expression>
    override fun verify(): Boolean {
        when{
            tokens.size < 3 -> {
                println("Not enough members for call expression")
                return false
            }
            IdentifierBuilder(tokens.subList(0,1)).build()==null -> {
                println("Call expression does not have identifier")
                return false
            }
            tokens[1].type != "LPAREN" -> {
                println("Call expression does not have left parenthesis")
                return false
            }
            tokens.last().type != "RPAREN" -> {
                println("Call expression does not have right parenthesis")
                return false
            }
            tokens.size > 3 -> {
                val commaCount = tokens.count { it.type == "COMMA" }
                if (commaCount == 0) {
                    val expressionBuilder = ExpressionBuilderProvider(tokens.subList(2, tokens.size - 1))
                        .getExpressionBuilder()?: return false
                    arguments+= expressionBuilder.build() as Expression
                    return true
                }
                var tokensAux = tokens.subList(2, tokens.size - 1)
                for (i in 0 until commaCount) {
                    val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
                    val expressionBuilder = AssignableExpressionBuilderProvider(tokens.subList(0, commaIndex))
                        .getAssignableExpressionBuilder() ?: return false
                    arguments+= expressionBuilder.build() as Expression
                    tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
                }
                return true
            }
            else -> {
                println("Call expression is valid")
                return true
            }
        }
    }

    override fun build(): CallExpression {
        return CallExpression(
            callee = IdentifierBuilder(tokens.subList(0, 1)).build()!!,
            arguments = arguments,
            start = tokens.first().position.start,
            end = tokens.last().position.end
        )
    }
}
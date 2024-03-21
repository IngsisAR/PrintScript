package astbuilder

import CallExpression
import Expression
import Token

class CallExpressionBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    private var arguments:List<Expression> = emptyList()
    override fun verify(): Boolean {
        when{
            tokens.size < 3 -> {
                println("Not enough members for call expression")
                return false
            }
            IdentifierBuilder(tokens.subList(0,1)).verifyAndBuild()==null -> {
                println("Call expression does not have identifier")
                return false
            }
            tokens[1].type != "OPAREN" -> {
                println("Call expression does not have left parenthesis")
                return false
            }
            tokens.last().type != "CPAREN" -> {
                println("Call expression does not have right parenthesis")
                return false
            }
            tokens.size > 3 -> {
                val commaCount = tokens.count { it.type == "COMMA" }
                if (commaCount == 0) {
                    val expression = ExpressionProvider(tokens.subList(2, tokens.size - 1))
                        .getVerifiedExpressionOrNull()?: return false
                    arguments= listOf(expression)
                    return true
                }else{
                var tokensAux = tokens.subList(2, tokens.size - 1)
                for (i in 0 until commaCount) {
                    val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
                    val expression = AssignableExpressionProvider(tokens.subList(0, commaIndex))
                        .getAssignableExpressionOrNull() ?: return false
                    arguments+= expression
                    tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
                }
                    }
                return true
            }
            else -> {
                println("Call expression is valid")
                return true
            }
        }
    }

    override fun verifyAndBuild(): CallExpression? {
        return if(verify()) CallExpression(
            callee = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()!!,
            arguments = arguments,
            start = tokens.first().position.start,
            end = tokens.last().position.end
        )else null
    }
}
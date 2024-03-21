package astbuilder

import BinaryExpression
import Expression
import Token

class BinaryExpressionBuilder(tokens:List<Token>): AbstractASTBuilder(tokens){
    private lateinit var left: Expression
    private lateinit var right: Expression
    private lateinit var operator: String
    override fun verify(): Boolean {
        when{
            tokens.size < 3 -> {
                println("Not enough tokens to build BinaryExpression")
                return false
            }
            else -> {
                if (tokens.any { it.type == "PLUS" || it.type == "MINUS" ||
                        it.type == "MUL" || it.type == "DIV" || it.type == "MODULE" }) {
                    if (tokens.any { it.type=="PLUS" || it.type=="MINUS" }){
                        val plusMinusIndex = tokens.indexOfFirst { it.type=="PLUS" || it.type=="MINUS" }
                        return verifyAndBuildLeftAndRight(plusMinusIndex)
                    }
                    else{
                        val mulDivModIndex = tokens.indexOfFirst { it.type=="MUL" || it.type=="DIV" || it.type=="MODULE" }
                        return verifyAndBuildLeftAndRight(mulDivModIndex)
                    }
                }
                else{
                    println("No operator found")
                    return false
                }
            }
        }
    }

    private fun verifyAndBuildLeftAndRight(operatorIndex: Int): Boolean {
        val leftTokens = tokens.subList(0, operatorIndex)
        val rightTokens = tokens.subList(operatorIndex + 1, tokens.size)
        val expression = ExpressionProvider(leftTokens)
            .getVerifiedExpressionOrNull() ?: return false
        left = expression
        val expression2 = ExpressionProvider(rightTokens)
            .getVerifiedExpressionOrNull() ?: return false
        right = expression2
        operator = tokens[operatorIndex].value!!
        return true
    }

    override fun verifyAndBuild(): BinaryExpression? {
        return if (verify()) BinaryExpression(
            left = left,
            right = right,
            operator = operator,
            start = tokens.first().position.start,
            end = tokens.last().position.end
        ) else null
    }
}
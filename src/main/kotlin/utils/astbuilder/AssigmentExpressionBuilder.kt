package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.AssigmentExpression
import australfi.ingsis7.utils.Expression
import australfi.ingsis7.utils.Token

class AssigmentExpressionBuilder(tokens:List<Token>): AbstractASTBuilder(tokens) {
    private var assignableExpression: Expression? = null
    override fun verify(): Boolean {
        return when{
            tokens.size < 3 -> {
                println("Not enough tokens to build AssigmentExpression")
                false
            }
            IdentifierBuilder(tokens.subList(0,1)).verifyAndBuild()!=null && tokens[1].type == "ASSIGN" -> {
                assignableExpression= AssignableExpressionProvider(tokens.subList(2, tokens.size))
                    .getAssignableExpressionOrNull()
                if (assignableExpression != null) {
                    println("Assigment Expression verified")
                    true
                } else {
                    println("Assignable expression is not valid")
                    false
                }
            }
            else -> false
        }
    }

    override fun verifyAndBuild(): ASTNode? {
        return if (verify()) AssigmentExpression(
            IdentifierBuilder(tokens.subList(0,1)).verifyAndBuild()!!,
            assignableExpression!!,
            tokens.first().position.start,
            tokens.last().position.end
        ) else null
    }
}
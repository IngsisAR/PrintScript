package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Expression
import australfi.ingsis7.utils.Token
import australfi.ingsis7.utils.VariableDeclarator

class VariableDeclaratorBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    private var assignableExpression: Expression? = null
    override fun verify(): Boolean {
        return when {
            tokens.size < 3-> {
                println("Not enough members for declaration")
                false
            }
            IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()==null -> {
                println("No identifier declared for variable declarator")
                false
            }
            tokens[1].type != "COLON" || tokens[1].type == "COLON" &&
                    TypeReferenceBuilder(tokens.subList(2,3)).verifyAndBuild()==null -> {
                println("No type declared")
                false
            }
            tokens.size>4-> {
                if (tokens[3].type == "ASSIGN") {
                    val assignableExpressionProvider =
                        AssignableExpressionProvider(tokens.subList(4, tokens.size))
                    assignableExpression = assignableExpressionProvider.getAssignableExpressionOrNull()
                    if (assignableExpression != null) {
                        println("Variable declarator is valid")
                        true
                    } else {
                        println("Invalid assignable expression")
                        false
                    }
                }else {
                    println("Variable declarator needs a valid assignation")
                    false
                }
            }
            else -> {
                println("Variable declarator is valid")
                true
            }
        }
    }

    override fun verifyAndBuild(): VariableDeclarator? {
        return if (verify()) VariableDeclarator(
            id = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()!!,
            type = TypeReferenceBuilder(tokens.subList(2, 3)).verifyAndBuild(),
            init = assignableExpression,
            start = tokens.first().position.start,
            end = tokens.last().position.end
        )else null
    }
}
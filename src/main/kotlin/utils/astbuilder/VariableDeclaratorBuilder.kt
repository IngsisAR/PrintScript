package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.*

class VariableDeclaratorBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    private val assignableExpressionBuilderProvider = AssignableExpressionBuilderProvider(tokens.subList(4, tokens.size))
    private var assignableExpressionBuilder: AbstractASTBuilder? = null
    override fun verify(): Boolean {
        return when {
            tokens.size < 3-> {
                println("Not enough members for declaration")
                false
            }
            tokens[0].type != "ID" -> {
                println("Variable declaration does not have identifier")
                false
            }
            tokens[1].type != "COLON" || tokens[1].type == "COLON" && tokens[2].type != "TYPE" -> {
                println("No type declared")
                false
            }
            tokens.size>3 && tokens[3].type == "ASSIGN"-> {
                assignableExpressionBuilder = assignableExpressionBuilderProvider.getAssignableExpressionBuilder()
                if (assignableExpressionBuilder!=null) {
                    println("Variable declarator is valid")
                    true
                } else {
                    println("Invalid expression")
                    false
                }

            }
            else -> {
                println("Variable declarator is invalid")
                false
            }
        }
    }

    override fun build(): VariableDeclarator {
        return VariableDeclarator(
            id = Identifier(tokens[1].value!!, tokens[1].position.start, tokens[1].position.end),
            type = tokens[3].value,
            init = assignableExpressionBuilder!!.build() as Expression,
            start = tokens.first().position.start,
            end = tokens.last().position.end
        )
    }
}
package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.*

class VariableDeclaratorBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    private lateinit var assignableExpressionBuilderProvider: AssignableExpressionBuilderProvider
    private var assignableExpressionBuilder: AbstractASTBuilder? = null
    override fun verify(): Boolean {
        return when {
            tokens.size < 3-> {
                println("Not enough members for declaration")
                false
            }
            IdentifierBuilder(tokens.subList(0, 1)).build()!=null -> {
                println("Variable declarator is valid")
                false
            }
            tokens[1].type != "COLON" || tokens[1].type == "COLON" && TypeReferenceBuilder(tokens.subList(2,3)).build()!=null -> {
                println("No type declared")
                false
            }
            tokens.size>4-> {
                if (tokens[3].type == "ASSIGN") {
                    assignableExpressionBuilderProvider =
                        AssignableExpressionBuilderProvider(tokens.subList(4, tokens.size))
                    assignableExpressionBuilder = assignableExpressionBuilderProvider.getAssignableExpressionBuilder()
                    if (assignableExpressionBuilder != null) {
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

    override fun build(): VariableDeclarator? {
        return if (verify()) VariableDeclarator(
            id = IdentifierBuilder(tokens.subList(0, 1)).build()!!,
            type = TypeReferenceBuilder(tokens.subList(2, 3)).build(),
            init = assignableExpressionBuilder?.build() as Expression,
            start = tokens.first().position.start,
            end = tokens.last().position.end
        )else null
    }
}
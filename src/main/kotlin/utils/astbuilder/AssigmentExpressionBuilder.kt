package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Token

class AssigmentExpressionBuilder(tokens:List<Token>): AbstractASTBuilder(tokens) {
    override fun verify(): Boolean {
        return when{
            tokens.size < 3 -> false
            IdentifierBuilder(tokens.subList(0,1)).build()!=null && tokens[1].value == "ASSIGN" -> {
                val assignableExpressionBuilderProvider = AssignableExpressionBuilderProvider(tokens.subList(2, tokens.size))
                assignableExpressionBuilderProvider.getAssignableExpressionBuilder() != null
            }
            else -> false
        }
    }

    override fun build(): ASTNode {
        TODO("Not yet implemented")
    }

}
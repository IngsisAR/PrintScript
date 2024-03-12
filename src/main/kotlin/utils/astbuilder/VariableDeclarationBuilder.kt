package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.astbuilder.AbstractASTBuilder
import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Token

class VariableDeclarationBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    override fun verify(): Boolean {
        return when {
            tokens.isEmpty() -> {
                println("No tokens provided")
                false
            }
            tokens.size < 5 -> {
                println("Not enough members for declaration")
                false
            }
            tokens.first().type != "LET" || tokens.first().type != "CONST"-> {
                println("First token is not a variable declaration")
                false
            }
            tokens[1].type != "ID" -> {
                println("Variable declaration does not have identifier")
                false
            }
            tokens[2].type != "COLON" || tokens[2].type == "COLON" && tokens[3].type != "TYPE" ->{
                println("No type declared")
                false
            }
            else -> {
                println("Tokens are valid")
                true
            }
        }
    }

    override fun build(): ASTNode {
        TODO("Not yet implemented")
    }
}
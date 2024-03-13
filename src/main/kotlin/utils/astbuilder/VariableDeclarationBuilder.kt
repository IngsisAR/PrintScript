package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.*

class VariableDeclarationBuilder(tokens:List<Token>) : AbstractASTBuilder(tokens) {
    private lateinit var variableDeclarators:List<VariableDeclarator>
    override fun verify(): Boolean {
        if (tokens.isEmpty()) {
            println("No tokens provided")
            return false
        }

        if (tokens.first().type != "LET" || tokens.first().type != "CONST") {
            println("First token is not a variable declaration")
            return false
        }

        if (tokens.last().type != "SEMICOLON") {
            println("Last token is not a semicolon")
            return false
        }

        val commaCount = tokens.count { it.type == "COMMA" }
        if (commaCount == 0) {
            val variableDeclaratorBuilder = VariableDeclaratorBuilder(tokens.subList(1, tokens.size - 1))
            if (!variableDeclaratorBuilder.verify()) {
                return false
            }
            variableDeclarators+= variableDeclaratorBuilder.build()
            return true
        }
        var tokensAux = tokens.subList(1, tokens.size - 1)
        for (i in 0 until commaCount) {
            val commaIndex = tokens.indexOfFirst { it.type == "COMMA" }
            val variableDeclaratorBuilder = VariableDeclaratorBuilder(tokens.subList(0, commaIndex))
            if (!variableDeclaratorBuilder.verify()) {
                return false
            }
            variableDeclarators+= variableDeclaratorBuilder.build()
            tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
        }

        return true
    }

    override fun build(): VariableDeclaration {
        TODO("Not yet implemented")
    }
}
package astbuilder

import Token
import VariableDeclaration
import VariableDeclarator

class VariableDeclarationBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    private var variableDeclarators: List<VariableDeclarator> = emptyList()

    override fun verify(): Boolean {
        if (tokens.isEmpty()) {
            println("No tokens provided")
            return false
        }

        if (tokens.last().type != "SEMICOLON") {
            println("Last token is not a semicolon")
            return false
        }

        if (tokens.first().type != "LET" && tokens.first().type != "CONST") {
            println("First token is not a variable declaration")
            return false
        }

        val commaCount = tokens.count { it.type == "COMMA" }
        if (commaCount == 0) {
            val variableDeclarator =
                VariableDeclaratorBuilder(tokens.subList(1, tokens.size - 1))
                    .verifyAndBuild() ?: return false
            variableDeclarators += variableDeclarator
            return true
        }
        var tokensAux = tokens.subList(1, tokens.size - 1)
        for (i in 0 until commaCount) {
            val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
            val variableDeclarator =
                VariableDeclaratorBuilder(tokens.subList(0, commaIndex))
                    .verifyAndBuild() ?: return false
            variableDeclarators += variableDeclarator
            tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
        }
        return true
    }

    override fun verifyAndBuild(): VariableDeclaration? =
        if (verify()) {
            VariableDeclaration(
                kind = tokens.first().type,
                declarations = variableDeclarators,
                start = tokens.first().position.start,
                end = tokens.last().position.end,
            )
        } else {
            null
        }
}

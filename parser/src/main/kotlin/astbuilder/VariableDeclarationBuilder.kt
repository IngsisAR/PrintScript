package astbuilder

import Token
import VariableDeclaration
import VariableDeclarator

class VariableDeclarationBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    private var variableDeclarators: List<VariableDeclarator> = emptyList()

    override fun verify(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Empty tokens")
        }

        if (tokens.last().type != "SEMICOLON") {
            return ASTBuilderFailure("Missing semicolon at variable declaration")
        }

        if (tokens.first().type != "LET" && tokens.first().type != "CONST") {
            return ASTBuilderFailure("Invalid start of variable declaration")
        }

        val commaCount = tokens.count { it.type == "COMMA" }
        if (commaCount == 0) {
            val variableDeclaratorResult =
                VariableDeclaratorBuilder(tokens.subList(1, tokens.size - 1))
                    .verifyAndBuild()
            if (variableDeclaratorResult is ASTBuilderFailure) {
                return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
            }
            variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
            return variableDeclaratorResult
        }
        var tokensAux = tokens.subList(1, tokens.size - 1)
        var declaratorResult: ASTBuilderResult = ASTBuilderFailure("Invalid variable declaration")
        for (i in 0 until commaCount + 1) {
            if (i == commaCount) {
                val variableDeclaratorResult =
                    VariableDeclaratorBuilder(tokensAux)
                        .verifyAndBuild()
                if (variableDeclaratorResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
                }
                declaratorResult = variableDeclaratorResult
                variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
                break
            }
            val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
            val variableDeclaratorResult =
                VariableDeclaratorBuilder(tokensAux.subList(0, commaIndex))
                    .verifyAndBuild()
            if (variableDeclaratorResult is ASTBuilderFailure) {
                return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
            }
            declaratorResult = variableDeclaratorResult
            variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
            tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
        }
        return declaratorResult
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                VariableDeclaration(
                    kind = tokens.first().value,
                    declarations = variableDeclarators,
                    start = tokens.first().position.start,
                    end = tokens.last().position.end,
                ),
            )
        } else {
            result
        }
    }
}

package astbuilder

import Token
import VariableDeclaration
import VariableDeclarator

class VariableDeclarationBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    private var variableDeclarators: List<VariableDeclarator> = emptyList()

    override fun verify(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Empty tokens")
        }

        if (tokens.last().type != "SEMICOLON") {
            return ASTBuilderFailure("Missing semicolon at ($lineIndex, ${tokens.last().position.end})")
        }

        if (tokens.any { it.type == "COLON" } || tokens.any { it.type == "LET" || it.type == "CONST" }) {
            if (tokens.first().type != "LET" && tokens.first().type != "CONST") {
                return ASTBuilderFailure(
                    "Invalid start of variable declaration at " +
                        "($lineIndex, ${tokens.first().position.start})",
                )
            }

            val commaCount = tokens.count { it.type == "COMMA" }
            var tokensAux = tokens.subList(1, tokens.size - 1)
            if (commaCount == 0) {
                if (tokensAux.size < 3) {
                    return ASTBuilderFailure(
                        "Invalid variable declaration: not enough tokens for a variable declarator at " +
                            "($lineIndex, ${tokens.first().position.end})",
                    )
                }
                val variableDeclaratorResult =
                    VariableDeclaratorBuilder(tokens.subList(1, tokens.size - 1), lineIndex)
                        .verifyAndBuild()
                if (variableDeclaratorResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
                }
                variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
                return variableDeclaratorResult
            }
            var declaratorResult: ASTBuilderResult = ASTBuilderFailure("Invalid variable declaration")
            for (i in 0 until commaCount + 1) {
                if (tokensAux.size < 3) {
                    return ASTBuilderFailure(
                        "Invalid variable declaration: not enough tokens for a variable declarator at " +
                            "($lineIndex, ${tokensAux.first().position.start})",
                    )
                }
                if (i == commaCount) {
                    val variableDeclaratorResult =
                        VariableDeclaratorBuilder(tokensAux, lineIndex)
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
                    VariableDeclaratorBuilder(tokensAux.subList(0, commaIndex), lineIndex)
                        .verifyAndBuild()
                if (variableDeclaratorResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
                }
                declaratorResult = variableDeclaratorResult
                variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
                tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
            }
            return declaratorResult
        } else {
            return ASTBuilderFailure("Invalid variable declaration")
        }
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

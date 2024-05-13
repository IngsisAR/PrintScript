package astbuilder

import Token
import VariableDeclaration
import VariableDeclarator

class VariableDeclarationBuilder(
    val tokens: List<Token>,
    private val astProviderFactory: ASTProviderFactory,
) : ASTBuilder {
    private var variableDeclarators: List<VariableDeclarator> = emptyList()

    override fun verifyAndBuild(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Empty tokens")
        }

        if (tokens.last().type != "SEMICOLON") {
            return ASTBuilderFailure("Missing semicolon at (${tokens.last().position.line}, ${tokens.last().position.end})")
        }

        if (tokens.any { it.type == "COLON" } || tokens.any { it.type == "LET" || it.type == "CONST" }) {
            if (tokens.first().type != "LET" && tokens.first().type != "CONST") {
                return ASTBuilderFailure(
                    "Invalid start of variable declaration at " +
                        "(${tokens.first().position.line}, ${tokens.first().position.start})",
                )
            }

            val commaCount = tokens.count { it.type == "COMMA" }
            var tokensAux = tokens.subList(1, tokens.size - 1)
            if (commaCount == 0) {
                if (tokensAux.size < 3) {
                    return ASTBuilderFailure(
                        "Invalid variable declaration: not enough tokens for a variable declarator at " +
                            "(${tokens.first().position.line}, ${tokens.first().position.end})",
                    )
                }
                val variableDeclaratorResult =
                    VariableDeclaratorBuilder(tokens.subList(1, tokens.size - 1), astProviderFactory)
                        .verifyAndBuild()
                if (variableDeclaratorResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
                }
                variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
                return ASTBuilderSuccess(
                    VariableDeclaration(
                        declarations = variableDeclarators,
                        kind = tokens.first().value,
                        line = tokens.first().position.line,
                        start = tokens.first().position.start,
                        end = tokens.last().position.end,
                    ),
                )
            }
            for (i in 0 until commaCount + 1) {
                if (tokensAux.size < 3) {
                    return ASTBuilderFailure(
                        "Invalid variable declaration: not enough tokens for a variable declarator at " +
                            "(${tokensAux.first().position.line}, ${tokensAux.first().position.start})",
                    )
                }
                if (i == commaCount) {
                    val variableDeclaratorResult =
                        VariableDeclaratorBuilder(tokensAux, astProviderFactory)
                            .verifyAndBuild()
                    if (variableDeclaratorResult is ASTBuilderFailure) {
                        return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
                    }
                    variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
                    break
                }
                val commaIndex = tokensAux.indexOfFirst { it.type == "COMMA" }
                val variableDeclaratorResult =
                    VariableDeclaratorBuilder(tokensAux.subList(0, commaIndex), astProviderFactory)
                        .verifyAndBuild()
                if (variableDeclaratorResult is ASTBuilderFailure) {
                    return ASTBuilderFailure("Invalid variable declaration: ${variableDeclaratorResult.errorMessage}")
                }
                variableDeclarators += (variableDeclaratorResult as ASTBuilderSuccess).astNode as VariableDeclarator
                tokensAux = tokensAux.subList(commaIndex + 1, tokensAux.size)
            }
            return ASTBuilderSuccess(
                VariableDeclaration(
                    declarations = variableDeclarators,
                    kind = tokens.first().value,
                    line = tokens.first().position.line,
                    start = tokens.first().position.start,
                    end = tokens.last().position.end,
                ),
            )
        } else {
            return ASTBuilderFailure("Invalid variable declaration")
        }
    }
}

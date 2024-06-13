package astbuilder

import Expression
import Identifier
import Token
import TypeReference
import VariableDeclarator

class VariableDeclaratorBuilder(
    val tokens: List<Token>,
    private val astProviderFactory: ASTProviderFactory,
) : ASTBuilder {
    private lateinit var identifier: Identifier
    private lateinit var typeReference: TypeReference
    private var init: Expression? = null

    override fun verifyAndBuild(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Not enough tokens for a variable declarator")
        }

        val idBuilderResult = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()
        if (idBuilderResult is ASTBuilderFailure) {
            return ASTBuilderFailure(
                "Invalid declarator: missing identifier at (${tokens.first().position.start}:${tokens.first().position.start})",
            )
        } else {
            identifier = (idBuilderResult as ASTBuilderSuccess).astNode as Identifier
        }

        if (tokens.size < 2 || tokens[1].type != "COLON") {
            return ASTBuilderFailure(
                "Invalid declarator: Missing colon at (${tokens.first().position.line}:${tokens.first().position.end})",
            )
        }

        if (tokens.size < 3) {
            return ASTBuilderFailure("Invalid declarator: Missing type at (${tokens.last().position.line}:${tokens.last().position.end})")
        }

        val typeBuilderResult = TypeReferenceBuilder(tokens.subList(2, 3)).verifyAndBuild()
        if (typeBuilderResult is ASTBuilderFailure) {
            return ASTBuilderFailure("Invalid declarator: Missing type at (${tokens[2].position.line}:${tokens[2].position.start})")
        } else if (typeBuilderResult is ASTBuilderSuccess) {
            typeReference = typeBuilderResult.astNode as TypeReference
        }
        if (tokens.size == 4 && tokens[3].type != "ASSIGN") {
            return ASTBuilderFailure(
                "Invalid declarator: Missing assignment operator at (${tokens.last().position.line}:${tokens.last().position.end})",
            )
        }
        if (tokens.size == 4) {
            return ASTBuilderFailure(
                "Invalid declarator: Missing assigned expression at (${tokens.last().position.line}:${tokens.last().position.end})",
            )
        }
        if (tokens.size > 4) {
            return if (tokens[3].type == "ASSIGN") {
                val assignableExpressionResult =
                    astProviderFactory.changeTokens(tokens.subList(4, tokens.size))
                        .getProviderByType("assignableExpression").getASTBuilderResult()
                if (assignableExpressionResult is ASTBuilderFailure) {
                    val errorMessage = assignableExpressionResult.errorMessage
                    return if (errorMessage.isNotBlank() || errorMessage.isNotEmpty()) {
                        ASTBuilderFailure("Invalid declarator: $errorMessage")
                    } else {
                        ASTBuilderFailure(
                            "Invalid declarator: Invalid assigned expression at (${tokens[3].position.line}:${tokens[3].position.end})",
                        )
                    }
                } else {
                    init = (assignableExpressionResult as ASTBuilderSuccess).astNode as Expression
                    ASTBuilderSuccess(
                        VariableDeclarator(
                            id = identifier,
                            type = typeReference,
                            init = init,
                            line = tokens.first().position.line,
                            start = tokens.first().position.start,
                            end = tokens.last().position.end,
                        ),
                    )
                }
            } else {
                ASTBuilderFailure(
                    "Invalid declarator: Missing assignment operator at (${tokens[3].position.line}:${tokens[3].position.start})",
                )
            }
        }
        return ASTBuilderSuccess(
            VariableDeclarator(
                id = identifier,
                type = typeReference,
                init = init,
                line = tokens.first().position.line,
                start = tokens.first().position.start,
                end = tokens.last().position.end,
            ),
        )
    }
}

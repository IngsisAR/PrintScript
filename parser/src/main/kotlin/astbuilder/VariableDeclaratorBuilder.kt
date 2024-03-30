package astbuilder

import Expression
import Identifier
import Token
import TypeReference
import VariableDeclarator

class VariableDeclaratorBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    private lateinit var identifier: Identifier
    private lateinit var typeReference: TypeReference
    private var init: Expression? = null

    override fun verify(): ASTBuilderResult {
        if (tokens.size < 3) {
            return ASTBuilderFailure("Not enough tokens for a variable declarator")
        }

        val idBuilderResult = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()
        if (idBuilderResult is ASTBuilderFailure) {
            return ASTBuilderFailure("Invalid declarator: ${(idBuilderResult).errorMessage}")
        } else {
            identifier = (idBuilderResult as ASTBuilderSuccess).astNode as Identifier
        }

        if (tokens[1].type != "COLON") {
            return ASTBuilderFailure("Invalid declarator: Missing colon")
        }

        val typeBuilderResult = TypeReferenceBuilder(tokens.subList(2, 3)).verifyAndBuild()
        if (tokens[1].type == "COLON" &&
            typeBuilderResult is ASTBuilderFailure
        ) {
            return ASTBuilderFailure("Invalid declarator: ${typeBuilderResult.errorMessage}")
        } else if (typeBuilderResult is ASTBuilderSuccess) {
            typeReference = typeBuilderResult.astNode as TypeReference
        }

        if (tokens.size > 4) {
            return if (tokens[3].type == "ASSIGN") {
                val assignableExpressionResult =
                    AssignableExpressionProvider(
                        tokens.subList(4, tokens.size),
                    ).getAssignableExpressionResult()
                if (assignableExpressionResult is ASTBuilderFailure) {
                    ASTBuilderFailure("Invalid declarator: ${assignableExpressionResult.errorMessage}")
                } else {
                    init = (assignableExpressionResult as ASTBuilderSuccess).astNode as Expression
                    assignableExpressionResult
                }
            } else {
                ASTBuilderFailure("Invalid declarator: Missing assignment operator")
            }
        }
        return idBuilderResult
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                VariableDeclarator(
                    id = identifier,
                    type = typeReference,
                    init = init,
                    start = tokens.first().position.start,
                    end = tokens.last().position.end,
                ),
            )
        } else {
            result
        }
    }
}

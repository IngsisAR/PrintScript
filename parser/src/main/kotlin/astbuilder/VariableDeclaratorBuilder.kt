package astbuilder

import Expression
import Identifier
import Token
import TypeReference
import VariableDeclarator

class VariableDeclaratorBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    private lateinit var identifier: Identifier
    private lateinit var typeReference: TypeReference
    private var init: Expression? = null

    override fun verify(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Not enough tokens for a variable declarator")
        }

        val idBuilderResult = IdentifierBuilder(tokens.subList(0, 1), lineIndex).verifyAndBuild()
        if (idBuilderResult is ASTBuilderFailure) {
            return ASTBuilderFailure("Invalid declarator: missing identifier at ($lineIndex, ${tokens.first().position.start})")
        } else {
            identifier = (idBuilderResult as ASTBuilderSuccess).astNode as Identifier
        }

        if (tokens.size < 2 || tokens[1].type != "COLON") {
            return ASTBuilderFailure("Invalid declarator: Missing colon at ($lineIndex, ${tokens[0].position.end})")
        }

        if (tokens.size < 3) {
            return ASTBuilderFailure("Invalid declarator: Missing type at ($lineIndex, ${tokens.last().position.end})")
        }

        val typeBuilderResult = TypeReferenceBuilder(tokens.subList(2, 3), lineIndex).verifyAndBuild()
        if (typeBuilderResult is ASTBuilderFailure) {
            return ASTBuilderFailure("Invalid declarator: Missing type at ($lineIndex, ${tokens.last().position.start})")
        } else if (typeBuilderResult is ASTBuilderSuccess) {
            typeReference = typeBuilderResult.astNode as TypeReference
        }
        if (tokens.size == 4 && tokens[3].type != "ASSIGN") {
            return ASTBuilderFailure("Invalid declarator: Missing assignment operator at ($lineIndex, ${tokens.last().position.end})")
        }
        if (tokens.size == 4) {
            return ASTBuilderFailure("Invalid declarator: Missing assignment expression at ($lineIndex, ${tokens.last().position.end})")
        }
        if (tokens.size > 4) {
            return if (tokens[3].type == "ASSIGN") {
                val assignableExpressionResult =
                    AssignableExpressionProvider(
                        tokens.subList(4, tokens.size),
                        lineIndex,
                    ).getAssignableExpressionResult()
                if (assignableExpressionResult is ASTBuilderFailure) {
                    ASTBuilderFailure("Invalid declarator: ${assignableExpressionResult.errorMessage}")
                } else {
                    init = (assignableExpressionResult as ASTBuilderSuccess).astNode as Expression
                    assignableExpressionResult
                }
            } else {
                ASTBuilderFailure("Invalid declarator: Missing assignment operator at ($lineIndex, ${tokens[3].position.start})")
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

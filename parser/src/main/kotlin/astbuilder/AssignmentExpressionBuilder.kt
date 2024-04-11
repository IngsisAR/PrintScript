package astbuilder

import AssignmentExpression
import Expression
import Identifier
import Token

class AssignmentExpressionBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    private var assignableExpressionResult: ASTBuilderResult = ASTBuilderFailure("Not implemented")
    private var identifierResult: ASTBuilderResult = ASTBuilderFailure("Not implemented")

    override fun verify(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Not enough tokens to build assignment expression")
        }
        if (tokens.any { it.type == "ASSIGN" }) {
            identifierResult = IdentifierBuilder(tokens.subList(0, 1), lineIndex).verifyAndBuild()
            if (tokens.size < 3) {
                return if (identifierResult is ASTBuilderFailure) {
                    ASTBuilderFailure("missing identifier at ($lineIndex, ${tokens[0].position.start})")
                } else {
                    ASTBuilderFailure("missing expression after assignment at ($lineIndex, ${tokens[1].position.start})")
                }
            }
            if (identifierResult is ASTBuilderSuccess && tokens[1].type == "ASSIGN") {
                assignableExpressionResult =
                    AssignableExpressionProvider(tokens.subList(2, tokens.size), lineIndex)
                        .getAssignableExpressionResult()
                return if (assignableExpressionResult is ASTBuilderSuccess &&
                    (assignableExpressionResult as ASTBuilderSuccess).astNode is Expression
                ) {
                    assignableExpressionResult
                } else {
                    ASTBuilderFailure((assignableExpressionResult as ASTBuilderFailure).errorMessage)
                }
            } else {
                return ASTBuilderFailure("Invalid assignment expression")
            }
        } else {
            return ASTBuilderFailure("Invalid assignment expression")
        }
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                AssignmentExpression(
                    (identifierResult as ASTBuilderSuccess).astNode as Identifier,
                    (assignableExpressionResult as ASTBuilderSuccess).astNode as Expression,
                    tokens.first().position.start,
                    tokens.last().position.end,
                ),
            )
        } else {
            result
        }
    }
}
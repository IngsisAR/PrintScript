package astbuilder

import AssigmentExpression
import Expression
import Identifier
import Token

class AssigmentExpressionBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    private var assignableExpressionResult: ASTBuilderResult = ASTBuilderFailure("Not implemented")
    private var identifierResult: ASTBuilderResult = ASTBuilderFailure("Not implemented")

    override fun verify(): ASTBuilderResult {
        if (tokens.size < 3) {
            return ASTBuilderFailure("Not enough tokens to build AssigmentExpression")
        }
        identifierResult = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()
        if (identifierResult is ASTBuilderSuccess && tokens[1].type == "ASSIGN") {
            assignableExpressionResult =
                AssignableExpressionProvider(tokens.subList(2, tokens.size))
                    .getAssignableExpressionResult()
            return if (assignableExpressionResult is ASTBuilderSuccess &&
                (assignableExpressionResult as ASTBuilderSuccess).astNode is Expression
            ) {
                assignableExpressionResult
            } else {
                ASTBuilderFailure("No valid assignable expression found")
            }
        } else {
            return ASTBuilderFailure("Invalid AssigmentExpression")
        }
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            ASTBuilderSuccess(
                AssigmentExpression(
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

package astbuilder

import AssignmentExpression
import Expression
import Identifier
import Token

class AssignmentExpressionBuilder(
    val tokens: List<Token>,
    private val factoryProvider: ASTProviderFactory,
) : ASTBuilder {
    private var assignableExpressionResult: ASTBuilderResult = ASTBuilderFailure("Not implemented")
    private var identifierResult: ASTBuilderResult = ASTBuilderFailure("Not implemented")

    override fun verifyAndBuild(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Not enough tokens to build assignment expression")
        }
        if (tokens.any { it.type == "ASSIGN" }) {
            identifierResult = IdentifierBuilder(tokens.subList(0, 1)).verifyAndBuild()
            if (tokens.size < 3) {
                return if (identifierResult is ASTBuilderFailure) {
                    ASTBuilderFailure("missing identifier at (${tokens[0].position.line}:${tokens[0].position.start})")
                } else {
                    ASTBuilderFailure("missing expression after assignment at (${tokens[1].position.line}:${tokens[1].position.end})")
                }
            }
            if (identifierResult is ASTBuilderSuccess && tokens[1].type == "ASSIGN") {
                assignableExpressionResult =
                    factoryProvider.changeTokens(tokens.subList(2, tokens.size))
                        .getProviderByType("assignableExpression").getASTBuilderResult()
                return if (assignableExpressionResult is ASTBuilderSuccess &&
                    (assignableExpressionResult as ASTBuilderSuccess).astNode is Expression
                ) {
                    ASTBuilderSuccess(
                        AssignmentExpression(
                            (identifierResult as ASTBuilderSuccess).astNode as Identifier,
                            (assignableExpressionResult as ASTBuilderSuccess).astNode as Expression,
                            tokens.first().position.line,
                            tokens.first().position.start,
                            tokens.last().position.end,
                        ),
                    )
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
}

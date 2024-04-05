package astbuilder

import Identifier
import Token

class IdentifierBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    override fun verify(): ASTBuilderResult =
        if (tokens.size == 1 && tokens.first().type == "ID") {
            ASTBuilderSuccess(
                Identifier(
                    tokens.first().value,
                    tokens.first().position.start,
                    tokens.first().position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid identifier")
        }

    override fun verifyAndBuild(): ASTBuilderResult = verify()
}

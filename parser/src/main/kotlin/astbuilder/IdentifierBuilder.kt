package astbuilder

import Identifier
import Token

class IdentifierBuilder(
    val tokens: List<Token>,
    val lineIndex: Int,
) : ASTBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
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
}

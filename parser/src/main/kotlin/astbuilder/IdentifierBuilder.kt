package astbuilder

import utils.Identifier
import utils.Token

class IdentifierBuilder(
    val tokens: List<Token>,
) : ASTBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        if (tokens.size == 1 && tokens.first().type == "ID") {
            ASTBuilderSuccess(
                Identifier(
                    tokens.first().value,
                    tokens.first().position.line,
                    tokens.first().position.start,
                    tokens.first().position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid identifier")
        }
}

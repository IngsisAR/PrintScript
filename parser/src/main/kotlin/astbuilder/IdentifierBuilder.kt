package astbuilder

import Identifier
import Token

class IdentifierBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    override fun verify(): Boolean = tokens.size == 1 && tokens.first().type == "ID"

    override fun verifyAndBuild(): Identifier? =
        if (verify()) {
            Identifier(tokens.first().value!!, tokens.first().position.start, tokens.first().position.end)
        } else {
            null
        }
}

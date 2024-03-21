package astbuilder

import NumberLiteral
import Token

class NumberLiteralBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    override fun verify(): Boolean = tokens.size == 1 && tokens[0].type == "NUMBER"

    override fun verifyAndBuild(): NumberLiteral? =
        if (verify()) {
            NumberLiteral(
                tokens[0].value!!.toBigDecimal(),
                tokens[0].position.start,
                tokens[0].position.end,
            )
        } else {
            null
        }
}

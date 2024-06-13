package astbuilder

import utils.NumberLiteral
import utils.Token

class NumberLiteralBuilder(
    val tokens: List<Token>,
) : LiteralBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        if (tokens.size == 1 && tokens[0].type == "NUMBER") {
            ASTBuilderSuccess(
                NumberLiteral(
                    tokens[0].value.toBigDecimal(),
                    tokens[0].position.line,
                    tokens[0].position.start,
                    tokens[0].position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid number")
        }
}

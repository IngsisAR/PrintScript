package astbuilder

import NumberLiteral
import Token

class NumberLiteralBuilder(
    val tokens: List<Token>,
    val lineIndex: Int,
) : LiteralBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        if (tokens.size == 1 && tokens[0].type == "NUMBER") {
            ASTBuilderSuccess(
                NumberLiteral(
                    tokens[0].value.toBigDecimal(),
                    tokens[0].position.start,
                    tokens[0].position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid number")
        }
}

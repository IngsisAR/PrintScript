package astbuilder

import StringLiteral
import Token

class StringLiteralBuilder(
    val tokens: List<Token>,
) : LiteralBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        if (tokens.size == 1 && tokens[0].type == "STRING") {
            ASTBuilderSuccess(
                StringLiteral(
                    tokens[0].value,
                    tokens[0].position.line,
                    tokens[0].position.start,
                    tokens[0].position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid string")
        }
}

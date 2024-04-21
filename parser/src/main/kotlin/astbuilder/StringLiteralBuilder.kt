package astbuilder

import StringLiteral
import Token

class StringLiteralBuilder(
    val tokens: List<Token>,
    val lineIndex: Int,
) : LiteralBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        if (tokens.size == 1 && tokens[0].type == "STRING") {
            ASTBuilderSuccess(
                StringLiteral(
                    tokens[0].value,
                    tokens[0].position.start,
                    tokens[0].position.end,
                ),
            )
        } else {
            ASTBuilderFailure("Invalid string")
        }
}

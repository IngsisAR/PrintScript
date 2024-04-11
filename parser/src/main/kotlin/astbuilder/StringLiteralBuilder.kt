package astbuilder

import StringLiteral
import Token

class StringLiteralBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex),
    LiteralBuilder {
    override fun verify(): ASTBuilderResult =
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

    override fun verifyAndBuild(): ASTBuilderResult = verify()
}

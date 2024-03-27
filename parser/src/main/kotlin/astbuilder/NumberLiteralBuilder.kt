package astbuilder

import NumberLiteral
import Token

class NumberLiteralBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
    override fun verify(): ASTBuilderResult =
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

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            result
        } else {
            result
        }
    }
}

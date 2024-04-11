package astbuilder

import BooleanLiteral
import Token

class BooleanLiteralBuilder(
    tokens: List<Token>,
    val lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex),
    LiteralBuilder {
        override fun verify(): ASTBuilderResult =
            if (tokens.size == 1 && tokens[0].type == "BOOLEAN") {
                ASTBuilderSuccess(
                    BooleanLiteral(
                        tokens[0].value.toBoolean(),
                        tokens[0].position.start,
                        tokens[0].position.end,
                    ),
                )
            } else {
                ASTBuilderFailure("Invalid boolean")
            }

        override fun verifyAndBuild(): ASTBuilderResult = verify()
    }
package astbuilder

import Token
import TypeReference

class TypeReferenceBuilder(
    tokens: List<Token>,
    lineIndex: Int,
) : AbstractASTBuilder(tokens, lineIndex) {
    override fun verify(): ASTBuilderResult =
        tokens
            .firstOrNull()
            ?.let {
                if (it.type == "TYPE") {
                    ASTBuilderSuccess(
                        TypeReference(
                            type = tokens.first().value,
                            start = tokens.first().position.start,
                            end = tokens.first().position.end,
                        ),
                    )
                } else {
                    ASTBuilderFailure("Invalid type")
                }
            }
            ?: ASTBuilderFailure("Empty tokens")

    override fun verifyAndBuild(): ASTBuilderResult = verify()
}

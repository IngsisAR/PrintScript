package astbuilder

import Token
import TypeReference

class TypeReferenceBuilder(
    tokens: List<Token>,
) : AbstractASTBuilder(tokens) {
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
            ?: ASTBuilderFailure("Invalid type")

    override fun verifyAndBuild(): ASTBuilderResult {
        val result = verify()
        return if (result is ASTBuilderSuccess) {
            result
        } else {
            result
        }
    }
}

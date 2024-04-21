package astbuilder

import Token
import TypeReference

class TypeReferenceBuilder(
    val tokens: List<Token>,
    val lineIndex: Int,
) : ASTBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
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
                    ASTBuilderFailure("Invalid type at ($lineIndex, ${it.position.start})")
                }
            }
            ?: ASTBuilderFailure("Empty tokens")
}

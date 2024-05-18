package astbuilder

import Token
import TypeReference

class TypeReferenceBuilder(
    val tokens: List<Token>,
) : ASTBuilder {
    override fun verifyAndBuild(): ASTBuilderResult =
        tokens
            .firstOrNull()
            ?.let {
                if (it.type == "TYPE") {
                    ASTBuilderSuccess(
                        TypeReference(
                            type = tokens.first().value,
                            line = tokens.first().position.line,
                            start = tokens.first().position.start,
                            end = tokens.first().position.end,
                        ),
                    )
                } else {
                    ASTBuilderFailure("Invalid type at (${it.position.line}:${it.position.start})")
                }
            }
            ?: ASTBuilderFailure("Empty tokens")
}

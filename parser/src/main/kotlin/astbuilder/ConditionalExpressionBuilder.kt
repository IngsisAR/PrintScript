package astbuilder

import Token

class ConditionalExpressionBuilder(tokens: List<Token>, val lineIndex: Int) : AbstractASTBuilder(tokens, lineIndex) {
    override fun verify(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Not enough tokens to build conditional expression")
        }
        return if (tokens[0].type == "IF" || tokens[1].type == "OPAREN" ||
            tokens[2].type == "ID" || tokens[3].type == "CPAREN" ||
            tokens[4].type == "OBRACE" || tokens.last().type == "CBRACE"
        ) {
            ExpressionProvider(tokens.subList(5, tokens.size - 1), lineIndex).getVerifiedExpressionResult()
        } else if (tokens[0].type == "ELSE" || tokens[1].type == "OBRACE" || tokens.last().type == "CBRACE") {
            ExpressionProvider(tokens.subList(2, tokens.size - 1), lineIndex).getVerifiedExpressionResult()
        } else {
            ASTBuilderFailure("Invalid conditional expression")
        }
    }

    override fun verifyAndBuild(): ASTBuilderResult {
        return verify()
    }
}

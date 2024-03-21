package astbuilder

import Token
import TypeReference

class TypeReferenceBuilder(tokens:List<Token>): AbstractASTBuilder(tokens) {
    override fun verify(): Boolean {
        return tokens.size == 1 && tokens.first().type == "TYPE"
    }

    override fun verifyAndBuild(): TypeReference? {
        return if(verify()) TypeReference(
            type = tokens.first().value,
            start = tokens.first().position.start,
            end = tokens.first().position.end
        )else null
    }
}
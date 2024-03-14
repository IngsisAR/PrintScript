package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Token
import australfi.ingsis7.utils.TypeReference

class TypeReferenceBuilder(tokens:List<Token>):AbstractASTBuilder(tokens) {
    override fun verify(): Boolean {
        return tokens.size == 1 && tokens.first().type == "TYPE"
    }

    override fun build(): TypeReference? {
        return if(verify()) TypeReference(
            type = tokens.first().value,
            start = tokens.first().position.start,
            end = tokens.first().position.end
        )else null
    }
}
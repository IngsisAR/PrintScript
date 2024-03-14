package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Identifier
import australfi.ingsis7.utils.Token

class IdentifierBuilder(tokens:List<Token>): AbstractASTBuilder(tokens) {
    override fun verify(): Boolean {
        return tokens.size==1 && tokens.first().type == "ID"
    }

    override fun build(): Identifier? {
        return if (verify())
            Identifier(tokens.first().value!!, tokens.first().position.start, tokens.first().position.end)
        else null
    }
}
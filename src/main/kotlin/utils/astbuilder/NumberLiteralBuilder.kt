package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.NumberLiteral
import australfi.ingsis7.utils.Token

class NumberLiteralBuilder(tokens: List<Token>):AbstractASTBuilder(tokens) {
    override fun verify(): Boolean {
        return tokens.size==1 && tokens[0].type == "NUMBER"
    }

    override fun verifyAndBuild(): NumberLiteral? {
        return if (verify())
            NumberLiteral(tokens[0].value!!.toBigDecimal(),
            tokens[0].position.start, tokens[0].position.end)
        else null
    }
}
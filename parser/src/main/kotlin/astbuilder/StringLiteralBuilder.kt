package astbuilder

import StringLiteral
import Token

class StringLiteralBuilder(tokens:List<Token>): AbstractASTBuilder(tokens){
    override fun verify(): Boolean {
        return tokens.size == 1 && tokens[0].type == "STRING"
    }

    override fun verifyAndBuild(): StringLiteral? {
        return if (verify())
            StringLiteral(tokens[0].value!!, tokens[0].position.start,
                tokens[0].position.end)
        else null
    }

}
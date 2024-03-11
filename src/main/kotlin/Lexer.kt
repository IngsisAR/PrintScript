package australfi.ingsis7
import australfi.ingsis7.utils.*

class Lexer (val input: String) {
    private var currentPosition = 0

    fun tokenize() : List<Token> {
        val tokens = mutableListOf<Token>()
        while(hasNext()){
            val token = getNextToken()
            if(token != null){
                tokens.add(token)
            }
        }
        return tokens
    }

    private fun hasNext() = currentPosition < input.length;

    private fun getNextToken(): Token? {
        val remainingInput = input.substring(currentPosition)

        for (regexToken in tokenReg) {
            val matchResult = regexToken.regex.toRegex().find(remainingInput)
            if (matchResult != null && matchResult.range.first == 0) {
                val matchedValue = matchResult.value
                val token = createToken(matchedValue, regexToken.token)
                currentPosition += matchedValue.length
                return token
            }
        }

        // Ignore white spaces
        if (remainingInput.isNotEmpty() && remainingInput[0].isWhitespace()) {
            currentPosition++
            return null
        }

        // Handle unexpected characters
        error("Unexpected character at position $currentPosition: ${remainingInput[0]}")
    }


    private fun createToken(matchedValue: String, tokenType: TokenType): Token {
        val startPosition = currentPosition
        val endPosition = currentPosition + matchedValue.length
        val position = Position(startPosition, endPosition)
        return when (tokenType) {
            TokenType.IDENTIFIER -> {
                IDENTIFIER(matchedValue, position)
            }
            TokenType.NUMBER -> {
                val value = matchedValue.toInt()
                NUMBER(value, position)
            }
            TokenType.STRING -> {
                val value = matchedValue.substring(1, matchedValue.length - 1)
                STRING(value, position)
            }
            TokenType.LET -> {
                LET(position)
            }
            TokenType.PRINTLN -> {
                PRINTLN(position)
            }
            TokenType.COLON -> {
                COLON(position)
            }
            TokenType.SEMICOLON -> {
                SEMICOLON(position)
            }
            TokenType.BINARYOPERATION -> {
                BINARYOPERATION(matchedValue, position)
            }
            TokenType.ASSIGN -> {
                ASSIGN(position)
            }
            TokenType.CPAREN -> {
                CPAREN(position)
            }
            TokenType.OPAREN -> {
                OPAREN(position)
            }
            TokenType.CBRACE -> {
                CBRACE(position)
            }
            TokenType.OBRACE -> {
                OBRACE(position)
            }
            TokenType.COMMA -> {
                COMMA(position)
            }
            TokenType.CBRACKET -> {
                CBRACKET(position)
            }
            TokenType.OBRACKET -> {
                OBRACKET(position)
            }
            TokenType.PROGRAM -> {
                PROGRAM(position)
            }
            TokenType.TYPE -> {
                TYPE(position)
            }
        }
    }
}
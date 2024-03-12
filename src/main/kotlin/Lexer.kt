package australfi.ingsis7
import australfi.ingsis7.utils.*

class Lexer (private val input: String) {
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

    private fun hasNext() = currentPosition < input.length

    private fun getNextToken(): Token? {
        val remainingInput = input.substring(currentPosition)

        for (regexToken in tokenReg) {
            val matchResult = regexToken.regex.toRegex().find(remainingInput)
            if (matchResult != null && matchResult.range.first == 0) {
                val matchedValue = matchResult.value
                val token = createToken(matchedValue, regexToken)
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


    private fun createToken(matchedValue: String, tokenType: RegexToken): Token {
        val startPosition = currentPosition
        val endPosition = currentPosition + matchedValue.length
        val position = Position(startPosition, endPosition)
        if (matchedValue == tokenType.regex.replace("\\","")) return Token(tokenType.token.toString(), position)
        return Token(tokenType.token.toString(), position, matchedValue)
    }
}
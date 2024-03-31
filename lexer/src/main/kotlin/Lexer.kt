class Lexer(
    private val input: String,
) {
    private var currentPosition = 0

    @Throws(IllegalStateException::class)
    fun tokenize(): List<Token> {
        if (input.count { it == ';' } > 1) {
            error("Only one line of code is allowed at a time.")
        }
        val tokens = mutableListOf<Token>()
        while (hasNext()) {
            val token = getNextToken()
            if (token != null) {
                tokens.add(token)
            }
        }
        return tokens
    }

    private fun hasNext() = currentPosition < input.length

    @Throws(IllegalStateException::class)
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

    private fun createToken(
        matchedValue: String,
        tokenType: RegexToken,
    ): Token {
        val startPosition = currentPosition
        val endPosition = currentPosition + matchedValue.length
        val position = Position(startPosition, endPosition)
        val trimmedVal = if (tokenType.token == TokenType.STRING) matchedValue.substring(1, matchedValue.length - 1) else matchedValue
        return Token(tokenType.token.toString(), position, trimmedVal)
    }
}

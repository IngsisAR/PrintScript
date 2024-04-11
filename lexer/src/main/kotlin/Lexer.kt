import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class Lexer(
    private val input: String,
    private val lineIndex: Int,
    tokenRegexJsonPath: String,
) {
    private var currentPosition = 0
    private val mapper = jacksonObjectMapper()
    private val tokensRegex: List<TokenRegex> = mapper.readValue(File(tokenRegexJsonPath))

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

        for (tokenRegex in tokensRegex) {
            val matchResult = tokenRegex.regex.toRegex().find(remainingInput)
            if (matchResult != null && matchResult.range.first == 0) {
                val matchedValue = matchResult.value
                val token = createToken(matchedValue, tokenRegex)
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
        error("Unexpected character at ($lineIndex,$currentPosition): ${remainingInput[0]}")
    }

    private fun createToken(
        matchedValue: String,
        tokenType: TokenRegex,
    ): Token {
        val startPosition = currentPosition
        val endPosition = currentPosition + matchedValue.length
        val position = Position(startPosition, endPosition)
        val trimmedVal = if (tokenType.token == "STRING") matchedValue.substring(1, matchedValue.length - 1) else matchedValue
        return Token(tokenType.token, position, trimmedVal)
    }
}

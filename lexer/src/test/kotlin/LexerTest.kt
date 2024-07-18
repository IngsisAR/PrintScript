import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class LexerTest {
    private val tokenRegexJsonPath = Paths.get(ClassLoader.getSystemResource("tokenRegex.json").toURI()).toString()

    @Test
    fun tokenizeWithCorrectCodeLine() {
        val code =
            """
            let a: number = 10;
            """.trimIndent()
        val lexer = Lexer(code, 0, tokenRegexJsonPath)
        val tokens = lexer.tokenize()
        assertEquals(7, tokens.size)
        assertEquals("LET", tokens[0].type)
        assertEquals("ID", tokens[1].type)
        assertEquals("a", tokens[1].value)
        assertEquals("COLON", tokens[2].type)
        assertEquals("TYPE", tokens[3].type)
        assertEquals("ASSIGN", tokens[4].type)
        assertEquals("NUMBER", tokens[5].type)
        assertEquals("10", tokens[5].value)
        assertEquals("SEMICOLON", tokens[6].type)
    }

    @Test
    fun tokenizeThrowsExceptionWithUnexpectedCharacter() {
        val exception: Exception =
            assertThrows(IllegalStateException::class.java) {
                val code =
                    """
                    let' a: number = 10;
                    """.trimIndent()
                val lexer = Lexer(code, 0, tokenRegexJsonPath)
                lexer.tokenize()
            }
        assertEquals("Unexpected token at (0:3)", exception.message)
    }

    @Test
    fun tokenizeWithEmptyInput() {
        val code = ""
        val lexer = Lexer(code, 0, tokenRegexJsonPath)
        val tokens = lexer.tokenize()
        assertEquals(0, tokens.size)
    }
}

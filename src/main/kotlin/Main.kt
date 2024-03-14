package australfi.ingsis7

fun main() {
    val input = """
        let c: number = a + b
    """.trimIndent()

    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
}
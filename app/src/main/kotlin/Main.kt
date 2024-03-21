fun main() {
    val input = """
        let c: number = a + 2 * 3 % 5 / 21;
    """.trimIndent()

    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
    val parser = Parser()
    val ast = parser.parse(tokens)
    println(ast)
}
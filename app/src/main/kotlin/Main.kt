fun main() {
    val input =
        """
        let a: number = 5;
        """.trimIndent()

    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
    val parser = Parser()
    val ast = parser.parse(tokens)
    val interpreterImpl = InterpreterImpl()
    if (ast != null) interpreterImpl.interpret(ast)
    println(ast)
}

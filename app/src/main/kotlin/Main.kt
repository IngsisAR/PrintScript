fun main() {
    val input = """
        let a: number = 5;
    """.trimIndent()

    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
    val parser = Parser()
    val ast = parser.parse(tokens)
    val interpreter = Interpreter()
    if (ast != null) interpreter.interpret(ast)
    println(ast)
}
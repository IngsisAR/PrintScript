package australfi.ingsis7

fun main() {
    val input = """
        let a: number = 5
        let b: number = 10
        let c: number = a + b
        println(c)
    """.trimIndent()

    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
}
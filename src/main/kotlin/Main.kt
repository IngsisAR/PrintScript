package australfi.ingsis7

fun main() {
    val input = """
        let a: number = 5
        let c: number = a + b
        let b: string = "hola como estas"
        println(c)
    """.trimIndent()

    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
}
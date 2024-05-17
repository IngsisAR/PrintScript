import astbuilder.ASTBuilderSuccess
import astbuilder.ASTProviderFactory
import formatter.FormatterImpl

fun main() {
    println("\nReading from string\n")
    val input =
        """
            if(a) {
                if(b) {

                    println("If b");
                } else {

                println("else b");
            }
        } else {

            println("else a");
        }
        """.trimIndent()
    val printScriptChunkReader = PrintScriptChunkReader()
    val lines = printScriptChunkReader.readChunksFromString(input)
    performFromChunks(lines)
//    println("\nReading from file\n")
//    val fileLines = printScriptLineReader.readLinesFromFile("cli/src/main/resources/script_example.txt")
//    performFromLines(fileLines)
}

private fun performFromChunks(fileChunks: List<String>) {
//    var interpreter = InterpreterImpl()
    for (chunk in fileChunks) {
        println(chunk + "\n")
        println("Lexer output")
        val lexer = Lexer(chunk, 0, "utils/src/main/resources/tokenRegex1.1.json")
        val tokens = lexer.tokenize()
        tokens.forEach { println(it) }
        val parser = Parser()
        val ast = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        when (ast) {
            is ASTBuilderSuccess -> {
                val formatted = FormatterImpl().format(ast.astNode, "formatter/src/main/resources/FormatterConfig.json", "1.1.0")
                println("\nFormatted output")
                println(formatted)
            }
            else -> println("Error")
        }
    }
}

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
    val printScriptLineReader = PrintScriptLineReader()
    val lines = printScriptLineReader.readLinesFromString(input)
    performFromLines(lines)
//    println("\nReading from file\n")
//    val fileLines = printScriptLineReader.readLinesFromFile("cli/src/main/resources/script_example.txt")
//    performFromLines(fileLines)
}

private fun performFromLines(fileLines: List<String>) {
    var interpreter = InterpreterImpl()
    for ((index, line) in fileLines.withIndex()) {
        println(line + "\n")
        println("Lexer output")
        val lexer = Lexer(line, 0, "utils/src/main/resources/tokenRegex.json")
        val tokens = lexer.tokenize()
        tokens.forEach { println(it) }
        val parser = Parser()
        val ast = parser.parse(ASTProviderFactory(tokens, index, "1.1.0"))
        println("\nParser Output")
        println("$ast\n")
        if (ast is ASTBuilderSuccess) {
            try {
//                interpreter = interpreter.interpret(ast.astNode)
                println("Formatter Output")
                println(FormatterImpl("formatter/src/main/resources/FormatterConfig.json").format(ast.astNode))
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}

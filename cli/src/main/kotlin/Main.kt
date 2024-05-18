import astbuilder.ASTBuilderFailure
import astbuilder.ASTBuilderSuccess
import astbuilder.ASTProviderFactory
import formatter.FormatterImpl

fun main() {
    val printScriptChunkReader = PrintScriptChunkReader()
//    println("\nReading from string\n")
//    val input =
//        """
//        let a : string = 8 + "th";
//        a = readEnv("PATH");
//        println(a);
//        let d : number = 3.1;
//        d = readInput("input: ");
//        println(d);
//        let b : bool = true;
//
//
//        if (b) {
//            println("a is 4");
//            if(b)
//                println("b is 4");
//            } else {
//                println("b is not 4");
//            }
//            b;
//        } else {
//            println("a is not 4");
//        }
//        """.trimIndent()
//    val lines = printScriptChunkReader.readChunksFromString(input)
//    performFromChunks(lines)
    println("\nReading from file\n")
    val fileLines = printScriptChunkReader.readChunksFromFile("cli/src/main/resources/script_example.txt")
    performFromChunks(fileLines)
}

private fun performFromChunks(fileChunks: List<String>) {
//    var interpreter = InterpreterImpl()
    var chunkStartLine = 1
    for (chunk in fileChunks) {
        println(chunk + "\n")
        println("Lexer output")
        val lexer = Lexer(chunk, chunkStartLine, "utils/src/main/resources/tokenRegex1.1.json")
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
            is ASTBuilderFailure -> println(ast.errorMessage + "\n")
        }
        chunkStartLine = lexer.getCurrentLineIndex() + 1
    }
}

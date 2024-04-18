import astbuilder.ASTBuilderSuccess

fun main() {
    println("\nReading from string\n")
    val input =
        """
        let a:number = 1;
        let b:bool = true;
        let c:bool = true;
        if(b) {
            println(a);
            if(c) {
                println("c is true");
                if(a) {
                    println("a is 1");
                } else {
                    println("a is not 1");
                }
            } else {
                println("c is false");
            }
        } else {
            println("b is false");
        }
        """.trimIndent()
    val printScriptLineReader = PrintScriptLineReader()
    val lines = printScriptLineReader.readLinesFromString(input)
    performFromLines(lines)
//    println("\nReading from file\n")
//    val fileLines = printScriptLineReader.readLinesFromFile("app/src/main/resources/script_example.txt")
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
        val ast = parser.parse(tokens, index)
        println("\nParser Output")
        println("$ast\n")
        if (ast is ASTBuilderSuccess) {
            try {
                interpreter = interpreter.interpret(ast.astNode)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}

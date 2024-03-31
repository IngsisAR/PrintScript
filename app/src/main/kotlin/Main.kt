import astbuilder.ASTBuilderSuccess
import utils.PrintScriptLineReader

fun main() {
    println("\nReading from string\n")
    val input =
        """
        function(1 + 2) * a / (4 + 5);
        """.trimIndent()
    val printScriptLineReader = PrintScriptLineReader()
//    val lines = printScriptLineReader.readLinesFromString(input)
//    performFromLines(lines)
    println("\nReading from file\n")
    val fileLines = printScriptLineReader.readLinesFromFile("app/src/main/resources/script_example.txt")
    performFromLines(fileLines)
}

private fun performFromLines(fileLines: List<String>) {
    var interpreter = InterpreterImpl()
    for (line in fileLines) {
        val lexer = Lexer(line)
        val tokens = lexer.tokenize()
        tokens.forEach { println(it) }
        val parser = Parser()
        val ast = parser.parse(tokens)
        println(ast)
        if (ast is ASTBuilderSuccess) {
            interpreter = interpreter.interpret(ast.astNode)
        }
    }
}

fun main() {
    println("\nReading from string\n")
    val input =
        """
        let a: number = 5, b: number = 10, c:number = 15;
        print(a + b + c);
        """.trimIndent()
    val printScriptLineReader = PrintScriptLineReader()
    val lines = printScriptLineReader.readLinesFromString(input)
    performFromLines(lines)
    println("\nReading from file\n")
    val fileLines = printScriptLineReader.readLinesFromFile("app/src/main/resources/script_example.txt")
    performFromLines(fileLines)
}

private fun performFromLines(fileLines: List<String>) {
    for (line in fileLines) {
        val lexer = Lexer(line)
        val tokens = lexer.tokenize()
        tokens.forEach { println(it) }
        val parser = Parser()
        val ast = parser.parse(tokens)
        println(ast)
    }
}

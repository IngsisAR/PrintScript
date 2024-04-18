import astbuilder.ASTBuilderFailure
import astbuilder.ASTBuilderResult
import astbuilder.ASTBuilderSuccess
import formatter.FormatterImpl
import java.io.File
import kotlin.system.exitProcess

private const val SCA_CONFIG_PATH = "sca/src/main/resources/SCAConfig.json"
private const val FORMAT_CONFIG_PATH = "formatter/src/main/resources/FormatterConfig.json"
private const val TOKEN_REGEX = "utils/src/main/resources/tokenRegex.json"
fun main() {
    println("""   ____       _       _   ____            _       _     """)
    println("""  |  _ \ _ __(_)_ __ | |_/ ___|  ___ _ __(_)_ __ | |_   """)
    println("""  | |_) | '__| | '_ \| __\___ \ / __| '__| | '_ \| __|  """)
    println("""  |  __/| |  | | | | | |_ ___) | (__| |  | | |_) | |_   """)
    println("""  |_|   |_|  |_|_| |_|\__|____/ \___|_|  |_| .__/ \__|  """)
    println("""                                           |_|      1.0 """)
    print("""File path: """)
    val path = readlnOrNull() ?: throw IllegalArgumentException("Script needed")
    val script = getFile(path)
    handleCommand(script, path)
}

private fun handleCommand(
    script: List<String>,
    path: String,
) {
    println("Choose an option for that file:")
    println("       1. Validation")
    println("       2. Execution")
    println("       3. Formatting")
    println("       4. Analyzing")
    println("       5. Exit")
    val number = readlnOrNull() ?: throw IllegalArgumentException("Choose a valid option ")
    when (number) {
        "1" -> validate(script)
        "2" -> execute(script)
        "3" -> format(script, path)
        "4" -> analyze(script)
        "5" -> exitProcess(0)
        else -> println("Invalid function specified - use 'analyze', 'format', 'execute' or 'validate'")
    }
}

private fun validate(fileLines: List<String>) {
    for ((index, line) in fileLines.withIndex()) {
        val lexer = Lexer(line, 0, TOKEN_REGEX)
        val tokens = lexer.tokenize()
        val parser = Parser()
        when (val ast = parser.parse(tokens, index)) {
            is ASTBuilderSuccess -> printGreen("\rProgress: ${functionProgress(fileLines.size, index)}%\r")
            is ASTBuilderFailure -> return printRed(ast.errorMessage)
        }
    }
    printGreen("✓ File validated successfully")
}

private fun execute(fileLines: List<String>) {
    var interpreter = InterpreterImpl()

    for ((index, line) in fileLines.withIndex()) {
        val ast = processLine(line, index)
        if (ast is ASTBuilderSuccess) {
            try {
                interpreter = interpreter.interpret(ast.astNode)
            } catch (e: Exception) {
                println(e.message)
            }
        }
        printGreen("\rProgress: ${functionProgress(fileLines.size, index)}%\r")
    }
}

private fun format(
    fileLines: List<String>,
    path: String,
) {
    val originalFile = File(path)
    val directoryPath = originalFile.parent
    val newFileName = originalFile.nameWithoutExtension + "_formatted.txt"
    val newFilePath = "$directoryPath/$newFileName"

    print("Config file path: ")
    val formatConfig = readlnOrNull()
    val configFile = formatConfig?.takeIf { it.isNotEmpty() } ?: FORMAT_CONFIG_PATH

    val formatter = FormatterImpl(configFile)
    val formattedContent: StringBuilder = StringBuilder()

    for ((index, line) in fileLines.withIndex()) {
        val ast = processLine(line, index)
        if (ast is ASTBuilderSuccess) {
            try {
                formattedContent.append(formatter.format(ast.astNode))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        printGreen("\rProgress: ${functionProgress(fileLines.size, index)}%\r")
    }
    createFormattedFile(newFilePath, formattedContent.toString())
}

private fun analyze(fileLines: List<String>) {
    print("Config file path: ")
    val scaConfig = readlnOrNull()
    val configFile = if (scaConfig.isNullOrEmpty()) SCA_CONFIG_PATH else scaConfig
    val sca = StaticCodeAnalyzer(configFile)

    for ((index, line) in fileLines.withIndex()) {
        val ast = processLine(line, index)
        if (ast is ASTBuilderSuccess) {
            val response: String = sca.analyze(ast.astNode, index)
            if (response.isNotEmpty()) {
                return printRed(response)
            }
            printGreen("\rProgress: ${functionProgress(fileLines.size, index)}%\r")
        }
    }
    printGreen("✓ File analyzed successfully")
}

private fun functionProgress(
    totalLines: Int,
    index: Int,
): Double {
    return (index + 1).toDouble() / totalLines * 100
}
private fun processLine(
    line: String,
    index: Int,
): ASTBuilderResult {
    val lexer = Lexer(line, 0, TOKEN_REGEX)
    val tokens = lexer.tokenize()
    val parser = Parser()
    return parser.parse(tokens, index)
}

private fun createFormattedFile(
    filePath: String,
    content: String,
) {
    val file = File(filePath)
    file.writeText(content)
    printGreen("✓ Formatted file created at: $filePath")
}

private fun getFile(path: String): List<String> {
    val printScriptLineReader = PrintScriptLineReader()
    return printScriptLineReader.readLinesFromFile(path)
}

private fun printRed(text: String) {
    println("\r\u001B[31m${text}\u001B[0m")
}

private fun printGreen(text: String) {
    println("\r\u001B[32m${text}\u001B[0m")
}

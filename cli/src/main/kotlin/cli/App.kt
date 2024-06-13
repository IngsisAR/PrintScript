package cli

import astbuilder.ASTBuilderFailure
import astbuilder.ASTBuilderResult
import astbuilder.ASTBuilderSuccess
import astbuilder.ASTProviderFactory
import formatter.FormatterImpl
import interpreter.InterpreterImpl
import lexer.Lexer
import parser.Parser
import sca.StaticCodeAnalyzer
import utils.ASTNode
import utils.PrintScriptChunkReader
import utils.VersionChecker
import java.io.File
import kotlin.math.roundToInt
import kotlin.system.exitProcess

private const val SCA_CONFIG_PATH = "sca/src/main/resources/SCAConfig.json"
private const val FORMAT_CONFIG_PATH = "formatter/src/main/resources/FormatterConfig.json"
private const val TOKEN_REGEX = "utils/src/main/resources/tokenRegex1.1.json"
private var version = "1.1.0"

fun main() {
    print("""Version (latest as default): """)
    version = readln()
    version = validateVersion(version)
    println("""   ____       _       _   ____            _       _     """)
    println("""  |  _ \ _ __(_)_ __ | |_/ ___|  ___ _ __(_)_ __ | |_   """)
    println("""  | |_) | '__| | '_ \| __\___ \ / __| '__| | '_ \| __|  """)
    println("""  |  __/| |  | | | | | |_ ___) | (__| |  | | |_) | |_   """)
    println("""  |_|   |_|  |_|_| |_|\__|____/ \___|_|  |_| .__/ \__|  """)
    println("""                                           |_|      $version """)
    print("""File path: """)
    val path = readlnOrNull() ?: throw IllegalArgumentException("Script needed")
    val script = getFile(path)
    handleCommand(script, path)
}

private fun validateVersion(version: String): String {
    var versionCheck = if (version == "") "1.1.0" else version
    val versionChecker = VersionChecker()
    while (!versionChecker.versionIsValid(versionCheck)) {
        printlnRed("Invalid version. Please enter a valid version: ")
        versionCheck = readln()
    }
    return versionCheck
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
    handleCommand(script, path)
}

private fun validate(fileChunks: List<String>): List<ASTNode> {
    val successfulASTs = mutableListOf<ASTNode>()
    var chunkStartLine = 1
    for ((index, chunk) in fileChunks.withIndex()) {
        val lexer = Lexer(chunk, chunkStartLine, TOKEN_REGEX)
        val tokens = lexer.tokenize()
        chunkStartLine = lexer.getCurrentLineIndex() + 1
        val parser = Parser()
        when (val ast = parser.parse(ASTProviderFactory(tokens, version))) {
            is ASTBuilderSuccess -> {
                successfulASTs.add(ast.astNode)
                printlnGreen("\rProgress: ${functionProgress(fileChunks.size, index)}%\r")
            }
            is ASTBuilderFailure -> {
                if (ast.errorMessage == "Empty tokens") continue
                printlnRed(ast.errorMessage)
                return emptyList()
            }
        }
    }
    printlnGreen("✓ File validated successfully")
    return successfulASTs
}

private fun execute(fileLines: List<String>) {
    var interpreter = InterpreterImpl(version = version)
    val astNodes = validate(fileLines)
    if (astNodes.isEmpty()) return
    for (ast in astNodes) {
        try {
            interpreter = interpreter.interpret(ast)
        } catch (e: Exception) {
            println(e.message)
            return
        }
    }
    printlnGreen("✓ File executed successfully")
}

// Debe usar validate o bien corregir processChunk() y utilizarlo en validate
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

    val formatter = FormatterImpl()
    val formattedContent: StringBuilder = StringBuilder()

    for ((index, line) in fileLines.withIndex()) {
        val ast = processChunk(line)
        if (ast is ASTBuilderSuccess) {
            try {
                formattedContent.append(formatter.format(ast.astNode, configFile, version))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        printlnGreen("\rProgress: ${functionProgress(fileLines.size, index)}%\r")
    }
    createFormattedFile(newFilePath, formattedContent.toString())
}

private fun analyze(fileChunks: List<String>) {
    print("Config file path: ")
    val scaConfig = readlnOrNull()
    val configFile = if (scaConfig.isNullOrEmpty()) SCA_CONFIG_PATH else scaConfig
    val sca = StaticCodeAnalyzer()

    for ((index, line) in fileChunks.withIndex()) {
        val ast = processChunk(line)
        if (ast is ASTBuilderSuccess) {
            val response: String = sca.analyze(ast.astNode, configFile, version)
            if (response.isNotEmpty()) {
                return printlnRed(response)
            }
            printlnGreen("\rProgress: ${functionProgress(fileChunks.size, index)}%\r")
        }
    }
    printlnGreen("✓ File analyzed successfully")
}

private fun functionProgress(
    totalLines: Int,
    index: Int,
): Int {
    return ((index + 1).toDouble() / totalLines * 100).roundToInt()
}
private fun processChunk(line: String): ASTBuilderResult {
    val lexer = Lexer(line, 0, TOKEN_REGEX)
    val tokens = lexer.tokenize()
    val parser = Parser()
    return parser.parse(ASTProviderFactory(tokens, version))
}

private fun createFormattedFile(
    filePath: String,
    content: String,
) {
    val file = File(filePath)
    file.writeText(content)
    printlnGreen("✓ Formatted file created at: $filePath")
}

private fun getFile(path: String): List<String> {
    val printScriptChunkReader = PrintScriptChunkReader()
    return printScriptChunkReader.readChunksFromFile(path)
}

private fun printlnRed(text: String) {
    println("\r\u001B[31m${text}\u001B[0m")
}

private fun printlnGreen(text: String) {
    println("\r\u001B[32m${text}\u001B[0m")
}

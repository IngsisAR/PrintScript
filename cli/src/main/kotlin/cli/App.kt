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
import utils.PrintOutputProvider
import utils.PrintScriptChunkReader
import utils.SystemInputProvider
import utils.VersionChecker
import java.io.File
import kotlin.math.roundToInt
import kotlin.system.exitProcess

private const val SCA_CONFIG_PATH = "sca/src/main/resources/SCAConfig.json"
private const val FORMAT_CONFIG_PATH = "formatter/src/main/resources/FormatterConfig.json"
private var version = "1.1.0"
private var chunkStartLine = 1

fun main() {
    print("""Version (latest as default): """)
    version = readln()
    version = validateVersion(version)
    val regexPath = getTokenRegex(version)
    println("""   ____       _       _   ____            _       _     """)
    println("""  |  _ \ _ __(_)_ __ | |_/ ___|  ___ _ __(_)_ __ | |_   """)
    println("""  | |_) | '__| | '_ \| __\___ \ / __| '__| | '_ \| __|  """)
    println("""  |  __/| |  | | | | | |_ ___) | (__| |  | | |_) | |_   """)
    println("""  |_|   |_|  |_|_| |_|\__|____/ \___|_|  |_| .__/ \__|  """)
    println("""                                           |_|      $version """)
    print("""File path (or enter to use example): """)
    var path = readlnOrNull()
    if (path.isNullOrEmpty()) {
        path = "cli/src/main/resources/script_example.txt"
    }
    handleCommand(path, regexPath)
}

private fun validateVersion(version: String): String {
    var versionCheck = if (version == "") "1.1" else version
    val versionChecker = VersionChecker()
    while (!versionChecker.versionIsValid(versionCheck)) {
        printlnRed("Invalid version. Please enter a valid version: ")
        versionCheck = readln()
    }
    return versionCheck
}

private fun handleCommand(
    path: String,
    regexPath: String,
) {
    chunkStartLine = 1
    val script = getFile(path)
    println("Choose an option for that file:")
    println("       1. Validation")
    println("       2. Execution")
    println("       3. Formatting")
    println("       4. Analyzing")
    println("       5. Exit")
    val number = readlnOrNull() ?: throw IllegalArgumentException("Choose a valid option ")
    when (number) {
        "1" -> validate(script, regexPath)
        "2" -> {
            var interpreter =
                InterpreterImpl(version = version, outputProvider = PrintOutputProvider(), inputProvider = SystemInputProvider())
            for (chunk in script) {
                when (val ast = validateChunk(chunk, regexPath)) {
                    is ASTBuilderFailure -> {
                        if (ast.errorMessage == "Empty tokens") continue
                        printlnRed(ast.errorMessage)
                        handleCommand(path, regexPath)
                        return
                    }
                    is ASTBuilderSuccess -> {
                        val executeResult = executeASTNode(ast.astNode, interpreter)
                        if (executeResult == null) {
                            handleCommand(path, regexPath)
                            return
                        }
                        interpreter = executeResult
                    }
                }
            }
            printlnGreen("✓ File executed successfully")
        }
        "3" -> format(script, path, regexPath)
        "4" -> analyze(script, regexPath)
        "5" -> exitProcess(0)
        else -> println("Invalid function specified - use 'analyze', 'format', 'execute' or 'validate'")
    }
    handleCommand(path, regexPath)
}

private fun validate(
    script: List<String>,
    regexPath: String,
) {
    for ((index, chunk) in script.withIndex()) {
        when (val ast = validateChunk(chunk, regexPath)) {
            is ASTBuilderSuccess -> printlnGreen("\rProgress: ${functionProgress(script.size, index)}%\r")
            is ASTBuilderFailure -> {
                if (ast.errorMessage == "Empty tokens") continue
                printlnRed(ast.errorMessage)
                return
            }
        }
    }
    printlnGreen("✓ File validated successfully")
}

private fun validateChunk(
    chunk: String,
    regexPath: String,
): ASTBuilderResult {
    val lexer = Lexer(chunk, chunkStartLine, regexPath)
    val tokens = lexer.tokenize()
    chunkStartLine = lexer.getCurrentLineIndex() + 1
    val parser = Parser()
    return parser.parse(ASTProviderFactory(tokens, version))
}

private fun executeASTNode(
    ast: ASTNode,
    interpreter: InterpreterImpl,
): InterpreterImpl? {
    try {
        return interpreter.interpret(ast)
    } catch (e: Exception) {
        println(e.message)
        return null
    }
}

private fun format(
    fileChunks: List<String>,
    path: String,
    regexPath: String,
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

    for ((index, chunk) in fileChunks.withIndex()) {
        when (val ast = validateChunk(chunk, regexPath)) {
            is ASTBuilderSuccess -> {
                try {
                    formattedContent.append(formatter.format(ast.astNode, configFile, version))
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            is ASTBuilderFailure -> {
                if (ast.errorMessage == "Empty tokens") continue
                printlnRed(ast.errorMessage)
                return
            }
        }
        printlnGreen("\rProgress: ${functionProgress(fileChunks.size, index)}%\r")
    }
    createFormattedFile(newFilePath, formattedContent.toString())
}

private fun analyze(
    fileChunks: List<String>,
    regexPath: String,
) {
    print("Config file path: ")
    val scaConfig = readlnOrNull()
    val configFile = if (scaConfig.isNullOrEmpty()) SCA_CONFIG_PATH else scaConfig
    val sca = StaticCodeAnalyzer()

    for ((index, line) in fileChunks.withIndex()) {
        when (val ast = validateChunk(line, regexPath)) {
            is ASTBuilderSuccess -> {
                val response: String = sca.analyze(ast.astNode, configFile, version)
                if (response.isNotEmpty()) {
                    return printlnRed(response)
                }
                printlnGreen("\rProgress: ${functionProgress(fileChunks.size, index)}%\r")
            }
            is ASTBuilderFailure -> {
                if (ast.errorMessage == "Empty tokens") continue
                printlnRed(ast.errorMessage)
                return
            }
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

private fun getTokenRegex(version: String): String {
    return "utils/src/main/resources/tokenRegex$version.json"
}

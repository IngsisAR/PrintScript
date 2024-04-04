import java.lang.IllegalArgumentException
import java.util.Locale
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("""+--------------------------------------------------------+""")
    println("""|   ____       _       _   ____            _       _     |""")
    println("""|  |  _ \ _ __(_)_ __ | |_/ ___|  ___ _ __(_)_ __ | |_   |""")
    println("""|  | |_) | '__| | '_ \| __\___ \ / __| '__| | '_ \| __|  |""")
    println("""|  |  __/| |  | | | | | |_ ___) | (__| |  | | |_) | |_   |""")
    println("""|  |_|   |_|  |_|_| |_|\__|____/ \___|_|  |_| .__/ \__|  |""")
    println("""|                                           |_|      1.0 |""")
    println("""+--------------------------------------------------------+""")
    if (args.isEmpty()) {
        println("No function or source file was specified")
        exitProcess(0)
    }

    try {
        when (args[0].lowercase(Locale.getDefault())) {
            "analyze" -> println("analyze")
            "format" -> println("format")
            "execute" -> println("execute")
            "validate" -> println("validate")
            "help" -> printHelpMessage()
            else -> throw IllegalArgumentException("Invalid function specified - use 'analyze' , 'format', 'execute' or 'validate'")
        }
    } catch (exception: Exception) {
        println("\u001B[31m${exception.message}\u001B[0m")
    }
}

private fun printHelpMessage() {
    println("""+-------------------------------------+""")
    println("-analyze [source-file] [config-file]")
    println("-format [source-file] [config-file]")
    println("-execute [source-file] ")
    println("-validate [source-file]")
    println("""+-------------------------------------+""")
}

import java.io.File

class PrintScriptLineReader {
    fun readLinesFromString(string: String): List<String> {
        // split by semicolon but don't drop it
        return string
            .split("(?<=;)".toRegex())
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    fun readLinesFromFile(fileName: String): List<String> = readLinesFromString(File(fileName).readText())
}

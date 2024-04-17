import java.io.File

class PrintScriptLineReader {
    fun readLinesFromString(string: String): List<String> {
        val lines = mutableListOf<String>()
        var currentLine = ""
        var braceCount = 0
        var elseCount = 0

        for ((index, char) in string.withIndex()) {
            when (char) {
                '{' -> {
                    braceCount++
                    currentLine += char
                }

                '}' -> {
                    braceCount--
                    currentLine += char
                    if (braceCount == 0) {
                        val elseIsNext = string.substring(index + 1).replace('}', ' ').trim().startsWith("else")
                        if (elseIsNext && elseCount == 0) {
                            elseCount++
                        } else if (!elseIsNext || elseCount > 0) {
                            lines.add(currentLine.trim())
                            currentLine = ""
                        }
                    }
                }

                ';' -> {
                    if (braceCount == 0) {
                        // Solo consideramos ; como el final de una línea si no estamos dentro de un bloque
                        currentLine += char
                        lines.add(currentLine.trim())
                        currentLine = ""
                    } else {
                        currentLine += char
                    }
                }

                else -> {
                    currentLine += char
                    if (currentLine.toByteArray().size > 8192) {
                        break
                    }
                }
            }
        }

        // Agrega cualquier línea restante
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.trim())
        }

        return lines
    }

    fun readLinesFromFile(fileName: String): List<String> = readLinesFromString(File(fileName).readText())
}

fun main() {
    println("\nReading from string\n")
    val input =
        """
        if(a){
            if(n){
                a = 2;

        else {
            b = 3;
        }
        let a = 2;
        let b = 2;
        """.trimIndent()
    val printScriptLineReader = PrintScriptLineReader()
    val lines = printScriptLineReader.readLinesFromString(input)
    lines.forEach { println("$it\n----------------------") }
}

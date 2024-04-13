package utils

import java.io.File

class PrintScriptLineReader {
    fun readLinesFromString(string: String): List<String> {
        val lines = mutableListOf<String>()
        var currentLine = ""
        var braceCount = 0

        string.forEach { char ->
            when (char) {
                '{' -> {
                    braceCount++
                    currentLine += char
                }
                '}' -> {
                    braceCount--
                    currentLine += char
                    if (braceCount == 0) {
                        // If we're at the top level, consider this the end of a block
                        lines.add(currentLine.trim())
                        currentLine = ""
                    }
                }
                ';' -> {
                    if (braceCount == 0) {
                        // Only consider ; as the end of a line if we're not inside a block
                        currentLine += char
                        lines.add(currentLine.trim())
                        currentLine = ""
                    } else {
                        currentLine += char
                    }
                }
                else -> currentLine += char
            }
        }

        // Add any remaining line
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.trim())
        }

        return lines
    }

    fun readLinesFromFile(fileName: String): List<String> = readLinesFromString(File(fileName).readText())
}

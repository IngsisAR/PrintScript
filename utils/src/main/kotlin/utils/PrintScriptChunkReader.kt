package utils

import java.io.File

class PrintScriptChunkReader {
    fun readChunksFromString(string: String): List<String> {
        val lines = mutableListOf<String>()
        var currentLine = ""
        var braceCount = 0
        var elseCount = 0
        // Normaliza los saltos de línea al leer el archivo
        val normalizedString = string.replace("\r\n", "\n").replace('\r', '\n')

        for ((index, char) in normalizedString.withIndex()) {
            when (char) {
                '{' -> {
                    braceCount++
                    currentLine += char
                }

                '}' -> {
                    braceCount--
                    currentLine += char
                    if (braceCount == 0) {
                        val elseIsNext =
                            normalizedString.substring(index + 1).replace('}', ' ')
                                .trim().startsWith("else")
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

                '\n' -> {
                    if (braceCount == 0 && normalizedString[index - 1] != ';' &&
                        normalizedString[index - 1] != '{' && normalizedString[index - 1] != '}'
                    ) {
                        lines.add(currentLine.trim())
                    } else if (braceCount != 0) {
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
        if (currentLine.trim().isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }

    fun readChunksFromFile(fileName: String): List<String> = readChunksFromString(File(fileName).readText())
}

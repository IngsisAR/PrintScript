package utils

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class ChunkKeywordRegex(
    @JsonProperty("declarators") val declarators: List<String>,
    @JsonProperty("followUpConditional") val followUpConditional: String,
    @JsonProperty("startConditional") val startConditional: String,
    @JsonProperty("identifier") val identifier: String,
)

class PrintScriptChunkReader {
    private val chunkKeywordsJsonPath = "utils/src/main/resources/ChunkKeywordsRegex.json"
    private val chunkKeywordsRegex: ChunkKeywordRegex = jacksonObjectMapper().readValue(File(chunkKeywordsJsonPath))

    fun readChunksFromString(string: String): List<String> {
        val chunks = mutableListOf<String>()
        var currentChunk = ""
        var braceCount = 0
        var elseCount = 0
        // Normaliza los saltos de línea al leer el archivo
        val normalizedString = string.replace("\r\n", "\n").replace('\r', '\n')

        for ((index, char) in normalizedString.withIndex()) {
            when (char) {
                '{' -> {
                    braceCount++
                    currentChunk += char
                }

                '}' -> {
                    braceCount--
                    currentChunk += char
                    if (braceCount == 0) {
                        val elseIsNext =
                            normalizedString.substring(index + 1).replace('}', ' ')
                                .trim().startsWith(chunkKeywordsRegex.followUpConditional)
                        if (elseIsNext && elseCount == 0) {
                            elseCount++
                        } else if (!elseIsNext || elseCount > 0) {
                            chunks.add(currentChunk.trim())
                            currentChunk = ""
                            elseCount = 0
                        }
                    }
                }

                ';' -> {
                    if (braceCount == 0) {
                        // Solo consideramos ; como el final de una línea si no estamos dentro de un bloque
                        currentChunk += char
                        chunks.add(currentChunk.trim())
                        currentChunk = ""
                    } else {
                        currentChunk += char
                    }
                }

                '\n' -> {
                    val nextIsKeyword = checkNextIsKeyword(normalizedString.substring(index + 1))
                    if (braceCount == 0 && currentChunk.isNotEmpty() && nextIsKeyword) {
                        chunks.add(currentChunk.trim())
                        currentChunk = ""
                    } else if (currentChunk.isEmpty()) {
                        continue
                    } else {
                        currentChunk += char
                    }
                }

                else -> {
                    currentChunk += char
                    if (currentChunk.toByteArray().size > 8192) {
                        break
                    }
                }
            }
        }

        // Agrega cualquier línea restante
        if (currentChunk.trim().isNotEmpty()) {
            chunks.add(currentChunk)
        }

        return chunks
    }

    fun readChunksFromFile(fileName: String): List<String> = readChunksFromString(File(fileName).readText())

    private fun checkNextIsKeyword(input: String): Boolean {
        val allKeywords =
            chunkKeywordsRegex.declarators +
                listOf(chunkKeywordsRegex.followUpConditional, chunkKeywordsRegex.startConditional, chunkKeywordsRegex.identifier)

        // Combine all regexes into one pattern
        val combinedRegex = allKeywords.joinToString(separator = "|").toRegex()
        val matchResult = combinedRegex.find(input.trimStart())

        // Check if there is a match at the beginning of the input
        return matchResult?.range?.first == 0
    }
}

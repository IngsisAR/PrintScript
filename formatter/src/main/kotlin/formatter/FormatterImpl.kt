package formatter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import utils.ASTNode
import java.io.File

class FormatterImpl {
    fun format(
        astNode: ASTNode,
        jsonPath: String,
        version: String,
    ): String {
        val mapper = jacksonObjectMapper()
        val configMap: Map<String, Any?> = mapper.readValue(File(jsonPath).readText())
        return StatementFormatter().format(astNode, configMap, version)
    }
}

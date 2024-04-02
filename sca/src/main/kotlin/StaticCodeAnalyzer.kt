import analyzers.CallExpressionAnalyzer
import analyzers.ExpressionStatementAnalyzer
import analyzers.IdentifierAnalyzer
import analyzers.VariableDeclarationAnalyzer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class StaticCodeAnalyzer(
    scaConfigJsonPath: String,
) {
    private val mapper = jacksonObjectMapper()
    private val configMap: Map<String, Any?> = mapper.readValue(File(scaConfigJsonPath).readText())

    fun analyze(
        astNode: ASTNode,
        lineIndex: Int,
    ): String =
        when (astNode) {
            is VariableDeclaration -> VariableDeclarationAnalyzer().analyze(astNode, configMap, lineIndex)
            is ExpressionStatement -> ExpressionStatementAnalyzer().analyze(astNode, configMap, lineIndex)
            is CallExpression -> CallExpressionAnalyzer().analyze(astNode, configMap, lineIndex)
            is Identifier -> IdentifierAnalyzer().analyze(astNode, configMap, lineIndex)
            else -> ""
        }
}

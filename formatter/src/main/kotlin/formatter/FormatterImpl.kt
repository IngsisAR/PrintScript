package formatter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import utils.ASTNode
import utils.AssignmentExpression
import utils.BinaryExpression
import utils.CallExpression
import utils.ConditionalStatement
import utils.ExpressionStatement
import utils.VariableDeclaration
import utils.VariableDeclarator
import utils.VersionChecker
import java.io.File

class FormatterImpl {
    fun format(
        astNode: ASTNode,
        jsonPath: String,
        version: String,
    ): String {
        val mapper = jacksonObjectMapper()
        val configMap: Map<String, Any?> = mapper.readValue(File(jsonPath).readText())
        return when (astNode) {
            is VariableDeclaration -> VariableDeclarationFormatter().format(astNode, configMap)
            is VariableDeclarator -> VariableDeclaratorFormatter().format(astNode, configMap)
            is BinaryExpression -> BinaryExpressionFormatter().format(astNode, configMap)
            is AssignmentExpression -> AssignmentExpressionFormatter().format(astNode, configMap)
            is CallExpression -> CallExpressionFormatter().format(astNode, configMap)
            is ExpressionStatement -> ExpressionStatementFormatter().format(astNode, configMap)
            is ConditionalStatement -> {
                if (VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
                    ConditionalStatementFormatter().format(astNode, configMap)
                } else {
                    throw IllegalArgumentException("Unknown ASTNode type")
                }
            }
            else -> throw IllegalArgumentException("Unknown ASTNode type")
        }
    }
}

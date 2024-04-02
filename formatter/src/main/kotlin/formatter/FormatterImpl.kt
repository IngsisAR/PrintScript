package formatter

import ASTNode
import AssigmentExpression
import BinaryExpression
import CallExpression
import ExpressionStatement
import VariableDeclaration
import VariableDeclarator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class FormatterImpl(
    val jsonPath: String,
) {
    private val mapper = jacksonObjectMapper()
    private val configMap: Map<String, Any?> = mapper.readValue(File(jsonPath).readText())

    fun format(astNode: ASTNode): String =
        when (astNode) {
            is VariableDeclaration -> VariableDeclarationFormatter().format(astNode, configMap)
            is VariableDeclarator -> VariableDeclaratorFormatter().format(astNode, configMap)
            is BinaryExpression -> BinaryExpressionFormatter().format(astNode, configMap)
            is AssigmentExpression -> AssigmentExpressionFormatter().format(astNode, configMap)
            is CallExpression -> CallExpressionFormatter().format(astNode, configMap)
            is ExpressionStatement -> ExpressionStatementFormatter().format(astNode, configMap)
            else -> throw IllegalArgumentException("Unknown ASTNode type")
        }
}

package formatter

import utils.ASTNode
import utils.ConditionalStatement
import utils.ExpressionStatement
import utils.Statement
import utils.VariableDeclaration

/**
 * @author Agustin Augurusa
 */
class StatementFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as Statement
        return when (astNode) {
            is VariableDeclaration -> VariableDeclarationFormatter().format(astNode, configMap)
            is ConditionalStatement -> ConditionalStatementFormatter().format(astNode, configMap)
            is ExpressionStatement -> ExpressionStatementFormatter().format(astNode, configMap)
            else -> throw IllegalArgumentException("Unknown ASTNode type")
        }
    }
}

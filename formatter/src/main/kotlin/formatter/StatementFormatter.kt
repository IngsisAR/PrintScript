package formatter

import utils.ASTNode
import utils.ConditionalStatement
import utils.ExpressionStatement
import utils.Statement
import utils.VariableDeclaration
import utils.VersionChecker

/**
 * @author Agustin Augurusa
 */
class StatementFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is Statement) {
            "StatementFormatter can only format Statement nodes."
        }
        return when (astNode) {
            is VariableDeclaration -> VariableDeclarationFormatter().format(astNode, configMap, version)
            is ConditionalStatement -> {
                if (VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
                    ConditionalStatementFormatter().format(astNode, configMap, version)
                } else {
                    throw IllegalArgumentException("Unknown ASTNode type")
                }
            }
            is ExpressionStatement -> ExpressionStatementFormatter().format(astNode, configMap, version)
            else -> throw IllegalArgumentException("Unknown ASTNode type")
        }
    }
}

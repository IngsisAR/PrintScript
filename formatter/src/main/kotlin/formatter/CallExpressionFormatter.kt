package formatter

import utils.ASTNode
import utils.CallExpression

class CallExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        require(astNode is CallExpression) {
            "CallExpressionFormatter can only format CallExpression nodes."
        }
        return when (astNode.callee.name) {
            "println" -> formatPrintln(astNode, configMap, version)
            else ->
                astNode.callee.name + "(" +
                    astNode.arguments.joinToString(", ") {
                        ExpressionFormatter().format(
                            it,
                            configMap,
                            version,
                        )
                    } + ")"
        }
    }

    private fun formatPrintln(
        function: CallExpression,
        configMap: Map<String, Any?>,
        version: String,
    ): String {
        val lineJumpBeforePrintln = (configMap["lineJumpBeforePrintln"] as? Int)?.takeIf { it > 0 } ?: 0
        val lineJump =
            when (lineJumpBeforePrintln == 0) {
                false -> jumpLine(lineJumpBeforePrintln)
                true -> ""
            }
        return lineJump + function.callee.name + "(" +
            function.arguments.joinToString(", ") {
                ExpressionFormatter().format(
                    it,
                    configMap,
                    version,
                )
            } + ")"
    }

    private fun jumpLine(n: Int): String = "\n".repeat(n)
}

package formatter

import utils.ASTNode
import utils.CallExpression

class CallExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as CallExpression
        return when (astNode.callee.name) {
            "println" -> formatPrintln(astNode, configMap)
            else ->
                astNode.callee.name + "(" +
                    astNode.arguments.joinToString(", ") {
                        ExpressionFormatter().format(
                            it,
                            configMap,
                        )
                    } + ")"
        }
    }

    private fun formatPrintln(
        function: CallExpression,
        configMap: Map<String, Any?>,
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
                )
            } + ")"
    }

    private fun jumpLine(n: Int): String = "\n".repeat(n)
}

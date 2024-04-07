package formatter

import ASTNode
import CallExpression

class CallExpressionFormatter : Formatter {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as CallExpression
        val lineJumpBeforePrintln = (configMap["lineJumpBeforePrintln"] as? Int)?.takeIf { it > 0 } ?: 0
        val lineJump =
            when (lineJumpBeforePrintln == 0) {
                false -> jumpLine(lineJumpBeforePrintln)
                true -> ""
            }
        return lineJump + astNode.callee.name + "(" +
            astNode.arguments.joinToString(", ") {
                ExpressionFormatter().format(
                    it,
                    configMap,
                )
            } + ")"
    }

    private fun jumpLine(n: Int): String = "\n".repeat(n)
}

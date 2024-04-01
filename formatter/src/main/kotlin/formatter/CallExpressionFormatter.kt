package formatter

import ASTNode
import CallExpression

class CallExpressionFormatter : FormatterInterface {
    override fun format(astNode: ASTNode, configMap: Map<String, Any?>): String {
        astNode as CallExpression
        val lineJumpBeforePrintln = configMap["lineJumpBeforePrintln"]?.let { it as Int } ?: 0
        val lineJump = when (lineJumpBeforePrintln > 0) {
            true -> jumpLine(lineJumpBeforePrintln)
            false -> ""
        }
        return lineJump + astNode.callee.name + "(" + astNode.arguments.joinToString(", ") {
            ExpressionFormatter().format(
                it,
                configMap
            )
        } + ")"
    }

    private fun jumpLine(n: Int): String {
        return "\n".repeat(n)
    }
}

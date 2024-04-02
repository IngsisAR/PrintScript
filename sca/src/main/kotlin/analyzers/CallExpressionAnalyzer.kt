package analyzers

import ASTNode
import CallExpression
import Identifier
import NumberLiteral
import StringLiteral

class CallExpressionAnalyzer : Analyzer {
    override fun analyze(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
        lineIndex: Int,
    ): String {
        var result = ""
        if (astNode !is CallExpression) return result
        val callee = astNode.callee
        result += IdentifierAnalyzer().analyze(callee, configMap, lineIndex)
        val arguments = astNode.arguments
        if (callee.name == "println") {
            for (argument in arguments) {
                if (configMap["printlnNoExpressionArguments"] == true &&
                    argument !is StringLiteral &&
                    argument !is NumberLiteral &&
                    argument !is Identifier
                ) {
                    result += "No expressions in println function rule violated at (${lineIndex + 1}:${argument.start})\n"
                }
            }
        }
        for (argument in arguments) {
            result += ExpressionAnalyzer().analyze(argument, configMap, lineIndex)
        }
        return result
    }
}

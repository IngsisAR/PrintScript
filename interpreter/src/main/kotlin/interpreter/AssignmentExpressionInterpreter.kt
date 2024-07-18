package interpreter

import utils.ASTNode
import utils.AssignmentExpression
import utils.BinaryExpression
import utils.CallExpression
import utils.EnvironmentProvider
import utils.Identifier
import utils.InputProvider
import utils.Literal
import utils.OutputProvider
import utils.VersionChecker

class AssignmentExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
    private val environmentProvider: EnvironmentProvider,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is AssignmentExpression) { "Node must be an AssignmentExpression" }
        val id = node.left.name
        val variable =
            variableMap[id] ?: throw IllegalArgumentException(
                "Variable '$id' not found at (${node.left.line}:${node.left.start})",
            )
        require(variable.isMutable == true) { "Intended to assign to immutable variable '$id' at (${node.left.line}:${node.left.start})" }
        val newValue =
            when (val right = node.right) {
                is Literal -> right.value
                is BinaryExpression ->
                    BinaryExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                        right,
                    )
                is Identifier -> IdentifierInterpreter(variableMap, version).interpret(right)
                is CallExpression ->
                    CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider, environmentProvider).interpret(
                        right,
                    )
                else -> throw IllegalArgumentException(
                    "Unsupported right side of assignment expression: ${right::class.simpleName} at " +
                        "(${right.line}:${right.start})",
                )
            }
        checkTypeMatches(variable, newValue, node.right)
        val updatedMap = variableMap.toMutableMap()
        updatedMap[id] = variable.copy(value = newValue.toString())
        return updatedMap
    }

    private fun checkTypeMatches(
        variable: VariableInfo,
        newValue: Any?,
        assignedNode: ASTNode,
    ) {
        val expectedType =
            when {
                newValue is Number -> "number"
                newValue is String -> "string"
                newValue is Boolean && VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version) -> "boolean"
                newValue == null -> variable.type
                else -> throw IllegalArgumentException(
                    "Unsupported value type: ${newValue::class.simpleName} " +
                        "at (${assignedNode.line}:${assignedNode.start})",
                )
            }
        require(variable.type == expectedType) {
            "Type mismatch: expected ${variable.type}, got $expectedType at (${assignedNode.line}:${assignedNode.start})"
        }
    }
}

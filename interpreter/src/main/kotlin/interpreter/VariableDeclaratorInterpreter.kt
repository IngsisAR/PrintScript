package interpreter

import utils.ASTNode
import utils.BinaryExpression
import utils.CallExpression
import utils.Identifier
import utils.InputProvider
import utils.Literal
import utils.OutputProvider
import utils.VariableDeclarator

class VariableDeclaratorInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val kind: String,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is VariableDeclarator) { "Node must be a VariableDeclarator" }
        val id = node.id.name
        require(variableMap[id] == null) { "Variable '$id' already exists at (${node.id.line}:${node.id.start})" }

        val type = node.type.type
        val value =
            node.init?.let {
                when (it) {
                    is BinaryExpression -> BinaryExpressionInterpreter(variableMap, version).interpret(it)
                    is Identifier -> IdentifierInterpreter(variableMap, version).interpret(it)
                    is CallExpression -> CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider).interpret(it)
                    is Literal -> it.value
                    else -> throw IllegalArgumentException("Unsupported init type: ${it::class.simpleName} at (${it.line}:${it.start})")
                }
            }

        return mapOf(id to VariableInfo(type, value?.toString(), kind == "let"))
    }
}

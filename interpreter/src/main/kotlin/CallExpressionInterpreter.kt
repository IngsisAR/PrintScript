import inputter.ConsoleInputter
import outputter.ConsoleOutputter

class CallExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Any {
        require(node is CallExpression) { "Node must be an CallExpression" }
        return when (node.callee.name) {
            "println" -> ConsoleOutputter(variableMap, version).output(node)
            "readEnv" -> handleReadEnv(node.arguments[0] as StringLiteral)
            "readInput" -> handleReadInput(node.arguments[0] as ASTNode)
            else -> throw IllegalArgumentException("Function '${node.callee.name}' not found")
        }
    }

    private fun handleReadEnv(key: StringLiteral): String {
        val versionChecker = VersionChecker()
        if (versionChecker.versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
            return System.getenv(key.value)
        } else {
            throw UnsupportedOperationException("Operation 'readEnv' not supported in this version")
        }
    }

    private fun handleReadInput(inputNode: ASTNode): Any {
        val versionChecker = VersionChecker()
        if (versionChecker.versionIsSameOrOlderThanCurrentVersion("1.1.0", version)) {
            return ConsoleInputter(variableMap, version).readInput(inputNode)
        } else {
            throw UnsupportedOperationException("Operation 'readInput' not supported in this version")
        }
    }
}

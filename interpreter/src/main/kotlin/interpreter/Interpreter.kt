package interpreter

import utils.ASTNode

sealed interface Interpreter {
    fun interpret(node: ASTNode): Any?
}

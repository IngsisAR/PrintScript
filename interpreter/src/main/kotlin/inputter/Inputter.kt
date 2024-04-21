package inputter

import ASTNode

interface Inputter {
    fun readInput(node: ASTNode): Any
}

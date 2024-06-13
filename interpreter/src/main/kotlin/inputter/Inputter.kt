package inputter

import utils.ASTNode

interface Inputter {
    fun readInput(node: ASTNode): Any
}

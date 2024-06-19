package outputter

import utils.ASTNode

interface Outputter {
    fun output(node: ASTNode)
}

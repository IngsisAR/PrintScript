package astbuilder

import ASTNode

sealed interface ASTBuilder {
    fun verifyAndBuild(): ASTNode?
}

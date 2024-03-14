package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.ASTNode

sealed interface ASTBuilder {

    fun verifyAndBuild(): ASTNode?
}
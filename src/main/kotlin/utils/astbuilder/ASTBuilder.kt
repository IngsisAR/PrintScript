package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Token

sealed interface ASTBuilder {

    fun build(): ASTNode?
}
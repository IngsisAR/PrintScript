package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Token

/**
 * @author Agustin Augurusa
 */
sealed interface ASTBuilder {

    fun verify(): Boolean

    fun build(): ASTNode
}
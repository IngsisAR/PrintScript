package australfi.ingsis7.astbuilder

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Token

/**
 * @author Agustin Augurusa
 */
sealed interface ASTBuilder {

    fun verify(): Boolean

    fun isStructureValid(): Boolean

    fun build(): ASTNode
}
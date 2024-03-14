package australfi.ingsis7.utils.astbuilder

import australfi.ingsis7.utils.Token

abstract class AbstractASTBuilder(val tokens: List<Token>): ASTBuilder{
    protected open fun verify():Boolean{
        return true
    }
}
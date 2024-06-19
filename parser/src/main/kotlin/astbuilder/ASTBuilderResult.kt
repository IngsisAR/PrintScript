package astbuilder

import utils.ASTNode

sealed interface ASTBuilderResult

data class ASTBuilderSuccess(
    val astNode: ASTNode,
) : ASTBuilderResult

data class ASTBuilderFailure(
    val errorMessage: String,
) : ASTBuilderResult

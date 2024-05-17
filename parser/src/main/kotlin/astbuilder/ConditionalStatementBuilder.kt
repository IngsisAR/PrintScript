package astbuilder

import ConditionalStatement
import Identifier
import Statement
import Token

class ConditionalStatementBuilder(
    val tokens: List<Token>,
    private val astProviderFactory: ASTProviderFactory,
) : ASTBuilder {
    private lateinit var test: Identifier

    override fun verifyAndBuild(): ASTBuilderResult {
        if (tokens.isEmpty()) {
            return ASTBuilderFailure("Not enough tokens to build conditional expression")
        }
        if (tokens[0].type != "IF") {
            return ASTBuilderFailure("Invalid conditional expression")
        }
        if (tokens.size > 1) {
            if (tokens[1].type != "OPAREN") {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected '(' after 'if' at (${tokens[0].position.line}:${tokens[0].position.end})",
                )
            }
            if (tokens.size == 2) {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected identifier after '(' " +
                        "at (${tokens[1].position.line}:${tokens[1].position.end})",
                )
            }
            val identifierResult = IdentifierBuilder(tokens.subList(2, 3)).verifyAndBuild()
            if (identifierResult is ASTBuilderFailure) {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected identifier after '(' " +
                        "at (${tokens[1].position.line}:${tokens[1].position.end})",
                )
            }
            test = (identifierResult as ASTBuilderSuccess).astNode as Identifier

            if (tokens.size == 3) {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected ')' after identifier " +
                        "at (${tokens[2].position.line}:${tokens[2].position.end})",
                )
            }
            if (tokens[3].type != "CPAREN") {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected ')' after identifier " +
                        "at (${tokens[2].position.line}:${tokens[2].position.end})",
                )
            }
            if (tokens.size == 4) {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected '{' after ')' " +
                        "at ( ${tokens[3].position.line}:${tokens[3].position.end})",
                )
            }
            if (tokens[4].type != "OBRACE") {
                return ASTBuilderFailure(
                    "Invalid conditional expression: expected '{' after ')' " +
                        "at (${tokens[3].position.line}:${tokens[3].position.end})",
                )
            }
            if (tokens.size == 5) {
                return ASTBuilderFailure(
                    "Invalid conditional expression: unclosed block " +
                        "at (${tokens[4].position.line}:${tokens[4].position.end})",
                )
            }
            return buildIfAndElseBlocks(tokens.subList(4, tokens.size))
        }
        return ASTBuilderFailure("Incomplete conditional expression at (${tokens.last().position.line}:${tokens.last().position.end})")
    }

    private fun buildIfAndElseBlocks(tokens: List<Token>): ASTBuilderResult {
        val consequent = mutableListOf<Statement>()
        val alternate = mutableListOf<Statement>()
        var currentIndex = 0
        var newStatementIndex = 0
        var braceCount = 0
        var isConditionalStatement = false
        var isIfBlock = false
        var isElseBlock = false
        var elseCount = 0

        while (currentIndex < tokens.size) {
            val currentToken = tokens[currentIndex]

            when (currentToken.type) {
                "OBRACE" -> {
                    braceCount++
                    if (currentIndex == 0) {
                        isIfBlock = true
                        newStatementIndex++
                    }
                    if (isElseBlock) {
                        newStatementIndex = currentIndex + 1
                    }
                    currentIndex++
                }
                "CBRACE" -> {
                    braceCount--
                    if (braceCount == 0) {
                        // catch statements without semicolon
                        if (newStatementIndex != currentIndex) {
                            val builderResult =
                                addStatementToBlocksOrBuilderFailure(
                                    tokens,
                                    newStatementIndex,
                                    currentIndex - 1,
                                    isIfBlock,
                                    consequent,
                                    isElseBlock,
                                    alternate,
                                )
                            if (builderResult is ASTBuilderFailure) {
                                return builderResult
                            }
                        }
                        if (isIfBlock) {
                            isIfBlock = false
                            isElseBlock = true
                            currentIndex++
                            continue
                        }
                        if (isElseBlock) {
                            isElseBlock = false
                            currentIndex++
                            newStatementIndex = currentIndex + 1
                            continue
                        }
                    } else if (braceCount == 1 && isConditionalStatement) {
                        if (currentIndex + 1 < tokens.size && tokens[currentIndex + 1].type == "ELSE" && elseCount == 0) {
                            currentIndex++
                            elseCount++
                            continue
                        }
                        val builderResult =
                            addStatementToBlocksOrBuilderFailure(
                                tokens,
                                newStatementIndex,
                                currentIndex,
                                isIfBlock,
                                consequent,
                                isElseBlock,
                                alternate,
                            )
                        if (builderResult is ASTBuilderFailure) {
                            return builderResult
                        }
                        newStatementIndex = currentIndex + 1
                        isConditionalStatement = false

                        currentIndex++
                    } else if (braceCount >= 1) {
                        currentIndex++
                    } else {
                        return ASTBuilderFailure(
                            "Unmatched braces in expression at (${currentToken.position.line}:${currentToken.position.end})",
                        )
                    }
                }
                "IF" -> {
                    isConditionalStatement = true
                    currentIndex++
                }
                "ELSE" -> {
                    if (!isElseBlock && !isConditionalStatement) {
                        return ASTBuilderFailure("Unexpected 'else' at (${currentToken.position.line}:${currentToken.position.start})")
                    }
                    currentIndex++
                }
                "SEMICOLON" -> {
                    if (!isConditionalStatement) {
                        val builderResult =
                            addStatementToBlocksOrBuilderFailure(
                                tokens,
                                newStatementIndex,
                                currentIndex,
                                isIfBlock,
                                consequent,
                                isElseBlock,
                                alternate,
                            )
                        if (builderResult is ASTBuilderFailure) {
                            return builderResult
                        }
                        newStatementIndex = currentIndex + 1
                    }
                    currentIndex++
                }
                else -> currentIndex++
            }
        }

        // Verificamos si hay corchetes sin cerrar
        if (braceCount != 0) {
            return ASTBuilderFailure("Unmatched braces in expression at (${tokens.last().position.line}:${tokens.last().position.end})")
        }

        return ASTBuilderSuccess(
            ConditionalStatement(
                test,
                consequent,
                alternate,
                tokens.first().position.line,
                tokens.first().position.start,
                tokens.last().position.end,
            ),
        )
    }

    private fun addStatementToBlocksOrBuilderFailure(
        tokens: List<Token>,
        newStatementIndex: Int,
        currentIndex: Int,
        isIfBlock: Boolean,
        consequent: MutableList<Statement>,
        isElseBlock: Boolean,
        alternate: MutableList<Statement>,
    ): ASTBuilderResult {
        val statementTokens = tokens.subList(newStatementIndex, currentIndex + 1)
        val result = astProviderFactory.changeTokens(statementTokens).getProviderByType("statement").getASTBuilderResult()

        if (result is ASTBuilderFailure) {
            return result
        }

        val statement = (result as ASTBuilderSuccess).astNode as Statement

        if (isIfBlock) {
            consequent.add(statement)
        } else if (isElseBlock) {
            alternate.add(statement)
        }
        return result
    }
}

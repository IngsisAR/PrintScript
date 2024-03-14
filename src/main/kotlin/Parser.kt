package australfi.ingsis7

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Position
import australfi.ingsis7.utils.Token
import australfi.ingsis7.utils.astbuilder.StatementProvider


class Parser {
    fun parse(tokens : List<Token>): ASTNode?{
        val statementProvider = StatementProvider(tokens)
        return statementProvider.getVerifiedStatementOrNull()
    }
}

fun main() {
    val parser = Parser()
    val tokens = listOf(
        Token("ID", Position(0,0), "a"),
        Token("ASSIGN", Position(1,1), "="),
        Token("NUMBER", Position(2,2), "1"),
        Token("PLUS", Position(3,3), "+"),
        Token("NUMBER", Position(4,4), "1"),
        Token("SEMICOLON", Position(5,5), ";")
    )
    val ast = parser.parse(tokens)
    println(ast)
}
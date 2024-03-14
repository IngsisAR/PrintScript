package australfi.ingsis7

import australfi.ingsis7.utils.ASTNode
import australfi.ingsis7.utils.Token
import australfi.ingsis7.utils.astbuilder.StatementProvider


class Parser {
    fun parse(tokens : List<Token>): ASTNode?{
        val statementProvider = StatementProvider(tokens)
        return statementProvider.getVerifiedStatementOrNull()
    }
}

fun main() {
    val code = """
        let a:number= 1+5*2;
    """.trimIndent()
    val lexer = Lexer(code)
    val tokens = lexer.tokenize()
    tokens.forEach { print(it) }
    val parser = Parser()
    val ast = parser.parse(tokens)
    println(ast)
}
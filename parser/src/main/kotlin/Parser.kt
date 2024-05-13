import astbuilder.ASTBuilderResult
import astbuilder.ASTProviderFactory
import astbuilder.StatementProvider

class Parser {
    @Throws(IllegalArgumentException::class)
    fun parse(astProviderFactory: ASTProviderFactory): ASTBuilderResult {
        val statementProviderImpl = astProviderFactory.getProviderByType("statement") as StatementProvider
        return statementProviderImpl.getASTBuilderResult()
    }
}

fun main() {
    val tokens =
        listOf(
            Token(type = "LET", position = Position(line = 1, start = 0, end = 3,), value = "let"),
            Token(type = "ID", position = Position(line = 1, start = 4, end = 5,), value = "a"),
            Token(type = "COLON", position = Position(line = 1, start = 5, end = 6,), value = ":"),
            Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13,), value = "number"),
            Token(type = "SEMICOLON", position = Position(line = 1, start = 13, end = 14,), value = ";"),
            Token(type = "SEMICOLON", position = Position(line = 1, start = 14, end = 15,), value = ";"),
        )
    val astProviderFactory = ASTProviderFactory(tokens, "1.1.0")
    val parser = Parser()
    val result = parser.parse(astProviderFactory)
    println(result)
}

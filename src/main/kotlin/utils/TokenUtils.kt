package australfi.ingsis7.utils

data class Position(val start: Int, val end: Int)

data class Token(val type: String, val position: Position, val value: String? = null)
enum class TokenType {
    LET,
    CONST,
    PLUS,
    MUL,
    DIV,
    MINUS,
    MODULE,
    COLON,
    SEMICOLON,
    ASSIGN,
    CPAREN,
    OPAREN,
    CBRACE,
    OBRACE,
    COMMA,
    CBRACKET,
    OBRACKET,
    PROGRAM,
    TYPE,
    ID,
    NUMBER,
    STRING
}

val tokenReg = listOf(
    RegexToken("let", TokenType.LET),
    RegexToken("const", TokenType.CONST),
    RegexToken(":", TokenType.COLON),
    RegexToken(";", TokenType.SEMICOLON),
    RegexToken(",", TokenType.COMMA),
    RegexToken("\\+", TokenType.PLUS),
    RegexToken("-", TokenType.MINUS),
    RegexToken("\\*", TokenType.MUL),
    RegexToken("/", TokenType.DIV),
    RegexToken("%", TokenType.MODULE),
    RegexToken("=", TokenType.ASSIGN),
    RegexToken("\\(", TokenType.OPAREN),
    RegexToken("\\)", TokenType.CPAREN),
    RegexToken("\\{", TokenType.OBRACE),
    RegexToken("\\}", TokenType.CBRACE),
    RegexToken("\\[", TokenType.OBRACKET),
    RegexToken("\\]", TokenType.CBRACKET),
    RegexToken("program", TokenType.PROGRAM),
    RegexToken("number|string|float|bool", TokenType.TYPE),
    RegexToken("[a-zA-Z_][a-zA-Z0-9_]*", TokenType.ID),
    RegexToken("[0-9]+", TokenType.NUMBER),
    RegexToken("\"[^\"]*\"", TokenType.STRING)
)

data class RegexToken(val regex: String, val token: TokenType)
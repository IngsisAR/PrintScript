package australfi.ingsis7.utils

data class Position(val start: Int, val end: Int)

sealed class Token(open val position: Position)
data class LET(override val position: Position) : Token(position)
data class PRINTLN(override val position: Position) : Token(position)
data class COLON(override val position: Position) : Token(position)
data class SEMICOLON(override val position: Position) : Token(position)
data class BINARYOPERATION(val kind: String, override val position: Position) : Token(position)
data class ASSIGN(override val position: Position) : Token(position)
data class CPAREN(override val position: Position) : Token(position)
data class OPAREN(override val position: Position) : Token(position)
data class CBRACE(override val position: Position) : Token(position)
data class OBRACE(override val position: Position) : Token(position)
data class COMMA(override val position: Position) : Token(position)
data class CBRACKET(override val position: Position) : Token(position)
data class OBRACKET(override val position: Position) : Token(position)
data class PROGRAM(override val position: Position) : Token(position)
data class TYPE(override val position: Position) : Token(position)
data class ID(val name: String, override val position: Position) : Token(position)
data class NUMBER(val value: Number, override val position: Position) : Token(position)
data class STRING(val value: String, override val position: Position) : Token(position)
enum class TokenType{
    LET,
    PRINTLN,
    COLON,
    SEMICOLON,
    BINARYOPERATION,
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
    RegexToken("println", TokenType.PRINTLN),
    RegexToken(":", TokenType.COLON),
    RegexToken(";", TokenType.SEMICOLON),
    RegexToken(",", TokenType.COMMA),
    RegexToken("[-+*/]", TokenType.BINARYOPERATION),
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
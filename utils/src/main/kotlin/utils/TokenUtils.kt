package utils

data class Position(
    val line: Int,
    val start: Int,
    val end: Int,
)

data class Token(
    val type: String,
    val position: Position,
    val value: String,
)

data class TokenRegex(
    val token: String,
    val regex: String,
)

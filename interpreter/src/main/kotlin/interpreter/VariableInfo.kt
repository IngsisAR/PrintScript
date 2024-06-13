package interpreter

data class VariableInfo(
    val type: String,
    val value: String?,
    val isMutable: Boolean?,
)

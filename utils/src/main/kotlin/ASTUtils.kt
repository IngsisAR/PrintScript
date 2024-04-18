sealed interface ASTNode {
    val start: Int
    val end: Int
}

sealed interface Statement : ASTNode

sealed interface Expression : ASTNode

sealed interface Literal : Expression

// data class Program(
//    val body: List<Statement>,
//    val sourceType: String,
//    override val start: Int,
//    override val end: Int,
// ) : ASTNode

data class ExpressionStatement(
    val expression: Expression,
    override val start: Int,
    override val end: Int,
) : Statement

data class VariableDeclaration(
    val declarations: List<VariableDeclarator>,
    val kind: String,
    override val start: Int,
    override val end: Int,
) : Statement

data class AssignmentExpression(
    val left: Identifier,
    val right: Expression,
    override val start: Int,
    override val end: Int,
) : Expression

data class BinaryExpression(
    val left: Expression,
    val right: Expression,
    val operator: String,
    override val start: Int,
    override val end: Int,
) : Expression

data class CallExpression(
    val callee: Identifier,
    val arguments: List<Expression>,
    override val start: Int,
    override val end: Int,
) : Expression

data class NumberLiteral(
    val value: Number,
    override val start: Int,
    override val end: Int,
) : Literal

data class StringLiteral(
    val value: String,
    override val start: Int,
    override val end: Int,
) : Literal

data class Identifier(
    val name: String,
    override val start: Int,
    override val end: Int,
) : Expression

data class TypeReference(
    val type: String,
    override val start: Int,
    override val end: Int,
) : ASTNode

data class VariableDeclarator(
    val id: Identifier,
    val type: TypeReference,
    val init: Expression?,
    override val start: Int,
    override val end: Int,
) : ASTNode

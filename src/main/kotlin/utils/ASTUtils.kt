package australfi.ingsis7.utils
sealed interface ASTNode{
    val start: Int
    val end: Int
}

sealed interface Statements : ASTNode

sealed interface Expression: ASTNode

data class Program(val body: Statements,val sourceType: String, override val start: Int, override val end: Int) : ASTNode

data class AssigmentExpression(val left: Identifier, val right: Expression, override val start: Int, override val end: Int) : Expression

data class BinaryExpression(val left: Expression, val right: Expression, val operator: String, override val start: Int, override val end: Int) : Expression

data class CallExpression (val callee: Identifier, val arguments: SequenceExpression, override val start: Int, override val end: Int) : Expression

data class ExpressionStatement(val expression: Expression, override val start: Int, override val end: Int) : ASTNode

class Identifier(override val start: Int, override val end: Int) : ASTNode

data class NumberLiteral(override val start: Int, override val end: Int) : Expression

data class SequenceExpression(val expressions: List<Expression>, override val start: Int, override val end: Int) : Expression

data class StringLiteral(override val start: Int, override val end: Int) : Expression

data class TypeReference(override val start: Int, override val end: Int) : ASTNode

data class VariableDeclaration(val type: TypeReference, val declarations: List<VariableDeclarator>, val kind: String, override val start: Int, override val end: Int) : ASTNode

data class VariableDeclarator(val id: Identifier, val type: Expression, override val start: Int, override val end: Int) : ASTNode


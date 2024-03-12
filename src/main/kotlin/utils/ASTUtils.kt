package australfi.ingsis7.utils

sealed interface ASTNode{
    val start: Int
    val end: Int
}

sealed interface Statement : ASTNode

sealed interface Expression: ASTNode

data class Program(val body: List<Statement>, val sourceType: String, override val start: Int, override val end: Int) : ASTNode

data class ExpressionStatement(val expression: Expression, override val start: Int, override val end: Int) : Statement

data class VariableDeclaration(val declarations: List<VariableDeclarator>,
                               val kind: String, override val start: Int, override val end: Int) : Statement

data class AssigmentExpression(val left: Identifier, val right: Expression, override val start: Int, override val end: Int) : Expression

data class BinaryExpression(val left: Expression, val right: Expression, val operator: String,
                            override val start: Int, override val end: Int) : Expression

data class CallExpression (val callee: Identifier, val arguments: SequenceExpression, override val start: Int, override val end: Int) : Expression

data class SequenceExpression(val expressions: List<Expression>, override val start: Int, override val end: Int) : Expression

data class NumberLiteral(val value:Number, override val start: Int, override val end: Int) : Expression

data class StringLiteral(val value:String, override val start: Int, override val end: Int) : Expression

data class Identifier(val name:String, override val start: Int, override val end: Int) : ASTNode

data class TypeReference(override val start: Int, override val end: Int) : ASTNode

data class VariableDeclarator(val id: Identifier,val type: TypeReference?, val init: Expression?,
                              override val start: Int, override val end: Int) : ASTNode
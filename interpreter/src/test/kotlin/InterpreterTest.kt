import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InterpreterTest {
    @Test
    fun interpretEmptyVariableDeclaration() {
        var interpreter = InterpreterImpl()
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init = null,
                            start = 4,
                            end = 13,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 14,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, null)
    }

    @Test
    fun interpretVariableDeclaration() {
        var interpreter = InterpreterImpl()
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init = StringLiteral(value = "hello", start = 16, end = 23),
                            start = 4,
                            end = 23,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 24,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, "hello")
    }

    @Test
    fun interpretMultipleVariableDeclaration() {
        var interpreter = InterpreterImpl()
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "f", start = 4, end = 5),
                            type = TypeReference(type = "number", start = 7, end = 13),
                            init = NumberLiteral(value = 5, start = 16, end = 17),
                            start = 4,
                            end = 17,
                        ),
                        VariableDeclarator(
                            id = Identifier(name = "g", start = 19, end = 20),
                            type = TypeReference(type = "number", start = 22, end = 28),
                            init = NumberLiteral(value = 10, start = 31, end = 33),
                            start = 19,
                            end = 33,
                        ),
                        VariableDeclarator(
                            id = Identifier(name = "h", start = 35, end = 36),
                            type = TypeReference(type = "number", start = 37, end = 43),
                            init = NumberLiteral(value = 15, start = 46, end = 48),
                            start = 35,
                            end = 48,
                        ),
                    ),
                kind = "const",
                start = 0,
                end = 49,
            )
        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "f", "number", false, "5")
        assertVariableInfo(resultVariableMap, "g", "number", false, "10")
        assertVariableInfo(resultVariableMap, "h", "number", false, "15")
    }

    @Test
    fun interpretNumberBinaryExpression() {
        var interpreter = InterpreterImpl()
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "number", start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(), start = 16, end = 17),
                                    right = NumberLiteral(value = 6.toBigDecimal(), start = 20, end = 21),
                                    operator = "+",
                                    start = 16,
                                    end = 21,
                                ),
                            start = 4,
                            end = 21,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 22,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "number", true, "11")
    }

    @Test
    fun interpretStringBinaryExpression() {
        var interpreter = InterpreterImpl()
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "b", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = StringLiteral(value = "hello", start = 16, end = 23),
                                    right = StringLiteral(value = " world", start = 26, end = 34),
                                    operator = "+",
                                    start = 16,
                                    end = 34,
                                ),
                            start = 4,
                            end = 34,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 35,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "b", "string", true, "hello world")

        val errorAstNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "b", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = StringLiteral(value = "hello", start = 16, end = 23),
                                    right = StringLiteral(value = " world", start = 26, end = 34),
                                    operator = "-",
                                    start = 16,
                                    end = 34,
                                ),
                            start = 4,
                            end = 34,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 35,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(errorAstNode)
        }
    }

    @Test
    fun interpretNumberStringBinaryExpression() {
        var interpreter = InterpreterImpl()
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = NumberLiteral(value = 8, start = 16, end = 17),
                                    right = StringLiteral(value = "th", start = 20, end = 24),
                                    operator = "+",
                                    start = 16,
                                    end = 24,
                                ),
                            start = 4,
                            end = 24,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 25,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, "8th")
    }

    @Test
    fun interpretBinaryExpression() {
        var interpreter = InterpreterImpl(mapOf("a" to VariableInfo("number", "2", true), "b" to VariableInfo("number", "4", true)))
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "c", start = 4, end = 5),
                            type = TypeReference(type = "number", start = 8, end = 14),
                            init =
                                BinaryExpression(
                                    left =
                                        BinaryExpression(
                                            left = Identifier(name = "a", start = 18, end = 19),
                                            right = NumberLiteral(value = 2.toBigDecimal(), start = 22, end = 23),
                                            operator = "*",
                                            start = 18,
                                            end = 23,
                                        ),
                                    right =
                                        BinaryExpression(
                                            left = NumberLiteral(value = 4.toBigDecimal(), start = 29, end = 30),
                                            right = Identifier(name = "b", start = 33, end = 34),
                                            operator = "/",
                                            start = 29,
                                            end = 34,
                                        ),
                                    operator = "-",
                                    start = 18,
                                    end = 34,
                                ),
                            start = 4,
                            end = 35,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 36,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "c", "number", true, "3")
    }

    @Test
    fun testNumberAssignmentExpression() {
        var interpreter = InterpreterImpl(mapOf("a" to VariableInfo("number", null, true)))
        val astNodeA =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = NumberLiteral(value = 9, start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeA)
        assertVariableInfo(interpreter.variableMap, "a", "number", true, "9")
    }

    @Test
    fun testStringAssignmentExpression() {
        var interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", true)))
        val astNodeD =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = StringLiteral(value = "world", start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeD)
        assertVariableInfo(interpreter.variableMap, "a", "string", true, "world")
    }

    @Test
    fun testNonexistentAssignmentExpression() {
        val interpreter = InterpreterImpl()
        val astNodeB =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = NumberLiteral(value = 9, start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNodeB)
        }
    }

    @Test
    fun testConstantAssignmentExpression() {
        val interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", false)))
        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = StringLiteral(value = "world", start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun testIdentifierAssignmentExpression() {
        var interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("string", "hello", true),
                    "d" to VariableInfo("string", "world", false),
                ),
            )
        val astNodeBinaryExpression =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = Identifier(name = "d", start = 18, end = 19),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeBinaryExpression)
        assertVariableInfo(interpreter.variableMap, "a", "string", true, "world")
    }

    private fun assertVariableInfo(
        variableMap: Map<String, VariableInfo>,
        variableName: String,
        type: String,
        isMutable: Boolean,
        value: Any?,
    ) {
        assertTrue(variableMap.containsKey(variableName), "Variable $variableName should exist")
        assertEquals(type, variableMap[variableName]?.type, "Type mismatch for $variableName")
        assertEquals(isMutable, variableMap[variableName]?.isMutable, "Mutability mismatch for $variableName")
        assertEquals(value, variableMap[variableName]?.value, "Value mismatch for $variableName")
    }
}

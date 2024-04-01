import astbuilder.ASTBuilderSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class InterpreterTest {
    @Test
    fun interpretEmptyVariableDeclaration() {
        var interpreter = InterpreterImpl()
        val astBuilderSuccess =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )
        interpreter = interpreter.interpret(astBuilderSuccess.astNode)
        val resultVariableMap = interpreter.variableMap

        assertAll(
            { assertTrue(resultVariableMap.containsKey("a")) },
            { assertEquals(resultVariableMap["a"]?.type, "string") },
            { assertEquals(resultVariableMap["a"]?.isMutable, true) },
            { assertEquals(resultVariableMap["a"]?.value, null) },
        )
    }

    @Test
    fun interpretVariableDeclaration() {
        var interpreter = InterpreterImpl()
        val astBuilderSuccess =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )

        interpreter = interpreter.interpret(astBuilderSuccess.astNode)
        val resultVariableMap = interpreter.variableMap

        assertAll(
            { assertTrue(resultVariableMap.containsKey("a")) },
            { assertEquals(resultVariableMap["a"]?.type, "string") },
            { assertEquals(resultVariableMap["a"]?.isMutable, true) },
            { assertEquals(resultVariableMap["a"]?.value, "hello") },
        )
    }

    @Test
    fun interpretMultipleVariableDeclaration() {
        var interpreter = InterpreterImpl()
        val astBuilderSuccess =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )
        interpreter = interpreter.interpret(astBuilderSuccess.astNode)
        val resultVariableMap = interpreter.variableMap

        assertAll(
            { assertTrue(resultVariableMap.containsKey("f")) },
            { assertEquals(resultVariableMap["f"]?.type, "number") },
            { assertEquals(resultVariableMap["f"]?.isMutable, false) },
            { assertEquals(resultVariableMap["f"]?.value, "5") },
        )
        assertAll(
            { assertTrue(resultVariableMap.containsKey("f")) },
            { assertEquals(resultVariableMap["g"]?.type, "number") },
            { assertEquals(resultVariableMap["g"]?.isMutable, false) },
            { assertEquals(resultVariableMap["g"]?.value, "10") },
        )
        assertAll(
            { assertTrue(resultVariableMap.containsKey("f")) },
            { assertEquals(resultVariableMap["h"]?.type, "number") },
            { assertEquals(resultVariableMap["h"]?.isMutable, false) },
            { assertEquals(resultVariableMap["h"]?.value, "15") },
        )
    }

    @Test
    fun interpretNumberBinaryExpression() {
        var interpreter = InterpreterImpl()
        val astBuilderSuccess =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )

        interpreter = interpreter.interpret(astBuilderSuccess.astNode)
        val resultVariableMap = interpreter.variableMap

        assertAll(
            { assertTrue(resultVariableMap.containsKey("a")) },
            { assertEquals(resultVariableMap["a"]?.type, "number") },
            { assertEquals(resultVariableMap["a"]?.isMutable, true) },
            { assertEquals(resultVariableMap["a"]?.value, "11") },
        )
    }

    @Test
    fun interpretStringBinaryExpression() {
        var interpreter = InterpreterImpl()
        val astBuilderSuccess =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )

        interpreter = interpreter.interpret(astBuilderSuccess.astNode)
        val resultVariableMap = interpreter.variableMap

        assertAll(
            { assertTrue(resultVariableMap.containsKey("b")) },
            { assertEquals(resultVariableMap["b"]?.type, "string") },
            { assertEquals(resultVariableMap["b"]?.isMutable, true) },
            { assertEquals(resultVariableMap["b"]?.value, "hello world") },
        )
    }

    @Test
    fun interpretNumberStringBinaryExpression() {
        var interpreter = InterpreterImpl()
        val astBuilderSuccess =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )

        interpreter = interpreter.interpret(astBuilderSuccess.astNode)
        val resultVariableMap = interpreter.variableMap

        assertAll(
            { assertTrue(resultVariableMap.containsKey("a")) },
            { assertEquals(resultVariableMap["a"]?.type, "string") },
            { assertEquals(resultVariableMap["a"]?.isMutable, true) },
            { assertEquals(resultVariableMap["a"]?.value, "8th") },
        )
    }
}

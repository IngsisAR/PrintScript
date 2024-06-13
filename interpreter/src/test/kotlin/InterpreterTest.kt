import interpreter.CallExpressionInterpreter
import interpreter.InterpreterImpl
import interpreter.VariableInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import utils.AssignmentExpression
import utils.BinaryExpression
import utils.BooleanLiteral
import utils.CallExpression
import utils.ConditionalStatement
import utils.ExpressionStatement
import utils.Identifier
import utils.NumberLiteral
import utils.StringLiteral
import utils.TypeReference
import utils.VariableDeclaration
import utils.VariableDeclarator
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class InterpreterTest {
    @Test
    fun interpretEmptyVariableDeclaration() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init = null,
                            line = 1,
                            start = 4,
                            end = 13,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 14,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, null)
    }

    @Test
    fun interpretVariableDeclaration() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init = StringLiteral(value = "hello", line = 1, start = 16, end = 23),
                            line = 1,
                            start = 4,
                            end = 23,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 24,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, "hello")
    }

    @Test
    fun interpretIdentifierVariableDeclaration() {
        var interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "2", true),
                ),
                "1.1.0",
            )
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "b", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "number", line = 1, start = 7, end = 13),
                            init = Identifier(name = "a", line = 1, start = 4, end = 5),
                            line = 1,
                            start = 4,
                            end = 21,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 22,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "b", "number", true, "2")
    }

    @Test
    fun interpretMultipleVariableDeclaration() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "f", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "number", line = 1, start = 7, end = 13),
                            init = NumberLiteral(value = 5, line = 1, start = 16, end = 17),
                            line = 1,
                            start = 4,
                            end = 17,
                        ),
                        VariableDeclarator(
                            id = Identifier(name = "g", line = 1, start = 19, end = 20),
                            type = TypeReference(type = "number", line = 1, start = 22, end = 28),
                            init = NumberLiteral(value = 10, line = 1, start = 31, end = 33),
                            line = 1,
                            start = 19,
                            end = 33,
                        ),
                        VariableDeclarator(
                            id = Identifier(name = "h", line = 1, start = 35, end = 36),
                            type = TypeReference(type = "number", line = 1, start = 37, end = 43),
                            init = NumberLiteral(value = 15, line = 1, start = 46, end = 48),
                            line = 1,
                            start = 35,
                            end = 48,
                        ),
                    ),
                kind = "const",
                line = 1,
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
    fun interpretErrorVariableDeclaration() {
        val interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init =
                                CallExpression(
                                    callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                                    arguments = listOf(Identifier(name = "c", line = 1, start = 8, end = 9)),
                                    line = 1,
                                    start = 0,
                                    end = 10,
                                ),
                            line = 1,
                            start = 4,
                            end = 23,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 24,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretNumberBinaryExpression() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "number", line = 1, start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 16, end = 17),
                                    right = NumberLiteral(value = 6.toBigDecimal(), line = 1, start = 20, end = 21),
                                    operator = "+",
                                    line = 1,
                                    start = 16,
                                    end = 21,
                                ),
                            line = 1,
                            start = 4,
                            end = 21,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 22,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "number", true, "11")
    }

    @Test
    fun interpretStringBinaryExpression() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "b", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = StringLiteral(value = "hello", line = 1, start = 16, end = 23),
                                    right = StringLiteral(value = " world", line = 1, start = 26, end = 34),
                                    operator = "+",
                                    line = 1,
                                    start = 16,
                                    end = 34,
                                ),
                            line = 1,
                            start = 4,
                            end = 34,
                        ),
                    ),
                kind = "let",
                line = 1,
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
                            id = Identifier(name = "b", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = StringLiteral(value = "hello", line = 1, start = 16, end = 23),
                                    right = StringLiteral(value = " world", line = 1, start = 26, end = 34),
                                    operator = "-",
                                    line = 1,
                                    start = 16,
                                    end = 34,
                                ),
                            line = 1,
                            start = 4,
                            end = 34,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 35,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(errorAstNode)
        }
    }

    @Test
    fun interpretNumberStringBinaryExpression() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = NumberLiteral(value = 8, line = 1, start = 16, end = 17),
                                    right = StringLiteral(value = "th", line = 1, start = 20, end = 24),
                                    operator = "+",
                                    line = 1,
                                    start = 16,
                                    end = 24,
                                ),
                            line = 1,
                            start = 4,
                            end = 24,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 25,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, "8th")
    }

    @Test
    fun interpretStringNumberBinaryExpression() {
        var interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = StringLiteral(value = "n°", line = 1, start = 16, end = 17),
                                    right = NumberLiteral(value = 1.toBigDecimal(), line = 1, start = 20, end = 24),
                                    operator = "+",
                                    line = 1,
                                    start = 16,
                                    end = 24,
                                ),
                            line = 1,
                            start = 4,
                            end = 24,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 25,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "a", "string", true, "n°1")
    }

    @Test
    fun interpretingStringIdentifiersInPlusOperationShouldResultInConcatenation() {
        var interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("string", "hello ", true),
                    "b" to VariableInfo("string", "world", true),
                ),
                "1.1.0",
            )
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "c", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                            init =
                                BinaryExpression(
                                    left = Identifier(name = "a", line = 1, start = 16, end = 20),
                                    right = Identifier(name = "b", line = 1, start = 23, end = 28),
                                    operator = "+",
                                    line = 1,
                                    start = 16,
                                    end = 28,
                                ),
                            line = 1,
                            start = 4,
                            end = 28,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 29,
            )
        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "c", "string", true, "hello world")
    }

    @Test
    fun interpretBinaryExpression() {
        var interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "2", true),
                    "b" to VariableInfo("number", "4", true),
                ),
                "1.1.0",
            )
        val astNode =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "c", line = 1, start = 4, end = 5),
                            type = TypeReference(type = "number", line = 1, start = 8, end = 14),
                            init =
                                BinaryExpression(
                                    left =
                                        BinaryExpression(
                                            left = Identifier(name = "a", line = 1, start = 18, end = 19),
                                            right = NumberLiteral(value = 2.toBigDecimal(), line = 1, start = 22, end = 23),
                                            operator = "*",
                                            line = 1,
                                            start = 18,
                                            end = 23,
                                        ),
                                    right =
                                        BinaryExpression(
                                            left = NumberLiteral(value = 4.toBigDecimal(), line = 1, start = 29, end = 30),
                                            right = Identifier(name = "b", line = 1, start = 33, end = 34),
                                            operator = "/",
                                            line = 1,
                                            start = 29,
                                            end = 34,
                                        ),
                                    operator = "-",
                                    line = 1,
                                    start = 18,
                                    end = 34,
                                ),
                            line = 1,
                            start = 4,
                            end = 35,
                        ),
                    ),
                kind = "let",
                line = 1,
                start = 0,
                end = 36,
            )

        interpreter = interpreter.interpret(astNode)
        val resultVariableMap = interpreter.variableMap

        assertVariableInfo(resultVariableMap, "c", "number", true, "3")
    }

    @Test
    fun interpretInvalidOperatorBinaryExpression() {
        val interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            BinaryExpression(
                left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 16, end = 17),
                right = NumberLiteral(value = 6.toBigDecimal(), line = 1, start = 20, end = 21),
                operator = ".",
                line = 1,
                start = 16,
                end = 21,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretNumberAssignmentExpression() {
        var interpreter = InterpreterImpl(mapOf("a" to VariableInfo("number", null, true)), "1.1.0")
        val astNodeA =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right = NumberLiteral(value = 9, line = 1, start = 4, end = 5),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeA)
        assertVariableInfo(interpreter.variableMap, "a", "number", true, "9")
    }

    @Test
    fun interpretStringAssignmentExpression() {
        var interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", true)), "1.1.0")
        val astNodeD =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right = StringLiteral(value = "world", line = 1, start = 4, end = 5),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeD)
        assertVariableInfo(interpreter.variableMap, "a", "string", true, "world")
    }

    @Test
    fun interpretBinaryAssignmentExpression() {
        var interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "4", true),
                ),
                "1.1.0",
            )
        val astNodeBinaryExpression =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right =
                            BinaryExpression(
                                left = NumberLiteral(value = 4.toBigDecimal(), line = 1, start = 29, end = 30),
                                right = NumberLiteral(value = 4.toBigDecimal(), line = 1, start = 29, end = 30),
                                operator = "/",
                                line = 1,
                                start = 29,
                                end = 34,
                            ),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeBinaryExpression)
        assertVariableInfo(interpreter.variableMap, "a", "number", true, "1")
    }

    @Test
    fun interpretIdentifierAssignmentExpression() {
        var interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("string", "hello", true),
                    "d" to VariableInfo("string", "world", true),
                ),
                "1.1.0",
            )
        val astNodeBinaryExpression =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right = Identifier(name = "d", line = 1, start = 18, end = 19),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        interpreter = interpreter.interpret(astNodeBinaryExpression)
        assertVariableInfo(interpreter.variableMap, "a", "string", true, "world")
    }

    @Test
    fun interpretFunctionAssignmentExpression() {
        var interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", null, true)), "1.1.0")

        val input = "Name"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right =
                            CallExpression(
                                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                                arguments = listOf(StringLiteral(value = "Enter a value:", line = 1, start = 16, end = 30)),
                                line = 1,
                                start = 0,
                                end = 10,
                            ),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )

        interpreter = interpreter.interpret(astNode)
        assertVariableInfo(interpreter.variableMap, "a", "string", true, "Name")
    }

    @Test
    fun interpretNotFoundAssignmentExpression() {
        val interpreter = InterpreterImpl(version = "1.1.0")
        val astNodeB =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right = NumberLiteral(value = 9, line = 1, start = 4, end = 5),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNodeB)
        }
    }

    @Test
    fun interpretConstantAssignmentExpression() {
        val interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", false)), "1.1.0")
        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right = StringLiteral(value = "world", line = 1, start = 4, end = 5),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretErrorAssignmentExpression() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("string", "hello", true),
                    "b" to VariableInfo("string", "world", true),
                ),
                "1.1.0",
            )
        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "b", line = 1, start = 0, end = 1),
                        right =
                            AssignmentExpression(
                                left = Identifier(name = "a", line = 1, start = 0, end = 1),
                                right = StringLiteral(value = "world", line = 1, start = 4, end = 5),
                                line = 1,
                                start = 0,
                                end = 5,
                            ),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretTypeMismatchAssignmentExpression() {
        val interpreter = InterpreterImpl(mapOf("a" to VariableInfo("number", "2", true)), "1.1.0")
        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "1", line = 1, start = 0, end = 1),
                        right = StringLiteral(value = "world", line = 1, start = 4, end = 5),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretNotFoundIdentifierExpression() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("string", "hello", true),
                    "b" to VariableInfo("number", "2", false),
                ),
                "1.1.0",
            )
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                        arguments = listOf(Identifier(name = "c", line = 1, start = 8, end = 9)),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretInvalidTypeIdentifierExpression() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("string", "hello", true),
                    "b" to VariableInfo("number", "2", false),
                    "c" to VariableInfo("unsupported", "true", false),
                ),
                "1.1.0",
            )
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                        arguments = listOf(Identifier(name = "c", line = 1, start = 8, end = 9)),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretIdentifierExpression() {
        val interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", true)), "1.1.0")
        val astNode = NumberLiteral(value = 2.toBigDecimal(), line = 1, start = 22, end = 23)
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun givenPrintlnWithNumber_when_interpretingPrintlnCallExpression_then_ShouldPrintNumber() {
        val consoleOutputCapture = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutputCapture))

        val interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                        arguments = listOf(NumberLiteral(value = 2.toBigDecimal(), line = 1, start = 8, end = 9)),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        interpreter.interpret(astNode)
        assertEquals("2\n", consoleOutputCapture.toString())
    }

    @Test
    fun givenPrintlnWithString_when_interpretingPrintlnCallExpression_then_ShouldPrintString() {
        val consoleOutputCapture = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutputCapture))

        val interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                        arguments = listOf(StringLiteral(value = "hello", line = 1, start = 8, end = 13)),
                        line = 1,
                        start = 0,
                        end = 14,
                    ),
                line = 1,
                start = 0,
                end = 15,
            )
        interpreter.interpret(astNode)
        assertEquals("hello\n", consoleOutputCapture.toString())
    }

    @Test
    fun givenPrintlnWithIdentifier_when_interpretingPrintlnCallExpression_then_ShouldPrintVariableValue() {
        val consoleOutputCapture = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutputCapture))

        val interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", true)), "1.1.0")
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                        arguments = listOf(Identifier(name = "a", line = 1, start = 8, end = 9)),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        interpreter.interpret(astNode)
        assertEquals("hello\n", consoleOutputCapture.toString())
    }

    @Test
    fun givenPrintlnWithSumOfTwoNumbers_when_interpretingPrintlnCallExpression_then_ShouldPrintSum() {
        val consoleOutputCapture = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutputCapture))

        val interpreter = InterpreterImpl(version = "1.1.0")
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 2.toBigDecimal(), line = 1, start = 8, end = 9),
                                    right = NumberLiteral(value = 3.toBigDecimal(), line = 1, start = 12, end = 13),
                                    operator = "+",
                                    line = 1,
                                    start = 8,
                                    end = 13,
                                ),
                            ),
                        line = 1,
                        start = 0,
                        end = 14,
                    ),
                line = 1,
                start = 0,
                end = 15,
            )
        interpreter.interpret(astNode)
        assertEquals("5\n", consoleOutputCapture.toString())
    }

    @Test
    fun interpretInvalidCallExpression() {
        val interpreter = InterpreterImpl(mapOf("a" to VariableInfo("string", "hello", true)), "1.1.0")

        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "sum", line = 1, start = 0, end = 7),
                        arguments = listOf(Identifier(name = "a", line = 1, start = 8, end = 9)),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    private fun assertVariableInfo(
        variableMap: Map<String, VariableInfo>,
        name: String,
        type: String,
        isMutable: Boolean,
        value: Any?,
    ) {
        assertTrue(variableMap.containsKey(name), "Variable $name should exist")
        assertEquals(type, variableMap[name]?.type, "Type mismatch for $name")
        assertEquals(isMutable, variableMap[name]?.isMutable, "Mutability mismatch for $name")
        assertEquals(value, variableMap[name]?.value, "Value mismatch for $name")
    }

    @Test
    fun givenANumberAndTrueCondition_interpretingSimpleConditionalStatementWithPrintln_ShouldOutputNumberToConsole() {
        val consoleOutputCapture = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutputCapture))
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "1", true),
                    "b" to VariableInfo("bool", "true", true),
                ),
                "1.1.0",
            )
        /*
        let a:number = 1;
        let b:bool = true;
        if(b) {
            println(a);
        }
         */
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "b", line = 1, start = 0, end = 1),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                                    arguments = listOf(Identifier(name = "a", line = 1, start = 8, end = 9)),
                                    line = 1,
                                    start = 0,
                                    end = 10,
                                ),
                            line = 1,
                            start = 0,
                            end = 11,
                        ),
                    ),
                alternate = emptyList(),
                line = 1,
                start = 0,
                end = 11,
            )
        interpreter.interpret(astNode)
        assertEquals("1\n", consoleOutputCapture.toString())
    }

    @Test
    fun givenFalseConditionAndNumbers_interpretingConditionalStatementWithPrintln_ShouldOutputAlternateToConsole() {
        val consoleOutputCapture = ByteArrayOutputStream()
        System.setOut(PrintStream(consoleOutputCapture))
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "1", true),
                    "b" to VariableInfo("bool", "false", true),
                    "c" to VariableInfo("number", "2", true),
                ),
                "1.1.0",
            )
        /*
        let a:number = 1;
        let b:bool = false;
        let c:number = 2;
        if(b) {
            println(a);
        }else{
            println(c);
        }
         */
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "b", line = 1, start = 0, end = 1),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                                    arguments = listOf(Identifier(name = "a", line = 1, start = 8, end = 9)),
                                    line = 1,
                                    start = 0,
                                    end = 10,
                                ),
                            line = 1,
                            start = 0,
                            end = 11,
                        ),
                    ),
                alternate =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                                    arguments = listOf(Identifier(name = "c", line = 1, start = 8, end = 9)),
                                    line = 1,
                                    start = 0,
                                    end = 10,
                                ),
                            line = 1,
                            start = 0,
                            end = 11,
                        ),
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        interpreter.interpret(astNode)
        assertEquals("2\n", consoleOutputCapture.toString())
    }

    @Test
    fun givenBadCondition_interpretingSimpleConditionalStatement_ShouldThrowIllegalArgumentException() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "1", true),
                    "b" to VariableInfo("string", "true", true),
                ),
                "1.1.0",
            )
        /*
        let b:string = "true";
        if(b) {
        }
         */
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "b", line = 1, start = 0, end = 1),
                consequent = emptyList(),
                alternate = emptyList(),
                line = 1,
                start = 0,
                end = 11,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun getEnvironmentVariable() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "b" to VariableInfo("string", "", true),
                ),
                "1.1.0",
            )
        /*
        b = readEnv("PATH")
         */
        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "b", line = 1, start = 0, end = 1),
                        right =
                            CallExpression(
                                callee = Identifier(name = "readEnv", line = 1, start = 0, end = 7),
                                arguments = listOf(StringLiteral(value = "PATH", line = 1, start = 16, end = 23)),
                                line = 1,
                                start = 0,
                                end = 10,
                            ),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        val response = interpreter.interpret(astNode)
        assertEquals(System.getenv("PATH"), response.variableMap["b"]?.value)
    }

    @Test
    fun callExpressionPrintln() {
        val variableMap = mapOf("b" to VariableInfo("string", "true", true))
        val interpreter = CallExpressionInterpreter(variableMap, "1.1.0")

        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))

        val astNode =
            CallExpression(
                callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                arguments = listOf(StringLiteral(value = "Hello, World!", line = 1, start = 16, end = 30)),
                line = 1,
                start = 0,
                end = 11,
            )

        interpreter.interpret(astNode)

        assertEquals("Hello, World!\n", outContent.toString())
    }

    @Test
    fun interpretCallExpression_ReadUserInput_with_numericInput() {
        val interpreter = CallExpressionInterpreter(emptyMap(), "1.1.0")

        val input = "123"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            CallExpression(
                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                arguments = listOf(StringLiteral(value = "Enter a value:", line = 1, start = 16, end = 30)),
                line = 1,
                start = 0,
                end = 11,
            )

        val result = interpreter.interpret(astNode)

        assertEquals(123, result)
    }

    @Test
    fun interpretCallExpression_ReadUserInput_with_booleanInput() {
        val interpreter = CallExpressionInterpreter(emptyMap(), "1.1.0")

        val input = "true"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            CallExpression(
                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                arguments = listOf(StringLiteral(value = "Enter a value:", line = 1, start = 16, end = 30)),
                line = 1,
                start = 0,
                end = 11,
            )

        val result = interpreter.interpret(astNode)

        assertEquals(true, result)
    }

    @Test
    fun interpretCallExpression_ReadUserInput_with_IdentifierStringAsArgument() {
        val variableMap = mapOf("b" to VariableInfo("string", "true", true))
        val interpreter = CallExpressionInterpreter(variableMap, "1.1.0")

        val input = "123"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            CallExpression(
                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                arguments = listOf(Identifier(name = "b", line = 1, start = 16, end = 17)),
                line = 1,
                start = 0,
                end = 11,
            )

        val result = interpreter.interpret(astNode)

        assertEquals(123, result)
    }

    @Test
    fun interpretCallExpression_ReadUserInput_with_IdentifierBooleanAsArgument_ShouldThrowException() {
        val variableMap = mapOf("b" to VariableInfo("bool", "true", true))
        val interpreter = CallExpressionInterpreter(variableMap, "1.1.0")

        val input = "true"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            CallExpression(
                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                arguments = listOf(Identifier(name = "b", line = 1, start = 16, end = 17)),
                line = 1,
                start = 0,
                end = 11,
            )

        val message =
            assertThrows(IllegalArgumentException::class.java) {
                interpreter.interpret(astNode)
            }.message
        assertEquals(
            "Expected String argument but was Boolean " +
                "at (1:16)",
            message,
        )
    }

    @Test
    fun interpretCallExpression_ReadUserInput_with_IdentifierNullAsArgument_ShouldThrowException() {
        val variableMap = mapOf("b" to VariableInfo("string", null, true))
        val interpreter = CallExpressionInterpreter(variableMap, "1.1.0")

        val input = "123"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            CallExpression(
                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                arguments = listOf(Identifier(name = "b", line = 1, start = 16, end = 17)),
                line = 1,
                start = 0,
                end = 11,
            )

        val message =
            assertThrows(IllegalArgumentException::class.java) {
                interpreter.interpret(astNode)
            }.message
        assertEquals(
            "Variable b is not initialized " +
                "at (1:16)",
            message,
        )
    }

    @Test
    fun booleanOnOlderVersion() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("number", "1", true),
                    "b" to VariableInfo("bool", "true", true),
                ),
                "1.0.0",
            )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "b", line = 1, start = 0, end = 1),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "println", line = 1, start = 0, end = 7),
                                    arguments = listOf(Identifier(name = "a", line = 1, start = 8, end = 9)),
                                    line = 1,
                                    start = 0,
                                    end = 10,
                                ),
                            line = 1,
                            start = 0,
                            end = 11,
                        ),
                    ),
                alternate = emptyList(),
                line = 1,
                start = 0,
                end = 11,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun interpretBooleanAssignmentExpressionOlderVersion() {
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "a" to VariableInfo("bool", null, true),
                ),
                "1.0.0",
            )

        val astNode =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a", line = 1, start = 0, end = 1),
                        right = BooleanLiteral(value = true, line = 1, start = 4, end = 5),
                        line = 1,
                        start = 0,
                        end = 5,
                    ),
                line = 1,
                start = 0,
                end = 6,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun callExpressionReadUserInputOnOlderVersion() {
        val variableMap = mapOf("b" to VariableInfo("string", "true", true))
        val interpreter = CallExpressionInterpreter(variableMap, "1.0.0")

        val input = "true"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val astNode =
            CallExpression(
                callee = Identifier(name = "readInput", line = 1, start = 0, end = 7),
                arguments = listOf(StringLiteral(value = "Enter a value:", line = 1, start = 16, end = 30)),
                line = 1,
                start = 0,
                end = 11,
            )

        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }

    @Test
    fun getEnvironmentVariableOnOlderVersion() {
        System.setProperty("TEST_PATH", "/mocked/path")
        val interpreter =
            InterpreterImpl(
                mapOf(
                    "b" to VariableInfo("string", "true", true),
                ),
                "1.0.0",
            )
        val astNode =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "readEnv", line = 1, start = 0, end = 7),
                        arguments = listOf(StringLiteral(value = "PATH", line = 1, start = 16, end = 23)),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
                line = 1,
                start = 0,
                end = 11,
            )
        assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(astNode)
        }
    }
}

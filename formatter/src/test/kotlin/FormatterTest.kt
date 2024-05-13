import formatter.FormatterImpl
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertThrows
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {
    private val testConfigJsonPath = "src/test/resources/FormatterTestConfig.json"

    private fun createTestConfigJson(configMap: Map<String, Any?>) {
        val jsonObject = JSONObject()
        configMap.forEach { (key, value) -> jsonObject.put(key, value) }
        try {
            Files.write(Path(testConfigJsonPath), jsonObject.toString().toByteArray(), StandardOpenOption.CREATE_NEW)
        } catch (e: Exception) {
            Files.write(Path(testConfigJsonPath), jsonObject.toString().toByteArray(), StandardOpenOption.TRUNCATE_EXISTING)
        }
    }

    @Test
    fun printWithOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
            ),
        )

        val ast =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "print",, start = 0, end = 5),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(),, start = 6, end = 7),
                                    right = NumberLiteral(value = 4.toBigDecimal(),, start = 10, end = 11),
                                    operator = "+",,
                                    start = 6,
                                    end = 11,
                                ),
                            ),,
                        start = 0,
                        end = 12,
                    ),,
                start = 0,
                end = 13,
            )
        val formatter = FormatterImpl()
        assertEquals("\nprint(5 + 4);\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun printWithTwoSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 2,
                "spaceAfterColon" to 2,
                "spacesInAssignSymbol" to 2,
                "lineJumpBeforePrintln" to 2,
            ),
        )

        val ast =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "print",, start = 0, end = 5),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(),, start = 6, end = 7),
                                    right = NumberLiteral(value = 4.toBigDecimal(),, start = 10, end = 11),
                                    operator = "+",,
                                    start = 6,
                                    end = 11,
                                ),
                            ),,
                        start = 0,
                        end = 12,
                    ),,
                start = 0,
                end = 13,
            )
        val formatter = FormatterImpl()
        assertEquals("\n\nprint(5 + 4);\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun printWithNoDefineSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
            ),
        )

        val ast =
            ExpressionStatement(
                expression =
                    CallExpression(
                        callee = Identifier(name = "print",, start = 0, end = 5),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(),, start = 6, end = 7),
                                    right = NumberLiteral(value = 4.toBigDecimal(),, start = 10, end = 11),
                                    operator = "+",,
                                    start = 6,
                                    end = 11,
                                ),
                            ),,
                        start = 0,
                        end = 12,
                    ),,
                start = 0,
                end = 13,
            )
        val formatter = FormatterImpl()
        assertEquals("print(5 + 4);\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun numberVariableDeclaratorWithOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
            ),
        )

        val ast =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a",, start = 4, end = 5),
                            type = TypeReference(type = "number",, start = 7, end = 13),
                            init = NumberLiteral(value = 1.toBigDecimal(),, start = 16, end = 18),,
                            start = 4,
                            end = 18,
                        ),
                    ),
                kind = "let",,
                start = 0,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertEquals("let a : number = 1;\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun numberVariableDeclaratorWithTwoSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 2,
                "spaceAfterColon" to 2,
                "spacesInAssignSymbol" to 2,
                "lineJumpBeforePrintln" to 2,
            ),
        )

        val ast =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a",, start = 4, end = 5),
                            type = TypeReference(type = "number",, start = 7, end = 13),
                            init = NumberLiteral(value = 1.toBigDecimal(),, start = 16, end = 18),,
                            start = 4,
                            end = 18,
                        ),
                    ),
                kind = "let",,
                start = 0,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertEquals("let a  :  number  =  1;\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun numberVariableDeclaratorWithInvalidSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
            ),
        )

        val ast =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a",, start = 4, end = 5),
                            type = TypeReference(type = "number",, start = 7, end = 13),
                            init = NumberLiteral(value = 1.toBigDecimal(),, start = 16, end = 18),,
                            start = 4,
                            end = 18,
                        ),
                    ),
                kind = "let",,
                start = 0,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertEquals("let a:number=1;\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun stringVariableDeclaratorWithOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
            ),
        )

        val ast =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a",, start = 4, end = 5),
                            type = TypeReference(type = "string",, start = 7, end = 13),
                            init = StringLiteral(value = "Hello World",, start = 16, end = 29),,
                            start = 4,
                            end = 29,
                        ),
                    ),
                kind = "let",,
                start = 0,
                end = 30,
            )
        val formatter = FormatterImpl()
        assertEquals("let a : string = \"Hello World\";\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun stringVariableDeclaratorWithTwoSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 2,
                "spaceAfterColon" to 2,
                "spacesInAssignSymbol" to 2,
                "lineJumpBeforePrintln" to 2,
            ),
        )

        val ast =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a",, start = 4, end = 5),
                            type = TypeReference(type = "string",, start = 7, end = 13),
                            init = StringLiteral(value = "Hello World",, start = 16, end = 29),,
                            start = 4,
                            end = 29,
                        ),
                    ),
                kind = "let",,
                start = 0,
                end = 30,
            )
        val formatter = FormatterImpl()
        assertEquals("let a  :  string  =  \"Hello World\";\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun stringVariableDeclaratorWithInvalidSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
            ),
        )

        val ast =
            VariableDeclaration(
                declarations =
                    listOf(
                        VariableDeclarator(
                            id = Identifier(name = "a",, start = 4, end = 5),
                            type = TypeReference(type = "string",, start = 7, end = 13),
                            init = StringLiteral(value = "Hello World",, start = 16, end = 29),,
                            start = 4,
                            end = 29,
                        ),
                    ),
                kind = "let",,
                start = 0,
                end = 30,
            )
        val formatter = FormatterImpl()
        assertEquals("let a:string=\"Hello World\";\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun assignationStatementWithOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
            ),
        )

        val ast =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a",, start = 0, end = 1),
                        right = NumberLiteral(value = 2.toBigDecimal(),, start = 4, end = 5),,
                        start = 0,
                        end = 5,
                    ),,
                start = 0,
                end = 6,
            )
        val formatter = FormatterImpl()
        assertEquals("a = 2;\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun assignationStatementWithInvalidSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
            ),
        )

        val ast =
            ExpressionStatement(
                expression =
                    AssignmentExpression(
                        left = Identifier(name = "a",, start = 0, end = 1),
                        right = NumberLiteral(value = 2.toBigDecimal(),, start = 4, end = 5),,
                        start = 0,
                        end = 5,
                    ),,
                start = 0,
                end = 6,
            )
        val formatter = FormatterImpl()
        assertEquals("a=2;\n", formatter.format(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun errorUnknownASTNodeType() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
            ),
        )
        val ast = NumberLiteral(value = 2.toBigDecimal(),, start = 0, end = 1)
        val formatter = FormatterImpl()
        assertThrows(IllegalArgumentException::class.java) {
            formatter.format(ast, testConfigJsonPath, "1.1.0")
        }
    }

    @Test
    fun conditionalWithoutelseAndOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
                "identationInsideConditionals" to 1,
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left = NumberLiteral(value = 1.toBigDecimal(),, start = 13, end = 14),
                                    right = NumberLiteral(value = 1.toBigDecimal(),, start = 15, end = 16),
                                    operator = "+",,
                                    start = 13,
                                    end = 16,
                                ),,
                            start = 13,
                            end = 17,
                        ),
                    ),
                alternate = emptyList(),,
                start = 7,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertEquals("if(a) {\n" + "    1 + 1;\n" + "}", formatter.format(astNode, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun conditionalWithElseAndOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
                "identationInsideConditionals" to 1,
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left = NumberLiteral(value = 1.toBigDecimal(),, start = 13, end = 14),
                                    right = NumberLiteral(value = 1.toBigDecimal(),, start = 15, end = 16),
                                    operator = "+",,
                                    start = 13,
                                    end = 16,
                                ),,
                            start = 13,
                            end = 17,
                        ),
                    ),
                alternate =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "print",, start = 29, end = 36),
                                    arguments = listOf(StringLiteral(value = "Hello",, start = 37, end = 44)),,
                                    start = 29,
                                    end = 45,
                                ),,
                            start = 29,
                            end = 46,
                        ),
                    ),,
                start = 7,
                end = 48,
            )
        val formatter = FormatterImpl()
        assertEquals(
            "if(a) {\n" +
                "    1 + 1;\n" +
                "} else {\n" +
                "    \n" +
                "    print(\"Hello\");\n" +
                "}\n",
            formatter.format(astNode, testConfigJsonPath, "1.1.0"),
        )
    }

    @Test
    fun multipleConditionalsWithElseAndOneSpace() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
                "identationInsideConditionals" to 1,
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ConditionalStatement(
                            test = Identifier(name = "b",, start = 16, end = 17),
                            consequent =
                                listOf(
                                    ExpressionStatement(
                                        expression =
                                            CallExpression(
                                                callee = Identifier(name = "println",, start = 28, end = 35),
                                                arguments = listOf(StringLiteral(value = "If b",, start = 36, end = 42)),,
                                                start = 28,
                                                end = 43,
                                            ),,
                                        start = 28,
                                        end = 44,
                                    ),
                                ),
                            alternate =
                                listOf(
                                    ExpressionStatement(
                                        expression =
                                            CallExpression(
                                                callee = Identifier(name = "println",, start = 64, end = 71),
                                                arguments = listOf(StringLiteral(value = "else b",, start = 72, end = 80)),,
                                                start = 64,
                                                end = 81,
                                            ),,
                                        start = 64,
                                        end = 82,
                                    ),
                                ),
                            ,
                            start = 18,
                            end = 88,
                        ),
                    ),
                alternate =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "println",, start = 100, end = 107),
                                    arguments = listOf(StringLiteral(value = "else a",, start = 108, end = 116)),,
                                    start = 100,
                                    end = 117,
                                ),,
                            start = 100,
                            end = 118,
                        ),
                    ),,
                start = 7,
                end = 120,
            )
        val formatter = FormatterImpl()
        assertEquals(
            "if(a) {\n" +
                "    if(b) {\n" +
                "        \n" +
                "        println(\"If b\");\n" +
                "    } else {\n" +
                "        \n" +
                "        println(\"else b\");\n" +
                "    }\n" +
                "} else {\n" +
                "    \n" +
                "    println(\"else a\");\n" +
                "}\n",
            formatter.format(astNode, testConfigJsonPath, "1.1.0"),
        )
    }

    @Test
    fun conditionalWithoutelseAndTwoSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
                "identationInsideConditionals" to 2,
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left = NumberLiteral(value = 1.toBigDecimal(),, start = 13, end = 14),
                                    right = NumberLiteral(value = 1.toBigDecimal(),, start = 15, end = 16),
                                    operator = "+",,
                                    start = 13,
                                    end = 16,
                                ),,
                            start = 13,
                            end = 17,
                        ),
                    ),
                alternate = emptyList(),,
                start = 7,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertEquals(
            "if(a) {\n" +
                "        1 + 1;\n" +
                "}",
            formatter.format(astNode, testConfigJsonPath, "1.1.0"),
        )
    }

    @Test
    fun conditionalWithElseAndTwoSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to 1,
                "spaceAfterColon" to 1,
                "spacesInAssignSymbol" to 1,
                "lineJumpBeforePrintln" to 1,
                "identationInsideConditionals" to 2,
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left = NumberLiteral(value = 1.toBigDecimal(),, start = 13, end = 14),
                                    right = NumberLiteral(value = 1.toBigDecimal(),, start = 15, end = 16),
                                    operator = "+",,
                                    start = 13,
                                    end = 16,
                                ),,
                            start = 13,
                            end = 17,
                        ),
                    ),
                alternate =
                    listOf(
                        ExpressionStatement(
                            expression =
                                CallExpression(
                                    callee = Identifier(name = "print",, start = 29, end = 36),
                                    arguments = listOf(StringLiteral(value = "Hello",, start = 37, end = 44)),,
                                    start = 29,
                                    end = 45,
                                ),,
                            start = 29,
                            end = 46,
                        ),
                    ),,
                start = 7,
                end = 48,
            )
        val formatter = FormatterImpl()
        assertEquals(
            "if(a) {\n" +
                "        1 + 1;\n" +
                "} else {\n" +
                "        \n" +
                "        print(\"Hello\");\n" +
                "}\n",
            formatter.format(astNode, testConfigJsonPath, "1.1.0"),
        )
    }

    @Test
    fun conditionalWithoutElseAndNoneSpaces() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
                "identationInsideConditionals" to "None",
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left = NumberLiteral(value = 1.toBigDecimal(),, start = 13, end = 14),
                                    right = NumberLiteral(value = 1.toBigDecimal(),, start = 15, end = 16),
                                    operator = "+",,
                                    start = 13,
                                    end = 16,
                                ),,
                            start = 13,
                            end = 17,
                        ),
                    ),
                alternate = emptyList(),,
                start = 7,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertEquals(
            "if(a) {\n" +
                "    1 + 1;\n" +
                "}",
            formatter.format(astNode, testConfigJsonPath, "1.1.0"),
        )
    }

    @Test
    fun conditionalStatementWithPreviousVersionSetShouldThrowUnknownTokenType() {
        createTestConfigJson(
            mapOf(
                "spaceBeforeColon" to "none",
                "spaceAfterColon" to "none",
                "spacesInAssignSymbol" to "none",
                "lineJumpBeforePrintln" to "none",
                "identationInsideConditionals" to "None",
            ),
        )
        val astNode =
            ConditionalStatement(
                test = Identifier(name = "a",, start = 4, end = 5),
                consequent =
                    listOf(
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left = NumberLiteral(value = 1.toBigDecimal(),, start = 13, end = 14),
                                    right = NumberLiteral(value = 1.toBigDecimal(),, start = 15, end = 16),
                                    operator = "+",,
                                    start = 13,
                                    end = 16,
                                ),,
                            start = 13,
                            end = 17,
                        ),
                    ),
                alternate = emptyList(),,
                start = 7,
                end = 19,
            )
        val formatter = FormatterImpl()
        assertThrows(IllegalArgumentException::class.java) {
            formatter.format(astNode, testConfigJsonPath, "1.0.0")
        }
    }
}

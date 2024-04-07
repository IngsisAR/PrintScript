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
                        callee = Identifier(name = "print", start = 0, end = 5),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(), start = 6, end = 7),
                                    right = NumberLiteral(value = 4.toBigDecimal(), start = 10, end = 11),
                                    operator = "+",
                                    start = 6,
                                    end = 11,
                                ),
                            ),
                        start = 0,
                        end = 12,
                    ),
                start = 0,
                end = 13,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("\nprint(5 + 4);\n", formatter.format(ast))
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
                        callee = Identifier(name = "print", start = 0, end = 5),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(), start = 6, end = 7),
                                    right = NumberLiteral(value = 4.toBigDecimal(), start = 10, end = 11),
                                    operator = "+",
                                    start = 6,
                                    end = 11,
                                ),
                            ),
                        start = 0,
                        end = 12,
                    ),
                start = 0,
                end = 13,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("\n\nprint(5 + 4);\n", formatter.format(ast))
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
                        callee = Identifier(name = "print", start = 0, end = 5),
                        arguments =
                            listOf(
                                BinaryExpression(
                                    left = NumberLiteral(value = 5.toBigDecimal(), start = 6, end = 7),
                                    right = NumberLiteral(value = 4.toBigDecimal(), start = 10, end = 11),
                                    operator = "+",
                                    start = 6,
                                    end = 11,
                                ),
                            ),
                        start = 0,
                        end = 12,
                    ),
                start = 0,
                end = 13,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("print(5 + 4);\n", formatter.format(ast))
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
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "number", start = 7, end = 13),
                            init = NumberLiteral(value = 1.toBigDecimal(), start = 16, end = 18),
                            start = 4,
                            end = 18,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 19,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("let a : number = 1;\n", formatter.format(ast))
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
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "number", start = 7, end = 13),
                            init = NumberLiteral(value = 1.toBigDecimal(), start = 16, end = 18),
                            start = 4,
                            end = 18,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 19,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("let a  :  number  =  1;\n", formatter.format(ast))
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
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "number", start = 7, end = 13),
                            init = NumberLiteral(value = 1.toBigDecimal(), start = 16, end = 18),
                            start = 4,
                            end = 18,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 19,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("let a:number=1;\n", formatter.format(ast))
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
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init = StringLiteral(value = "Hello World", start = 16, end = 29),
                            start = 4,
                            end = 29,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 30,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("let a : string = \"Hello World\";\n", formatter.format(ast))
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
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init = StringLiteral(value = "Hello World", start = 16, end = 29),
                            start = 4,
                            end = 29,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 30,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("let a  :  string  =  \"Hello World\";\n", formatter.format(ast))
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
                            id = Identifier(name = "a", start = 4, end = 5),
                            type = TypeReference(type = "string", start = 7, end = 13),
                            init = StringLiteral(value = "Hello World", start = 16, end = 29),
                            start = 4,
                            end = 29,
                        ),
                    ),
                kind = "let",
                start = 0,
                end = 30,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("let a:string=\"Hello World\";\n", formatter.format(ast))
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
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = NumberLiteral(value = 2.toBigDecimal(), start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("a = 2;\n", formatter.format(ast))
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
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = NumberLiteral(value = 2.toBigDecimal(), start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        val formatter = FormatterImpl(testConfigJsonPath)
        assertEquals("a=2;\n", formatter.format(ast))
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
        val ast = NumberLiteral(value = 2.toBigDecimal(), start = 0, end = 1)
        val formatter = FormatterImpl(testConfigJsonPath)
        assertThrows(IllegalArgumentException::class.java) {
            formatter.format(ast)
        }
    }
}

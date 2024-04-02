import formatter.FormatterImpl
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertThrows
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {
    @Test
    fun printWithSingleValue() {
        copyJsonAndModifyAttribute(1, 1, 1, 1)
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
        val formatter = FormatterImpl("src/main/resources/FormatterTestConfig.json")
        assertEquals("\nprint(5 + 4);\n", formatter.format(ast))
        deleteTestJson()
    }

    @Test
    fun printWithNoneValue() {
        copyJsonAndModifyAttribute(1, 1, 1, 0)
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
        val formatter = FormatterImpl("src/main/resources/FormatterTestConfig.json")
        assertEquals("print(5 + 4);\n", formatter.format(ast))
        deleteTestJson()
    }

    @Test
    fun numberVariableDeclaratorWithSingleValue() {
        copyJsonAndModifyAttribute(1, 1, 1, 1)
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
        val formatter = FormatterImpl("src/main/resources/FormatterTestConfig.json")
        assertEquals("let a : number = 1;\n", formatter.format(ast))
        deleteTestJson()
    }

    @Test
    fun stringVariableDeclaratorWithSingleValue() {
        copyJsonAndModifyAttribute(1, 1, 1, 1)
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
        val formatter = FormatterImpl("src/main/resources/FormatterTestConfig.json")
        assertEquals("let a : string = \"Hello World\";\n", formatter.format(ast))
        deleteTestJson()
    }

    @Test
    fun assignationStatementWithSingleValue() {
        copyJsonAndModifyAttribute(1, 1, 1, 1)
        val ast =
            ExpressionStatement(
                expression =
                    AssigmentExpression(
                        left = Identifier(name = "a", start = 0, end = 1),
                        right = NumberLiteral(value = 2.toBigDecimal(), start = 4, end = 5),
                        start = 0,
                        end = 5,
                    ),
                start = 0,
                end = 6,
            )
        val formatter = FormatterImpl("src/main/resources/FormatterTestConfig.json")
        assertEquals("a = 2;\n", formatter.format(ast))
        deleteTestJson()
    }

    @Test
    fun errorUnknownASTNodeType() {
        copyJsonAndModifyAttribute(1, 1, 1, 1)
        val ast = NumberLiteral(value = 2.toBigDecimal(), start = 0, end = 1)
        val formatter = FormatterImpl("src/main/resources/FormatterTestConfig.json")
        val expected = IllegalArgumentException("Unknown ASTNode type")
        assertThrows(IllegalArgumentException::class.java) {
            formatter.format(ast)
        }
        deleteTestJson()
    }

    private fun copyJsonAndModifyAttribute(
        spaceBeforeColonValue: Int,
        spaceAfterColonValue: Int,
        spacesInAssignSymbolValue: Int,
        lineJumpBeforePrintlnValue: Int,
    ) {
        val path = Paths.get("src/main/resources/FormatterConfig.json")
        val json = String(Files.readAllBytes(path))
        val jsonObject = JSONObject(json)

        jsonObject.put("spaceBeforeColon", spaceBeforeColonValue)
        jsonObject.put("spaceAfterColon", spaceAfterColonValue)
        jsonObject.put("spacesInAssignSymbol", spacesInAssignSymbolValue)
        jsonObject.put("lineJumpBeforePrintln", lineJumpBeforePrintlnValue)
        Files.write(Path("src/main/resources/FormatterTestConfig.json"), jsonObject.toString().toByteArray())
    }

    private fun deleteTestJson() {
        val file = File("src/main/resources/FormatterTestConfig.json")
        file.delete()
    }
}

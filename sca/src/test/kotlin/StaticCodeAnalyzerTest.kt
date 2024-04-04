import org.json.JSONObject
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.test.assertEquals

class StaticCodeAnalyzerTest {
    private val testConfigJsonPath = "src/test/resources/SCATestConfig.json"

    private fun createTestConfigJson(
        printlnNoExpressionArguments: Boolean,
        identifierCasing: String,
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("printlnNoExpressionArguments", printlnNoExpressionArguments)
        jsonObject.put("identifierCasing", identifierCasing)
        Files.write(Path(testConfigJsonPath), jsonObject.toString().toByteArray(), StandardOpenOption.CREATE)
    }

    @Test
    fun analysisOf_goodIdentifier_with_camelCasingConfiguration_resultsInEmptyMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifierExample1", 10, 27)
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_badIdentifier_with_camelCasingConfiguration_resultsInMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifier_example", 10, 27)
        val lineIndex = 0
        assertEquals(sca.analyze(ast, lineIndex).trim(), "Identifier: ${ast.name} is not in camel case at (${lineIndex + 1}:${ast.start})")
    }

    @Test
    fun analysisOf_goodIdentifier_with_snakeCasingConfiguration_resultsInEmptyMessage() {
        createTestConfigJson(true, "snake case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifier_example", 10, 27)
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_badIdentifier_with_snakeCasingConfiguration_resultsInMessage() {
        createTestConfigJson(true, "snake case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifierExample", 10, 27)
        val lineIndex = 0
        assertEquals(sca.analyze(ast, lineIndex).trim(), "Identifier: ${ast.name} is not in snake case at (${lineIndex + 1}:${ast.start})")
    }

    @Test
    fun analysisOf_goodCallExpression_with_printlnNoExpressionArgumentsConfigurationSetTrue_resultsInEmptyMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = CallExpression(Identifier("println", 0, 7), emptyList(), 0, 12)
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_badCallExpression_with_printlnNoExpressionArgumentsConfigurationSetTrue_resultsInMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            CallExpression(
                Identifier("println", 0, 7),
                listOf(BinaryExpression(NumberLiteral(1, 8, 9), NumberLiteral(2, 10, 11), "+", 9, 11)),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals(
            sca.analyze(ast, lineIndex).trim(),
            "No expressions in println function rule violated at (${lineIndex + 1}:${ast.arguments[0].start})",
        )
    }

    @Test
    fun analysisOf_goodCallExpression_with_printlnNoExpressionArgumentsConfigurationSetFalse_resultsInEmptyMessage() {
        createTestConfigJson(false, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            CallExpression(
                Identifier("println", 0, 7),
                listOf(BinaryExpression(NumberLiteral(1, 8, 9), NumberLiteral(2, 10, 11), "+", 9, 11)),
                0,
                12,
            )
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_callExpressionWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            CallExpression(
                Identifier("Println", 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 8, 9),
                        NumberLiteral(2, 10, 11),
                        "+",
                        9,
                        11,
                    ),
                ),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals(
            sca.analyze(ast, lineIndex).trim(),
            "Identifier: ${ast.callee.name} is not in camel case at (${lineIndex + 1}:${ast.callee.start})",
        )
    }

    @Test
    fun analysisOf_variableDeclarationWithGoodIdentifiers_resultsInEmptyMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            VariableDeclaration(
                listOf(
                    VariableDeclarator(
                        Identifier("variableExample2", 1, 17),
                        TypeReference("Number", 18, 24),
                        Identifier("variableExample", 26, 42),
                        26,
                        28,
                    ),
                ),
                "let",
                0,
                28,
            )
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_variableDeclarationWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            VariableDeclaration(
                listOf(
                    VariableDeclarator(
                        Identifier("variable_example", 1, 17),
                        TypeReference("Number", 18, 24),
                        null,
                        26,
                        28,
                    ),
                ),
                "let",
                0,
                28,
            )
        val lineIndex = 0
        assertEquals(
            sca.analyze(ast, lineIndex).trim(),
            "Identifier: ${ast.declarations[0].id.name} is not in camel case at (${lineIndex + 1}:${ast.declarations[0].id.start})",
        )
    }

    @Test
    fun analysisOf_expressionStatementWithGoodIdentifier_resultsInEmptyMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            ExpressionStatement(
                BinaryExpression(
                    Identifier("variableExample", 1, 17),
                    NumberLiteral(1, 18, 19),
                    "+",
                    17,
                    19,
                ),
                0,
                19,
            )
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_expressionStatementWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            ExpressionStatement(
                Identifier("variable_example", 0, 16),
                0,
                17,
            )
        val identifier = ast.expression as Identifier
        val lineIndex = 0
        assertEquals(
            sca.analyze(ast, lineIndex).trim(),
            "Identifier: ${identifier.name} is not in camel case at (${lineIndex + 1}:${identifier.start})",
        )
    }

    @Test
    fun analysisOf_expressionStatement_withPrintlnCallExpression_and_PrintlnNoExpressionArgumentsSetTrue_resultsInMessage() {
        createTestConfigJson(true, "camel case")
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            ExpressionStatement(
                CallExpression(
                    Identifier("println", 0, 7),
                    emptyList(),
                    0,
                    12,
                ),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals(sca.analyze(ast, lineIndex).trim(), "")
    }
}

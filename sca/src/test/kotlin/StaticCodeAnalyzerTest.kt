import org.json.JSONObject
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.test.assertEquals

class StaticCodeAnalyzerTest {
    private val testConfigJsonPath = "src/test/resources/SCATestConfig.json"

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
    fun analysisOf_goodIdentifier_with_camelCasingConfiguration_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifierExample1", 10, 27)
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_badIdentifier_with_camelCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifier_example", 10, 27)
        val lineIndex = 0
        assertEquals(sca.analyze(ast, lineIndex).trim(), "Identifier: ${ast.name} is not in camel case at (${lineIndex + 1}:${ast.start})")
    }

    @Test
    fun analysisOf_goodIdentifier_with_snakeCasingConfiguration_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "snake case",
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifier_example", 10, 27)
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_badIdentifier_with_snakeCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "snake case",
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifierExample", 10, 27)
        val lineIndex = 0
        assertEquals(sca.analyze(ast, lineIndex).trim(), "Identifier: ${ast.name} is not in snake case at (${lineIndex + 1}:${ast.start})")
    }

    @Test
    fun analysisOf_goodCallExpression_with_printlnNoExpressionArgumentsConfigurationSetTrue_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to true,
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = CallExpression(Identifier("println", 0, 7), emptyList(), 0, 12)
        val lineIndex = 0
        assert(sca.analyze(ast, lineIndex) == "")
    }

    @Test
    fun analysisOf_badCallExpression_with_printlnNoExpressionArgumentsConfigurationSetTrue_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to true,
            ),
        )
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
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to false,
            ),
        )
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
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
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
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
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
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
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
            "Identifier: ${ast.declarations[0].id.name} is not in camel case at (${lineIndex + 1}:${ast.declarations[0].id.start})",
            sca.analyze(ast, lineIndex).trim(),
        )
    }

    @Test
    fun analysisOf_expressionStatementWithGoodIdentifier_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
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
        assertEquals("", sca.analyze(ast, lineIndex))
    }

    @Test
    fun analysisOf_expressionStatementWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
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
            "Identifier: ${identifier.name} is not in camel case at (${lineIndex + 1}:${identifier.start})",
            sca.analyze(ast, lineIndex).trim(),
        )
    }

    @Test
    fun analysisOf_expressionStatement_withPrintlnCallExpression_and_PrintlnNoExpressionArgumentsSetTrue_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to true,
            ),
        )
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
        assertEquals("", sca.analyze(ast, lineIndex).trim())
    }

    @Test
    fun analysisOf_expressionStatement_withPrintlnCallExpression_and_PrintlnNoExpressionArgumentsSetFalse_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to false,
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            ExpressionStatement(
                CallExpression(
                    Identifier("println", 0, 7),
                    listOf(BinaryExpression(NumberLiteral(1, 8, 9), NumberLiteral(2, 10, 11), "+", 9, 11)),
                    0,
                    12,
                ),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals("", sca.analyze(ast, lineIndex).trim())
    }

    @Test
    fun analysisOf_printlnCallExpression_with_printlnNoExpressionArgumentsRuleValueInString_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to "true",
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            CallExpression(
                Identifier("println", 0, 7),
                emptyList(),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals(
            "Invalid printlnNoExpressionArguments configuration value: \"true\", expected a boolean",
            sca.analyze(ast, lineIndex).trim(),
        )
    }

    @Test
    fun analysisOf_printlnCallExpression_with_printlnNoExpressionArgumentsRuleValueInInteger_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to 1,
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            CallExpression(
                Identifier("println", 0, 7),
                emptyList(),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals(
            "Invalid printlnNoExpressionArguments configuration value: 1, expected a boolean",
            sca.analyze(ast, lineIndex).trim(),
        )
    }

    @Test
    fun analysisOf_badCasingInIdentifierCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "kebab case",
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifierExample1", 10, 27)
        val lineIndex = 0
        assertEquals(
            "Invalid identifier casing configuration: kebab case, expected one of: camel case, snake case",
            sca.analyze(ast, lineIndex).trim(),
        )
    }

    @Test
    fun analysisOf_badValueInIdentifierCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to 1,
            ),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifierExample1", 10, 27)
        val lineIndex = 0
        assertEquals(
            "Invalid identifier casing configuration: 1, expected one of: camel case, snake case",
            sca.analyze(ast, lineIndex).trim(),
        )
    }

    @Test
    fun analysisOf_Identifier_with_NoConfigurationForIdentifierCasing_expectsCamelCaseByDefault() {
        createTestConfigJson(
            emptyMap(),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast = Identifier("identifier_example", 10, 27)
        val lineIndex = 0
        assertEquals(sca.analyze(ast, lineIndex).trim(), "Identifier: ${ast.name} is not in camel case at (${lineIndex + 1}:${ast.start})")
    }

    @Test
    fun analysisOf_printlnCallExpression_with_NoConfigurationForExpressionsInArguments_expectsTrueByDefault() {
        createTestConfigJson(
            emptyMap(),
        )
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
    fun analysisOf_readInputCallExpression_with_NoConfigurationForExpressionsInArguments_expectsTrueByDefault() {
        createTestConfigJson(
            emptyMap(),
        )
        val sca = StaticCodeAnalyzer(testConfigJsonPath)
        val ast =
            CallExpression(
                Identifier("readInput", 0, 7),
                listOf(BinaryExpression(NumberLiteral(1, 8, 9), NumberLiteral(2, 10, 11), "+", 9, 11)),
                0,
                12,
            )
        val lineIndex = 0
        assertEquals(
            sca.analyze(ast, lineIndex).trim(),
            "No expressions in readInput function rule violated at (${lineIndex + 1}:${ast.arguments[0].start})",
        )
    }
}

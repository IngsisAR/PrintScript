import org.json.JSONObject
import org.junit.jupiter.api.Test
import sca.StaticCodeAnalyzer
import utils.BinaryExpression
import utils.CallExpression
import utils.ExpressionStatement
import utils.Identifier
import utils.NumberLiteral
import utils.TypeReference
import utils.VariableDeclaration
import utils.VariableDeclarator
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
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifierExample1", 1, 10, 27)
        assertEquals("", sca.analyze(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun analysisOf_badIdentifier_with_camelCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifier_example", 1, 10, 27)
        assertEquals(
            "Identifier: ${ast.name} is not in camel case at (${ast.line}:${ast.start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_goodIdentifier_with_snakeCasingConfiguration_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "snake case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifier_example", 1, 10, 27)
        assertEquals("", sca.analyze(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun analysisOf_badIdentifier_with_snakeCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "snake case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifierExample", 1, 10, 27)
        assertEquals(
            "Identifier: ${ast.name} is not in snake case at (${ast.line}:${ast.start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_goodCallExpression_with_printlnNoExpressionArgumentsConfigurationSetTrue_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to true,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast = CallExpression(Identifier("println", 1, 0, 7), emptyList(), 1, 0, 12)
        assert(sca.analyze(ast, testConfigJsonPath, "1.1.0") == "")
    }

    @Test
    fun analysisOf_badCallExpression_with_printlnNoExpressionArgumentsConfigurationSetTrue_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to true,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("println", 1, 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 1, 8, 9),
                        NumberLiteral(2, 1, 10, 11),
                        "+",
                        1,
                        9,
                        11,
                    ),
                ),
                1,
                0,
                12,
            )
        assertEquals(
            "No expressions in println function rule violated at (${ast.arguments[0].line}:${ast.arguments[0].start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_goodCallExpression_with_printlnNoExpressionArgumentsConfigurationSetFalse_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to false,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("println", 1, 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 1, 8, 9),
                        NumberLiteral(2, 1, 10, 11),
                        "+",
                        1,
                        9,
                        11,
                    ),
                ),
                1,
                0,
                12,
            )
        assert(sca.analyze(ast, testConfigJsonPath, "1.1.0") == "")
    }

    @Test
    fun analysisOf_callExpressionWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("Println", 1, 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 1, 8, 9),
                        NumberLiteral(2, 1, 10, 11),
                        "+",
                        1,
                        9,
                        11,
                    ),
                ),
                1,
                0,
                12,
            )
        assertEquals(
            "Identifier: ${ast.callee.name} is not in camel case at (${ast.callee.line}:${ast.callee.start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_variableDeclarationWithGoodIdentifiers_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            VariableDeclaration(
                listOf(
                    VariableDeclarator(
                        Identifier("variableExample2", 1, 1, 17),
                        TypeReference("Number", 1, 18, 24),
                        Identifier("variableExample", 1, 26, 42),
                        1,
                        26,
                        28,
                    ),
                ),
                "let",
                1,
                0,
                28,
            )
        assert(sca.analyze(ast, testConfigJsonPath, "1.1.0") == "")
    }

    @Test
    fun analysisOf_variableDeclarationWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            VariableDeclaration(
                listOf(
                    VariableDeclarator(
                        Identifier("variable_example", 1, 1, 17),
                        TypeReference("Number", 1, 18, 24),
                        null,
                        1,
                        26,
                        28,
                    ),
                ),
                "let",
                1,
                0,
                28,
            )
        assertEquals(
            "Identifier: ${ast.declarations[0].id.name} is not in camel case at " +
                "(${ast.declarations[0].id.line}:${ast.declarations[0].id.start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_expressionStatementWithGoodIdentifier_resultsInEmptyMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            ExpressionStatement(
                BinaryExpression(
                    Identifier("variableExample", 1, 1, 17),
                    NumberLiteral(1, 1, 18, 19),
                    "+",
                    1,
                    17,
                    19,
                ),
                1,
                0,
                19,
            )
        assertEquals("", sca.analyze(ast, testConfigJsonPath, "1.1.0"))
    }

    @Test
    fun analysisOf_expressionStatementWithBadIdentifier_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "camel case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            ExpressionStatement(
                Identifier("variable_example", 1, 0, 16),
                1,
                0,
                17,
            )
        val identifier = ast.expression as Identifier
        assertEquals(
            "Identifier: ${identifier.name} is not in camel case at (${identifier.line}:${identifier.start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_expressionStatement_withPrintlnCallExpression_and_PrintlnNoExpressionArgumentsSetTrue_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to true,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            ExpressionStatement(
                CallExpression(
                    Identifier("println", 1, 0, 7),
                    emptyList(),
                    1,
                    0,
                    12,
                ),
                1,
                0,
                12,
            )
        assertEquals("", sca.analyze(ast, testConfigJsonPath, "1.1.0").trim())
    }

    @Test
    fun analysisOf_expressionStatement_withPrintlnCallExpression_and_PrintlnNoExpressionArgumentsSetFalse_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to false,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            ExpressionStatement(
                CallExpression(
                    Identifier("println", 1, 0, 7),
                    listOf(
                        BinaryExpression(
                            NumberLiteral(1, 1, 8, 9),
                            NumberLiteral(2, 1, 10, 11),
                            "+",
                            1,
                            9,
                            11,
                        ),
                    ),
                    1,
                    0,
                    12,
                ),
                1,
                0,
                12,
            )
        assertEquals("", sca.analyze(ast, testConfigJsonPath, "1.1.0").trim())
    }

    @Test
    fun analysisOf_printlnCallExpression_with_printlnNoExpressionArgumentsRuleValueInString_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to "true",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("println", 1, 0, 7),
                emptyList(),
                1,
                0,
                12,
            )
        assertEquals(
            "Invalid printlnNoExpressionArguments configuration value: \"true\", expected a boolean",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_printlnCallExpression_with_printlnNoExpressionArgumentsRuleValueInInteger_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "printlnNoExpressionArguments" to 1,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("println", 1, 0, 7),
                emptyList(),
                1,
                0,
                12,
            )
        assertEquals(
            "Invalid printlnNoExpressionArguments configuration value: 1, expected a boolean",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_badCasingInIdentifierCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to "kebab case",
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifierExample1", 1, 10, 27)
        assertEquals(
            "Invalid identifier casing configuration: kebab case at (${ast.line}:${ast.start}), " +
                "expected one of: camel case, snake case",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_badValueInIdentifierCasingConfiguration_resultsInMessage() {
        createTestConfigJson(
            mapOf(
                "identifierCasing" to 1,
            ),
        )
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifierExample1", 1, 10, 27)
        assertEquals(
            "Invalid identifier casing configuration: 1 at (${ast.line}:${ast.start}), expected one of: camel case, snake case",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_Identifier_with_NoConfigurationForIdentifierCasing_expectsCamelCaseByDefault() {
        createTestConfigJson(
            emptyMap(),
        )
        val sca = StaticCodeAnalyzer()
        val ast = Identifier("identifier_example", 1, 10, 27)
        assertEquals(
            "Identifier: ${ast.name} is not in camel case at (${ast.line}:${ast.start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_printlnCallExpression_with_NoConfigurationForExpressionsInArguments_expectsTrueByDefault() {
        createTestConfigJson(
            emptyMap(),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("println", 1, 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 1, 8, 9),
                        NumberLiteral(2, 1, 10, 11),
                        "+",
                        1,
                        9,
                        11,
                    ),
                ),
                1,
                0,
                12,
            )
        assertEquals(
            "No expressions in println function rule violated at (${ast.arguments[0].line}:${ast.arguments[0].start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_readInputCallExpression_with_NoConfigurationForExpressionsInArguments_expectsTrueByDefault() {
        createTestConfigJson(
            emptyMap(),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("readInput", 1, 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 1, 8, 9),
                        NumberLiteral(2, 1, 10, 11),
                        "+",
                        1,
                        9,
                        11,
                    ),
                ),
                1,
                0,
                12,
            )
        assertEquals(
            "No expressions in readInput function rule violated at (${ast.arguments[0].line}:${ast.arguments[0].start})",
            sca.analyze(ast, testConfigJsonPath, "1.1.0").trim(),
        )
    }

    @Test
    fun analysisOf_readInputCallExpression_with_NoConfigurationForExpressionsInArgumentsAndSetPreviousVersion_expectsNoMessage() {
        createTestConfigJson(
            emptyMap(),
        )
        val sca = StaticCodeAnalyzer()
        val ast =
            CallExpression(
                Identifier("readInput", 1, 0, 7),
                listOf(
                    BinaryExpression(
                        NumberLiteral(1, 1, 8, 9),
                        NumberLiteral(2, 1, 10, 11),
                        "+",
                        1,
                        9,
                        11,
                    ),
                ),
                1,
                0,
                12,
            )
        assertEquals(
            "",
            sca.analyze(ast, testConfigJsonPath, "1.0.0").trim(),
        )
    }
}

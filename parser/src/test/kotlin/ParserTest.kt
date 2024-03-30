import TokenType.ASSIGN
import TokenType.COLON
import TokenType.COMMA
import TokenType.CPAREN
import TokenType.DIV
import TokenType.ID
import TokenType.LET
import TokenType.MUL
import TokenType.NUMBER
import TokenType.OPAREN
import TokenType.PLUS
import TokenType.SEMICOLON
import TokenType.STRING
import TokenType.TYPE
import astbuilder.ASTBuilderFailure
import astbuilder.ASTBuilderSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParserTest {
    @Test
    fun parseDeclareEmptyNumberVariable() {
        val parser = Parser()
        val token =
            listOf(
                Token(LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = SEMICOLON.toString(), position = Position(start = 13, end = 14), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id = Identifier(name = "a", start = 4, end = 5),
                                    type = TypeReference(type = "number", start = 7, end = 13),
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
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseDeclareEmptyStringVariable() {
        val parser = Parser()
        val token =
            listOf(
                Token(LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "string"),
                Token(type = SEMICOLON.toString(), position = Position(start = 13, end = 14), value = ";"),
            )
        val result =
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
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseDeclareStringVariable() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "string"),
                Token(type = ASSIGN.toString(), position = Position(start = 14, end = 15), value = "="),
                Token(type = STRING.toString(), position = Position(start = 16, end = 17), value = "A"),
                Token(type = SEMICOLON.toString(), position = Position(start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id = Identifier(name = "a", start = 4, end = 5),
                                    type = TypeReference(type = "string", start = 7, end = 13),
                                    init = StringLiteral(value = "A", start = 16, end = 17),
                                    start = 4,
                                    end = 17,
                                ),
                            ),
                        kind = "let",
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseMultiplicationOperation() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 1), value = "a"),
                Token(type = ASSIGN.toString(), position = Position(start = 2, end = 3), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 4, end = 5), value = "5"),
                Token(type = MUL.toString(), position = Position(start = 6, end = 7), value = "*"),
                Token(type = NUMBER.toString(), position = Position(start = 8, end = 9), value = "5"),
                Token(type = SEMICOLON.toString(), position = Position(start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssigmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), start = 8, end = 9),
                                        operator = "*",
                                        start = 4,
                                        end = 9,
                                    ),
                                start = 0,
                                end = 9,
                            ),
                        start = 0,
                        end = 10,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseDivisionOperation() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 1), value = "a"),
                Token(type = ASSIGN.toString(), position = Position(start = 2, end = 3), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 4, end = 5), value = "5"),
                Token(type = DIV.toString(), position = Position(start = 6, end = 7), value = "/"),
                Token(type = NUMBER.toString(), position = Position(start = 8, end = 9), value = "5"),
                Token(type = SEMICOLON.toString(), position = Position(start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssigmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), start = 8, end = 9),
                                        operator = "/",
                                        start = 4,
                                        end = 9,
                                    ),
                                start = 0,
                                end = 9,
                            ),
                        start = 0,
                        end = 10,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseDeclareNumberVariable() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "string"),
                Token(type = ASSIGN.toString(), position = Position(start = 14, end = 15), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 16, end = 17), value = "5"),
                Token(type = SEMICOLON.toString(), position = Position(start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id = Identifier(name = "a", start = 4, end = 5),
                                    type = TypeReference(type = "string", start = 7, end = 13),
                                    init = NumberLiteral(value = 5.toBigDecimal(), start = 16, end = 17),
                                    start = 4,
                                    end = 17,
                                ),
                            ),
                        kind = "let",
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parsePrintFunctionWithString() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 5), value = "print"),
                Token(type = OPAREN.toString(), position = Position(start = 5, end = 6), value = "("),
                Token(type = STRING.toString(), position = Position(start = 6, end = 19), value = "Hello world"),
                Token(type = CPAREN.toString(), position = Position(start = 19, end = 20), value = ")"),
                Token(type = SEMICOLON.toString(), position = Position(start = 20, end = 21), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            CallExpression(
                                callee =
                                    Identifier(
                                        name = "print",
                                        start = 0,
                                        end = 5,
                                    ),
                                arguments = listOf(StringLiteral(value = "Hello world", start = 6, end = 19)),
                                start = 0,
                                end = 20,
                            ),
                        start = 0,
                        end = 21,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parsePrintFunctionWithNumber() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 5), value = "print"),
                Token(type = OPAREN.toString(), position = Position(start = 5, end = 6), value = "("),
                Token(type = NUMBER.toString(), position = Position(start = 6, end = 7), value = "1"),
                Token(type = CPAREN.toString(), position = Position(start = 7, end = 8), value = ")"),
                Token(type = SEMICOLON.toString(), position = Position(start = 8, end = 9), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            CallExpression(
                                callee =
                                    Identifier(
                                        name = "print",
                                        start = 0,
                                        end = 5,
                                    ),
                                arguments = listOf(NumberLiteral(value = 1.toBigDecimal(), start = 6, end = 7)),
                                start = 0,
                                end = 8,
                            ),
                        start = 0,
                        end = 9,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseMultipleVariableDeclaration() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = ASSIGN.toString(), position = Position(start = 14, end = 15), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 16, end = 17), value = "5"),
                Token(type = COMMA.toString(), position = Position(start = 17, end = 18), value = ","),
                Token(type = ID.toString(), position = Position(start = 19, end = 20), value = "b"),
                Token(type = COLON.toString(), position = Position(start = 20, end = 21), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 22, end = 28), value = "number"),
                Token(type = ASSIGN.toString(), position = Position(start = 29, end = 30), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 31, end = 33), value = "10"),
                Token(type = SEMICOLON.toString(), position = Position(start = 33, end = 34), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id =
                                        Identifier(
                                            name = "a",
                                            start = 4,
                                            end = 5,
                                        ),
                                    type = TypeReference(type = "number", start = 7, end = 13),
                                    init = NumberLiteral(value = 5.toBigDecimal(), start = 16, end = 17),
                                    start = 4,
                                    end = 17,
                                ),
                                VariableDeclarator(
                                    id = Identifier(name = "b", start = 19, end = 20),
                                    type = TypeReference(type = "number", start = 22, end = 28),
                                    init = NumberLiteral(value = 10.toBigDecimal(), start = 31, end = 33),
                                    start = 19,
                                    end = 33,
                                ),
                            ),
                        kind = "let",
                        start = 0,
                        end = 34,
                    ),
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseDeclareBinaryOperation() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = ASSIGN.toString(), position = Position(start = 14, end = 15), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 16, end = 17), value = "5"),
                Token(type = PLUS.toString(), position = Position(start = 18, end = 19), value = "+"),
                Token(type = NUMBER.toString(), position = Position(start = 20, end = 21), value = "5"),
                Token(type = SEMICOLON.toString(), position = Position(start = 21, end = 22), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id =
                                        Identifier(
                                            name = "a",
                                            start = 4,
                                            end = 5,
                                        ),
                                    type = TypeReference(type = "number", start = 7, end = 13),
                                    init =
                                        BinaryExpression(
                                            left = NumberLiteral(value = 5.toBigDecimal(), start = 16, end = 17),
                                            right = NumberLiteral(value = 5.toBigDecimal(), start = 20, end = 21),
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
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseBinaryOperation() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 1), value = "a"),
                Token(type = ASSIGN.toString(), position = Position(start = 2, end = 3), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 4, end = 5), value = "5"),
                Token(type = PLUS.toString(), position = Position(start = 6, end = 7), value = "+"),
                Token(type = NUMBER.toString(), position = Position(start = 8, end = 9), value = "5"),
                Token(type = SEMICOLON.toString(), position = Position(start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssigmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), start = 8, end = 9),
                                        operator = "+",
                                        start = 4,
                                        end = 9,
                                    ),
                                start = 0,
                                end = 9,
                            ),
                        start = 0,
                        end = 10,
                    ),
            )

        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorForMisingSemicolon() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = ASSIGN.toString(), position = Position(start = 14, end = 15), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 16, end = 17), value = "5"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nMissing semicolon at variable declaration\n" +
                        "Missing semicolon at expression statement\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorForExtraSemicolon() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = SEMICOLON.toString(), position = Position(start = 13, end = 14), value = ";"),
                Token(type = SEMICOLON.toString(), position = Position(start = 14, end = 15), value = ";"),
            )
        try {
            parser.parse(token)
        } catch (e: IllegalStateException) {
            assertEquals("Only one line of code is allowed at a time.", e.message)
        }
    }

    @Test
    fun parseErrorForMisingColonInDeclaration() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = SEMICOLON.toString(), position = Position(start = 13, end = 14), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nInvalid variable declaration: " +
                        "Not enough tokens for a variable declarator\nInvalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorForMultipleColonInDeclaration() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = COLON.toString(), position = Position(start = 7, end = 8), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 9, end = 15), value = "number"),
                Token(type = SEMICOLON.toString(), position = Position(start = 15, end = 16), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \n" +
                        "Invalid variable declaration: Invalid declarator: Invalid type\n" +
                        "Invalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorForInvalidStatementDeclaration() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 1), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 1, end = 2), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 3, end = 9), value = "number"),
                Token(type = SEMICOLON.toString(), position = Position(start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nInvalid start of variable declaration" +
                        "\nInvalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorForEmptyTokens() {
        val parser = Parser()
        val token = listOf<Token>()
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: " +
                        "\nEmpty tokens\nEmpty tokens\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorExtraComasInDeclaration() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = COMMA.toString(), position = Position(start = 13, end = 14), value = ","),
                Token(type = COMMA.toString(), position = Position(start = 14, end = 15), value = ","),
                Token(type = SEMICOLON.toString(), position = Position(start = 15, end = 16), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nInvalid variable declaration: " +
                        "Not enough tokens for a variable declarator\nInvalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorFewerComasInDeclaration() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = LET.toString(), position = Position(start = 0, end = 3), value = "let"),
                Token(type = ID.toString(), position = Position(start = 4, end = 5), value = "a"),
                Token(type = COLON.toString(), position = Position(start = 5, end = 6), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 7, end = 13), value = "number"),
                Token(type = ASSIGN.toString(), position = Position(start = 14, end = 15), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 16, end = 17), value = "5"),
                Token(type = ID.toString(), position = Position(start = 18, end = 19), value = "b"),
                Token(type = COLON.toString(), position = Position(start = 19, end = 20), value = ":"),
                Token(type = TYPE.toString(), position = Position(start = 21, end = 27), value = "number"),
                Token(type = ASSIGN.toString(), position = Position(start = 28, end = 29), value = "="),
                Token(type = NUMBER.toString(), position = Position(start = 30, end = 32), value = "10"),
                Token(type = SEMICOLON.toString(), position = Position(start = 32, end = 33), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nInvalid variable declaration: " +
                        "Invalid declarator: No valid assignable expression found\nInvalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorMisingCloseParenthesisInPrintFunction() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 5), value = "print"),
                Token(type = OPAREN.toString(), position = Position(start = 5, end = 6), value = "("),
                Token(type = STRING.toString(), position = Position(start = 6, end = 19), value = "Hello world"),
                Token(type = SEMICOLON.toString(), position = Position(start = 19, end = 20), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nInvalid start of variable declaration" +
                        "\nInvalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }

    @Test
    fun parseErrorMisingOpenParenthesisInPrintFunction() {
        val parser = Parser()
        val token =
            listOf(
                Token(type = ID.toString(), position = Position(start = 0, end = 5), value = "print"),
                Token(type = STRING.toString(), position = Position(start = 6, end = 19), value = "Hello world"),
                Token(type = CPAREN.toString(), position = Position(start = 19, end = 20), value = ")"),
                Token(type = SEMICOLON.toString(), position = Position(start = 20, end = 21), value = ";"),
            )
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "No valid statement found, errors: \nInvalid start of variable declaration" +
                        "\nInvalid expression: No valid expression found\n",
            )
        assertEquals(result, parser.parse(token))
    }
}

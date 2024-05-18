import astbuilder.ASTBuilderFailure
import astbuilder.ASTBuilderSuccess
import astbuilder.ASTProviderFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParserTest {
    @Test
    fun parseDeclareEmptyNumberVariable() {
        val parser = Parser()
        val tokens =

            listOf(
                Token("LET", position = Position(1, start = 0, end = 3), value = "let"),
                Token("ID", position = Position(1, start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 13, end = 14), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id = Identifier(name = "a", line = 1, start = 4, end = 5),
                                    type = TypeReference(type = "number", line = 1, start = 7, end = 13),
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
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseDeclareEmptyStringVariable() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token("ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "string"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 13, end = 14), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
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
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseDeclareStringVariable() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token("ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "string"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token(type = "STRING", position = Position(line = 1, start = 16, end = 17), value = "A"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id = Identifier(name = "a", line = 1, start = 4, end = 5),
                                    type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                                    init = StringLiteral(value = "A", line = 1, start = 16, end = 17),
                                    line = 1,
                                    start = 4,
                                    end = 17,
                                ),
                            ),
                        kind = "let",
                        line = 1,
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsingGoodAssignmentExpressionShouldBeASTBuilderSuccess() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 4, end = 5), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 5, end = 6), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        line = 1,
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    NumberLiteral(
                                        value = 5.toBigDecimal(),
                                        line = 1,
                                        start = 4,
                                        end = 5,
                                    ),
                                line = 1,
                                start = 0,
                                end = 5,
                            ),
                        line = 1,
                        start = 0,
                        end = 6,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsingAssignmentExpressionWithMissingIdentifierShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ASSIGN", position = Position(line = 1, start = 0, end = 1), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 2, end = 3), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 3, end = 4), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals("Invalid expression: missing identifier at (1:0)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingAssignmentExpressionWithMissingAssignmentExpressionShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token("SEMICOLON", position = Position(line = 1, start = 3, end = 4), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid expression: missing expression after assignment at (1:3)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseMultiplicationOperation() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 4, end = 5), value = "5"),
                Token("MUL", position = Position(line = 1, start = 6, end = 7), value = "*"),
                Token("NUMBER", position = Position(line = 1, start = 8, end = 9), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        line = 1,
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 8, end = 9),
                                        operator = "*",
                                        line = 1,
                                        start = 4,
                                        end = 9,
                                    ),
                                line = 1,
                                start = 0,
                                end = 9,
                            ),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseDivisionOperation() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 4, end = 5), value = "5"),
                Token("DIV", position = Position(line = 1, start = 6, end = 7), value = "/"),
                Token("NUMBER", position = Position(line = 1, start = 8, end = 9), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        line = 1,
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 8, end = 9),
                                        operator = "/",
                                        line = 1,
                                        start = 4,
                                        end = 9,
                                    ),
                                line = 1,
                                start = 0,
                                end = 9,
                            ),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsePlusOperationInExpressionStatement() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 4, end = 5), value = "5"),
                Token(type = "PLUS", position = Position(line = 1, start = 6, end = 7), value = "+"),
                Token(type = "NUMBER", position = Position(line = 1, start = 8, end = 9), value = "5"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        line = 1,
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 8, end = 9),
                                        operator = "+",
                                        line = 1,
                                        start = 4,
                                        end = 9,
                                    ),
                                line = 1,
                                start = 0,
                                end = 9,
                            ),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
            )

        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseMinusOperationInExpressionStatement() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 4, end = 5), value = "5"),
                Token("MINUS", position = Position(line = 1, start = 6, end = 7), value = "-"),
                Token("NUMBER", position = Position(line = 1, start = 8, end = 9), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 9, end = 10), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left =
                                    Identifier(
                                        name = "a",
                                        line = 1,
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 8, end = 9),
                                        operator = "-",
                                        line = 1,
                                        start = 4,
                                        end = 9,
                                    ),
                                line = 1,
                                start = 0,
                                end = 9,
                            ),
                        line = 1,
                        start = 0,
                        end = 10,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseBinaryExpressionWithAllOperatorsInStatementExpression() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(line = 1, start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 4, end = 5), value = "1"),
                Token("PLUS", position = Position(line = 1, start = 6, end = 7), value = "+"),
                Token("NUMBER", position = Position(line = 1, start = 8, end = 9), value = "2"),
                Token("MUL", position = Position(line = 1, start = 10, end = 11), value = "*"),
                Token("NUMBER", position = Position(line = 1, start = 12, end = 13), value = "3"),
                Token("DIV", position = Position(line = 1, start = 14, end = 15), value = "/"),
                Token("NUMBER", position = Position(line = 1, start = 16, end = 17), value = "4"),
                Token("MINUS", position = Position(line = 1, start = 17, end = 18), value = "-"),
                Token("NUMBER", position = Position(line = 1, start = 19, end = 20), value = "5"),
                Token("MODULE", position = Position(line = 1, start = 21, end = 22), value = "%"),
                Token("NUMBER", position = Position(line = 1, start = 23, end = 24), value = "6"),
                Token("SEMICOLON", position = Position(line = 1, start = 24, end = 25), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left = Identifier(name = "a", line = 1, start = 0, end = 1),
                                right =
                                    BinaryExpression(
                                        left =
                                            BinaryExpression(
                                                left = NumberLiteral(value = 1.toBigDecimal(), line = 1, start = 4, end = 5),
                                                right =
                                                    BinaryExpression(
                                                        left =
                                                            BinaryExpression(
                                                                left =
                                                                    NumberLiteral(
                                                                        value = 2.toBigDecimal(),
                                                                        line = 1,
                                                                        start = 8,
                                                                        end = 9,
                                                                    ),
                                                                right =
                                                                    NumberLiteral(
                                                                        value = 3.toBigDecimal(),
                                                                        line = 1,
                                                                        start = 12,
                                                                        end = 13,
                                                                    ),
                                                                operator = "*",
                                                                line = 1,
                                                                start = 8,
                                                                end = 13,
                                                            ),
                                                        right = NumberLiteral(value = 4.toBigDecimal(), line = 1, start = 16, end = 17),
                                                        operator = "/",
                                                        line = 1,
                                                        start = 8,
                                                        end = 17,
                                                    ),
                                                operator = "+",
                                                line = 1,
                                                start = 4,
                                                end = 17,
                                            ),
                                        right =
                                            BinaryExpression(
                                                left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 19, end = 20),
                                                right = NumberLiteral(value = 6.toBigDecimal(), line = 1, start = 23, end = 24),
                                                operator = "%",
                                                line = 1,
                                                start = 19,
                                                end = 24,
                                            ),
                                        operator = "-",
                                        line = 1,
                                        start = 4,
                                        end = 24,
                                    ),
                                line = 1,
                                start = 0,
                                end = 24,
                            ),
                        line = 1,
                        start = 0,
                        end = 25,
                    ),
            )

        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseBinaryExpressionsWithFunctionIdentifierAndNumbersInStatementExpression() {
        val parser = Parser()
        // function(1 + 2) * a / (4 + 5)
        val tokens =
            listOf(
                Token("ID", position = Position(line = 1, start = 0, end = 8), value = "function"),
                Token("OPAREN", position = Position(line = 1, start = 8, end = 9), value = "("),
                Token("NUMBER", position = Position(line = 1, start = 9, end = 10), value = "1"),
                Token("PLUS", position = Position(line = 1, start = 11, end = 12), value = "+"),
                Token("NUMBER", position = Position(line = 1, start = 13, end = 14), value = "2"),
                Token("CPAREN", position = Position(line = 1, start = 14, end = 15), value = ")"),
                Token("MUL", position = Position(line = 1, start = 16, end = 17), value = "*"),
                Token("ID", position = Position(line = 1, start = 18, end = 19), value = "a"),
                Token("DIV", position = Position(line = 1, start = 20, end = 21), value = "/"),
                Token("OPAREN", position = Position(line = 1, start = 22, end = 23), value = "("),
                Token("NUMBER", position = Position(line = 1, start = 23, end = 24), value = "4"),
                Token("PLUS", position = Position(line = 1, start = 25, end = 26), value = "-"),
                Token("NUMBER", position = Position(line = 1, start = 27, end = 28), value = "5"),
                Token("CPAREN", position = Position(line = 1, start = 28, end = 29), value = ")"),
                Token("SEMICOLON", position = Position(line = 1, start = 29, end = 30), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            BinaryExpression(
                                left =
                                    BinaryExpression(
                                        left =
                                            CallExpression(
                                                callee = Identifier(name = "function", line = 1, start = 0, end = 8),
                                                arguments =
                                                    listOf(
                                                        BinaryExpression(
                                                            left = NumberLiteral(value = 1.toBigDecimal(), line = 1, start = 9, end = 10),
                                                            right = NumberLiteral(value = 2.toBigDecimal(), line = 1, start = 13, end = 14),
                                                            operator = "+",
                                                            line = 1,
                                                            start = 9,
                                                            end = 14,
                                                        ),
                                                    ),
                                                line = 1,
                                                start = 0,
                                                end = 15,
                                            ),
                                        right = Identifier(name = "a", line = 1, start = 18, end = 19),
                                        operator = "*",
                                        line = 1,
                                        start = 0,
                                        end = 19,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 4.toBigDecimal(), line = 1, start = 23, end = 24),
                                        right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 27, end = 28),
                                        operator = "-",
                                        line = 1,
                                        start = 23,
                                        end = 28,
                                    ),
                                operator = "/",
                                line = 1,
                                start = 0,
                                end = 28,
                            ),
                        line = 1,
                        start = 0,
                        end = 30,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseBinaryExpressionWithStringAndNumberInStatementExpression() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("STRING", position = Position(line = 1, start = 0, end = 13), value = "Hello world"),
                Token("PLUS", position = Position(line = 1, start = 14, end = 15), value = "+"),
                Token("NUMBER", position = Position(line = 1, start = 16, end = 17), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            BinaryExpression(
                                left = StringLiteral(value = "Hello world", line = 1, start = 0, end = 13),
                                right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 16, end = 17),
                                operator = "+",
                                line = 1,
                                start = 0,
                                end = 17,
                            ),
                        line = 1,
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseBinaryExpressionWithMismatchedParenthesisInStatementExpressionReturnsFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("OPAREN", position = Position(line = 1, start = 0, end = 1), value = "("),
                Token("NUMBER", position = Position(line = 1, start = 1, end = 2), value = "1"),
                Token("PLUS", position = Position(line = 1, start = 3, end = 4), value = "+"),
                Token("NUMBER", position = Position(line = 1, start = 5, end = 6), value = "2"),
                Token("CPAREN", position = Position(line = 1, start = 6, end = 7), value = ")"),
                Token("CPAREN", position = Position(line = 1, start = 7, end = 8), value = ")"),
                Token("SEMICOLON", position = Position(line = 1, start = 8, end = 9), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals("Invalid expression: Mismatched parenthesis at (1:7)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parseDeclareNumberVariable() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token("ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token("TYPE", position = Position(line = 1, start = 7, end = 13), value = "string"),
                Token("ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token("NUMBER", position = Position(line = 1, start = 16, end = 17), value = "5"),
                Token("SEMICOLON", position = Position(line = 1, start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    VariableDeclaration(
                        declarations =
                            listOf(
                                VariableDeclarator(
                                    id = Identifier(name = "a", line = 1, start = 4, end = 5),
                                    type = TypeReference(type = "string", line = 1, start = 7, end = 13),
                                    init = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 16, end = 17),
                                    line = 1,
                                    start = 4,
                                    end = 17,
                                ),
                            ),
                        kind = "let",
                        line = 1,
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsePrintFunctionWithString() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(line = 1, start = 0, end = 5), value = "print"),
                Token(type = "OPAREN", position = Position(line = 1, start = 5, end = 6), value = "("),
                Token(type = "STRING", position = Position(line = 1, start = 6, end = 19), value = "Hello world"),
                Token(type = "CPAREN", position = Position(line = 1, start = 19, end = 20), value = ")"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 20, end = 21), value = ";"),
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
                                        line = 1,
                                        start = 0,
                                        end = 5,
                                    ),
                                arguments = listOf(StringLiteral(value = "Hello world", line = 1, start = 6, end = 19)),
                                line = 1,
                                start = 0,
                                end = 20,
                            ),
                        line = 1,
                        start = 0,
                        end = 21,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseCallExpressionWithMultipleArguments() {
        val parser = Parser()
        // function(1,2,3,4);
        val tokens =
            listOf(
                Token(type = "ID", position = Position(line = 1, start = 0, end = 8), value = "function"),
                Token(type = "OPAREN", position = Position(line = 1, start = 8, end = 9), value = "("),
                Token(type = "NUMBER", position = Position(line = 1, start = 9, end = 10), value = "1"),
                Token(type = "COMMA", position = Position(line = 1, start = 10, end = 11), value = ","),
                Token(type = "NUMBER", position = Position(line = 1, start = 11, end = 12), value = "2"),
                Token(type = "COMMA", position = Position(line = 1, start = 12, end = 13), value = ","),
                Token(type = "NUMBER", position = Position(line = 1, start = 13, end = 14), value = "3"),
                Token(type = "COMMA", position = Position(line = 1, start = 14, end = 15), value = ","),
                Token(type = "NUMBER", position = Position(line = 1, start = 15, end = 16), value = "4"),
                Token(type = "CPAREN", position = Position(line = 1, start = 16, end = 17), value = ")"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            CallExpression(
                                callee = Identifier(name = "function", line = 1, start = 0, end = 8),
                                arguments =
                                    listOf(
                                        NumberLiteral(value = 1.toBigDecimal(), line = 1, start = 9, end = 10),
                                        NumberLiteral(value = 2.toBigDecimal(), line = 1, start = 11, end = 12),
                                        NumberLiteral(value = 3.toBigDecimal(), line = 1, start = 13, end = 14),
                                        NumberLiteral(value = 4.toBigDecimal(), line = 1, start = 15, end = 16),
                                    ),
                                line = 1,
                                start = 0,
                                end = 17,
                            ),
                        line = 1,
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsePrintFunctionWithNumber() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(line = 1, start = 0, end = 5), value = "print"),
                Token(type = "OPAREN", position = Position(line = 1, start = 5, end = 6), value = "("),
                Token(type = "NUMBER", position = Position(line = 1, start = 6, end = 7), value = "1"),
                Token(type = "CPAREN", position = Position(line = 1, start = 7, end = 8), value = ")"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 8, end = 9), value = ";"),
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
                                        line = 1,
                                        start = 0,
                                        end = 5,
                                    ),
                                arguments = listOf(NumberLiteral(value = 1.toBigDecimal(), line = 1, start = 6, end = 7)),
                                line = 1,
                                start = 0,
                                end = 8,
                            ),
                        line = 1,
                        start = 0,
                        end = 9,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseMultipleVariableDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 16, end = 17), value = "5"),
                Token(type = "COMMA", position = Position(line = 1, start = 17, end = 18), value = ","),
                Token(type = "ID", position = Position(line = 1, start = 19, end = 20), value = "b"),
                Token(type = "COLON", position = Position(line = 1, start = 20, end = 21), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 22, end = 28), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 29, end = 30), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 31, end = 33), value = "10"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 33, end = 34), value = ";"),
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
                                            line = 1,
                                            start = 4,
                                            end = 5,
                                        ),
                                    type = TypeReference(type = "number", line = 1, start = 7, end = 13),
                                    init = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 16, end = 17),
                                    line = 1,
                                    start = 4,
                                    end = 17,
                                ),
                                VariableDeclarator(
                                    id = Identifier(name = "b", line = 1, start = 19, end = 20),
                                    type = TypeReference(type = "number", line = 1, start = 22, end = 28),
                                    init = NumberLiteral(value = 10.toBigDecimal(), line = 1, start = 31, end = 33),
                                    line = 1,
                                    start = 19,
                                    end = 33,
                                ),
                            ),
                        kind = "let",
                        line = 1,
                        start = 0,
                        end = 34,
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseDeclareBinaryOperation() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 16, end = 17), value = "5"),
                Token(type = "PLUS", position = Position(line = 1, start = 18, end = 19), value = "+"),
                Token(type = "NUMBER", position = Position(line = 1, start = 20, end = 21), value = "5"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 21, end = 22), value = ";"),
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
                                            line = 1,
                                            start = 4,
                                            end = 5,
                                        ),
                                    type = TypeReference(type = "number", line = 1, start = 7, end = 13),
                                    init =
                                        BinaryExpression(
                                            left = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 16, end = 17),
                                            right = NumberLiteral(value = 5.toBigDecimal(), line = 1, start = 20, end = 21),
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
                    ),
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseErrorForMissingSemicolon() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 16, end = 17), value = "5"),
            )

        assert(
            parser.parse(ASTProviderFactory(tokens, "1.1.0")) is ASTBuilderFailure &&
                (parser.parse(ASTProviderFactory(tokens, "1.1.0")) as ASTBuilderFailure).errorMessage.contains("Missing semicolon"),
        )
    }

    @Test
    fun parseErrorForExtraSemicolon() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 13, end = 14), value = ";"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 14, end = 15), value = ";"),
            )
        try {
            parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        } catch (e: IllegalStateException) {
            assertEquals("Only one line of code is allowed at a time.", e.message)
        }
    }

    @Test
    fun parseErrorForMissingColonInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 16, end = 17), value = "a"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 17, end = 18), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Missing colon at (1:${tokens[1].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorForMultipleColonInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 3, end = 4), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 4, end = 5), value = ":"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 6, end = 12), value = "number"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 12, end = 13), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Missing type at (1:${tokens[2].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorForInvalidStatementDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(line = 1, start = 0, end = 1), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 1, end = 2), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 3, end = 9), value = "number"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 9, end = 10), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure && result.errorMessage.contains("Invalid start of variable declaration"))
    }

    @Test
    fun parsingVariableDeclarationWithMissingAssignedExpression() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 14, end = 15), value = "="),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 15, end = 16), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Missing assigned expression at " +
                "(1:${tokens[4].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorForEmptyTokens() {
        val parser = Parser()
        val tokens = listOf<Token>()
        val result =
            ASTBuilderFailure(
                errorMessage =
                    "Empty tokens",
            )
        assertEquals(result, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parseErrorExtraComasInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 7, end = 13), value = "number"),
                Token(type = "COMMA", position = Position(line = 1, start = 13, end = 14), value = ","),
                Token(type = "COMMA", position = Position(line = 1, start = 14, end = 15), value = ","),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 15, end = 16), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: not enough tokens for a variable declarator at " +
                "(1:${tokens[4].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorFewerComasInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(line = 1, start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(line = 1, start = 3, end = 4), value = "a"),
                Token(type = "COLON", position = Position(line = 1, start = 4, end = 5), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 6, end = 12), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 12, end = 13), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 13, end = 14), value = "5"),
                Token(type = "ID", position = Position(line = 1, start = 14, end = 15), value = "b"),
                Token(type = "COLON", position = Position(line = 1, start = 15, end = 16), value = ":"),
                Token(type = "TYPE", position = Position(line = 1, start = 16, end = 22), value = "number"),
                Token(type = "ASSIGN", position = Position(line = 1, start = 22, end = 23), value = "="),
                Token(type = "NUMBER", position = Position(line = 1, start = 23, end = 32), value = "10"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 32, end = 33), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Invalid assigned expression at " +
                "(1:${tokens[5].position.start})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorMissingCloseParenthesisInPrintFunction() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(line = 1, start = 0, end = 5), value = "print"),
                Token(type = "OPAREN", position = Position(line = 1, start = 5, end = 6), value = "("),
                Token(type = "STRING", position = Position(line = 1, start = 6, end = 19), value = "Hello world"),
                Token(type = "SEMICOLON", position = Position(line = 1, start = 19, end = 20), value = ";"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid expression: Call expression does not have close parenthesis at (1:19)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parsingSimpleConditionalStatementShouldBeAstBuilderSuccess() {
        val parser = Parser()
        /*
        if (a) {
            a = 3;
        }else{
            b = 3;
        }
         */
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(line = 1, start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(line = 1, start = 7, end = 8), value = "{"),
                Token(type = "ID", position = Position(line = 2, start = 21, end = 22), value = "a"),
                Token(type = "ASSIGN", position = Position(line = 2, start = 23, end = 24), value = "="),
                Token(type = "NUMBER", position = Position(line = 2, start = 25, end = 26), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 2, start = 26, end = 27), value = ";"),
                Token(type = "CBRACE", position = Position(line = 3, start = 36, end = 37), value = "}"),
                Token(type = "ELSE", position = Position(line = 3, start = 37, end = 41), value = "else"),
                Token(type = "OBRACE", position = Position(line = 3, start = 41, end = 42), value = "{"),
                Token(type = "ID", position = Position(line = 4, start = 55, end = 56), value = "b"),
                Token(type = "ASSIGN", position = Position(line = 4, start = 57, end = 58), value = "="),
                Token(type = "NUMBER", position = Position(line = 4, start = 59, end = 60), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 4, start = 60, end = 61), value = ";"),
                Token(type = "CBRACE", position = Position(line = 5, start = 70, end = 71), value = "}"),
            )

        val expected =
            ASTBuilderSuccess(
                astNode =
                    ConditionalStatement(
                        test = Identifier(name = "a", line = 1, start = 4, end = 5),
                        consequent =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "a", line = 2, start = 21, end = 22),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 2, start = 25, end = 26),
                                            line = 2,
                                            start = 21,
                                            end = 26,
                                        ),
                                    line = 2,
                                    start = 21,
                                    end = 27,
                                ),
                            ),
                        alternate =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", line = 4, start = 55, end = 56),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 4, start = 59, end = 60),
                                            line = 4,
                                            start = 55,
                                            end = 60,
                                        ),
                                    line = 4,
                                    start = 55,
                                    end = 61,
                                ),
                            ),
                        line = 1,
                        start = 7,
                        end = 71,
                    ),
            )

        assertEquals(expected, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsingSimpleConditionalStatementWithMultipleStatementsShouldBeAstBuilderSuccess() {
        val parser = Parser()
        /*
        if (a) {
            a = 3;
            b = 3;
            c = 3;
        } else {
            b = 3;
            c = 3;
            d = 3;
        }
         */
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(line = 1, start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(line = 1, start = 7, end = 8), value = "{"),
                Token(type = "ID", position = Position(line = 2, start = 21, end = 22), value = "a"),
                Token(type = "ASSIGN", position = Position(line = 2, start = 23, end = 24), value = "="),
                Token(type = "NUMBER", position = Position(line = 2, start = 25, end = 26), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 2, start = 26, end = 27), value = ";"),
                Token(type = "ID", position = Position(line = 3, start = 40, end = 41), value = "b"),
                Token(type = "ASSIGN", position = Position(line = 3, start = 42, end = 43), value = "="),
                Token(type = "NUMBER", position = Position(line = 3, start = 44, end = 45), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 3, start = 45, end = 46), value = ";"),
                Token(type = "ID", position = Position(line = 4, start = 59, end = 60), value = "c"),
                Token(type = "ASSIGN", position = Position(line = 4, start = 61, end = 62), value = "="),
                Token(type = "NUMBER", position = Position(line = 4, start = 63, end = 64), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 4, start = 64, end = 65), value = ";"),
                Token(type = "CBRACE", position = Position(line = 5, start = 74, end = 75), value = "}"),
                Token(type = "ELSE", position = Position(line = 5, start = 76, end = 80), value = "else"),
                Token(type = "OBRACE", position = Position(line = 5, start = 81, end = 82), value = "{"),
                Token(type = "ID", position = Position(line = 6, start = 95, end = 96), value = "b"),
                Token(type = "ASSIGN", position = Position(line = 6, start = 97, end = 98), value = "="),
                Token(type = "NUMBER", position = Position(line = 6, start = 99, end = 100), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 6, start = 100, end = 101), value = ";"),
                Token(type = "ID", position = Position(line = 7, start = 114, end = 115), value = "c"),
                Token(type = "ASSIGN", position = Position(line = 7, start = 116, end = 117), value = "="),
                Token(type = "NUMBER", position = Position(line = 7, start = 118, end = 119), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 7, start = 119, end = 120), value = ";"),
                Token(type = "ID", position = Position(line = 8, start = 133, end = 134), value = "d"),
                Token(type = "ASSIGN", position = Position(line = 8, start = 135, end = 136), value = "="),
                Token(type = "NUMBER", position = Position(line = 8, start = 137, end = 138), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 8, start = 138, end = 139), value = ";"),
                Token(type = "CBRACE", position = Position(line = 9, start = 148, end = 149), value = "}"),
            )

        val expected =
            ASTBuilderSuccess(
                astNode =
                    ConditionalStatement(
                        test = Identifier(name = "a", line = 1, start = 4, end = 5),
                        consequent =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "a", line = 2, start = 21, end = 22),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 2, start = 25, end = 26),
                                            line = 2,
                                            start = 21,
                                            end = 26,
                                        ),
                                    line = 2,
                                    start = 21,
                                    end = 27,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", line = 3, start = 40, end = 41),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 3, start = 44, end = 45),
                                            line = 3,
                                            start = 40,
                                            end = 45,
                                        ),
                                    line = 3,
                                    start = 40,
                                    end = 46,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "c", line = 4, start = 59, end = 60),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 4, start = 63, end = 64),
                                            line = 4,
                                            start = 59,
                                            end = 64,
                                        ),
                                    line = 4,
                                    start = 59,
                                    end = 65,
                                ),
                            ),
                        alternate =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", line = 6, start = 95, end = 96),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 6, start = 99, end = 100),
                                            line = 6,
                                            start = 95,
                                            end = 100,
                                        ),
                                    line = 6,
                                    start = 95,
                                    end = 101,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "c", line = 7, start = 114, end = 115),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 7, start = 118, end = 119),
                                            line = 7,
                                            start = 114,
                                            end = 119,
                                        ),
                                    line = 7,
                                    start = 114,
                                    end = 120,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "d", line = 8, start = 133, end = 134),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 8, start = 137, end = 138),
                                            line = 8,
                                            start = 133,
                                            end = 138,
                                        ),
                                    line = 8,
                                    start = 133,
                                    end = 139,
                                ),
                            ),
                        line = 1,
                        start = 7,
                        end = 149,
                    ),
            )

        assertEquals(expected, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsingCorrectlyNestedConditionalStatementShouldBeAstBuilderSuccess() {
        val parser = Parser()
        /*
        if (a) {
            if (b) {
                a = 3;
            } else {
                b = 3;
            }
        } else {
            b = 3;
        }
         */
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(line = 1, start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(line = 1, start = 7, end = 8), value = "{"),
                Token(type = "IF", position = Position(line = 2, start = 21, end = 23), value = "if"),
                Token(type = "OPAREN", position = Position(line = 2, start = 24, end = 25), value = "("),
                Token(type = "ID", position = Position(line = 2, start = 25, end = 26), value = "b"),
                Token(type = "CPAREN", position = Position(line = 2, start = 26, end = 27), value = ")"),
                Token(type = "OBRACE", position = Position(line = 2, start = 28, end = 29), value = "{"),
                Token(type = "ID", position = Position(line = 3, start = 46, end = 47), value = "a"),
                Token(type = "ASSIGN", position = Position(line = 3, start = 48, end = 49), value = "="),
                Token(type = "NUMBER", position = Position(line = 3, start = 50, end = 51), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 3, start = 51, end = 52), value = ";"),
                Token(type = "CBRACE", position = Position(line = 4, start = 65, end = 66), value = "}"),
                Token(type = "ELSE", position = Position(line = 4, start = 67, end = 71), value = "else"),
                Token(type = "OBRACE", position = Position(line = 4, start = 72, end = 73), value = "{"),
                Token(type = "ID", position = Position(line = 5, start = 90, end = 91), value = "b"),
                Token(type = "ASSIGN", position = Position(line = 5, start = 92, end = 93), value = "="),
                Token(type = "NUMBER", position = Position(line = 5, start = 94, end = 95), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 5, start = 95, end = 96), value = ";"),
                Token(type = "CBRACE", position = Position(line = 6, start = 109, end = 110), value = "}"),
                Token(type = "CBRACE", position = Position(line = 7, start = 119, end = 120), value = "}"),
                Token(type = "ELSE", position = Position(line = 7, start = 121, end = 125), value = "else"),
                Token(type = "OBRACE", position = Position(line = 7, start = 126, end = 127), value = "{"),
                Token(type = "ID", position = Position(line = 8, start = 140, end = 141), value = "b"),
                Token(type = "ASSIGN", position = Position(line = 8, start = 142, end = 143), value = "="),
                Token(type = "NUMBER", position = Position(line = 8, start = 144, end = 145), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 8, start = 145, end = 146), value = ";"),
                Token(type = "CBRACE", position = Position(line = 9, start = 155, end = 156), value = "}"),
            )

        val expected =
            ASTBuilderSuccess(
                astNode =
                    ConditionalStatement(
                        test = Identifier(name = "a", line = 1, start = 4, end = 5),
                        consequent =
                            listOf(
                                ConditionalStatement(
                                    test = Identifier(name = "b", line = 2, start = 25, end = 26),
                                    consequent =
                                        listOf(
                                            ExpressionStatement(
                                                expression =
                                                    AssignmentExpression(
                                                        left = Identifier(name = "a", line = 3, start = 46, end = 47),
                                                        right = NumberLiteral(value = 3.toBigDecimal(), line = 3, start = 50, end = 51),
                                                        line = 3,
                                                        start = 46,
                                                        end = 51,
                                                    ),
                                                line = 3,
                                                start = 46,
                                                end = 52,
                                            ),
                                        ),
                                    alternate =
                                        listOf(
                                            ExpressionStatement(
                                                expression =
                                                    AssignmentExpression(
                                                        left = Identifier(name = "b", line = 5, start = 90, end = 91),
                                                        right = NumberLiteral(value = 3.toBigDecimal(), line = 5, start = 94, end = 95),
                                                        line = 5,
                                                        start = 90,
                                                        end = 95,
                                                    ),
                                                line = 5,
                                                start = 90,
                                                end = 96,
                                            ),
                                        ),
                                    line = 2,
                                    start = 28,
                                    end = 110,
                                ),
                            ),
                        alternate =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", line = 8, start = 140, end = 141),
                                            right = NumberLiteral(value = 3.toBigDecimal(), line = 8, start = 144, end = 145),
                                            line = 8,
                                            start = 140,
                                            end = 145,
                                        ),
                                    line = 8,
                                    start = 140,
                                    end = 146,
                                ),
                            ),
                        line = 1,
                        start = 7,
                        end = 156,
                    ),
            )

        assertEquals(expected, parser.parse(ASTProviderFactory(tokens, "1.1.0")))
    }

    @Test
    fun parsingIncompleteConditionalStatementWithMissingOParenAtStartShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "ID", position = Position(line = 1, start = 3, end = 4), value = "a"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assertEquals(
            "Invalid conditional expression: expected '(' after 'if' at (1:2)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parsingIncompleteConditionalStatementWithMissingIdentifierInArgumentShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 2, end = 3), value = "("),
                Token(type = "CPAREN", position = Position(line = 1, start = 3, end = 4), value = ")"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assertEquals(
            "Invalid conditional expression: expected identifier after '(' at (1:3)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parsingIncompleteConditionalStatementWithMissingCParenAtEndShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 2, end = 3), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 3, end = 4), value = "a"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assertEquals(
            "Invalid conditional expression: expected ')' after identifier at (1:4)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parsingConditionalStatementWithUnsupportedExpressionTestShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 2, end = 3), value = "("),
                Token(type = "NUMBER", position = Position(line = 1, start = 3, end = 4), value = "3"),
                Token(type = "CPAREN", position = Position(line = 1, start = 4, end = 5), value = ")"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assertEquals(
            "Invalid conditional expression: expected identifier after '(' at (1:3)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parsingBadNestedConditionalStatementWithAdditionalElseTokenShouldBeAstBuilderFailure() {
        val parser = Parser()
        /*
        if (a) {
            if (b) {
            } else {
        } else {
        }   b = 3;
        }
         */
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(line = 1, start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(line = 1, start = 7, end = 8), value = "{"),
                Token(type = "IF", position = Position(line = 2, start = 21, end = 23), value = "if"),
                Token(type = "OPAREN", position = Position(line = 2, start = 24, end = 25), value = "("),
                Token(type = "ID", position = Position(line = 2, start = 25, end = 26), value = "b"),
                Token(type = "CPAREN", position = Position(line = 2, start = 26, end = 27), value = ")"),
                Token(type = "OBRACE", position = Position(line = 2, start = 28, end = 29), value = "{"),
                Token(type = "CBRACE", position = Position(line = 3, start = 42, end = 43), value = "}"),
                Token(type = "ELSE", position = Position(line = 3, start = 44, end = 48), value = "else"),
                Token(type = "OBRACE", position = Position(line = 3, start = 49, end = 50), value = "{"),
                Token(type = "CBRACE", position = Position(line = 4, start = 59, end = 60), value = "}"),
                Token(type = "ELSE", position = Position(line = 4, start = 61, end = 65), value = "else"),
                Token(type = "OBRACE", position = Position(line = 4, start = 66, end = 67), value = "{"),
                Token(type = "CBRACE", position = Position(line = 5, start = 76, end = 77), value = "}"),
                Token(type = "ID", position = Position(line = 5, start = 80, end = 81), value = "b"),
                Token(type = "ASSIGN", position = Position(line = 5, start = 82, end = 83), value = "="),
                Token(type = "NUMBER", position = Position(line = 5, start = 84, end = 85), value = "3"),
                Token(type = "SEMICOLON", position = Position(line = 5, start = 85, end = 86), value = ";"),
                Token(type = "CBRACE", position = Position(line = 6, start = 95, end = 96), value = "}"),
            )

        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assertEquals("Unexpected 'else' at (4:61)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingBadNestedConditionalStatementWithMismatchedBracesShouldBeAstBuilderFailure() {
        val parser = Parser()
        /*
        if (a) {
            if (b) {
            } else {
        }
         */

        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(line = 1, start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(line = 1, start = 7, end = 8), value = "{"),
                Token(type = "IF", position = Position(line = 2, start = 21, end = 23), value = "if"),
                Token(type = "OPAREN", position = Position(line = 2, start = 24, end = 25), value = "("),
                Token(type = "ID", position = Position(line = 2, start = 25, end = 26), value = "b"),
                Token(type = "CPAREN", position = Position(line = 2, start = 26, end = 27), value = ")"),
                Token(type = "OBRACE", position = Position(line = 2, start = 28, end = 29), value = "{"),
                Token(type = "CBRACE", position = Position(line = 3, start = 42, end = 43), value = "}"),
                Token(type = "ELSE", position = Position(line = 3, start = 44, end = 48), value = "else"),
                Token(type = "OBRACE", position = Position(line = 3, start = 49, end = 50), value = "{"),
                Token(type = "CBRACE", position = Position(line = 4, start = 59, end = 60), value = "}"),
            )

        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assertEquals("Unmatched braces in expression at (4:60)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingConditionalStatementWithMissingSemicolonInExpressionShouldReturnASTBuilderFailure() {
        val parser = Parser()
        /*
        if(a){
           a = 3
        }
         */
        val tokens =
            listOf(
                Token(type = "IF", position = Position(line = 1, start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(line = 1, start = 2, end = 3), value = "("),
                Token(type = "ID", position = Position(line = 1, start = 3, end = 4), value = "a"),
                Token(type = "CPAREN", position = Position(line = 1, start = 4, end = 5), value = ")"),
                Token(type = "OBRACE", position = Position(line = 1, start = 5, end = 6), value = "{"),
                Token(type = "ID", position = Position(line = 2, start = 18, end = 19), value = "a"),
                Token(type = "ASSIGN", position = Position(line = 2, start = 20, end = 21), value = "="),
                Token(type = "NUMBER", position = Position(line = 2, start = 22, end = 23), value = "3"),
                Token(type = "CBRACE", position = Position(line = 3, start = 32, end = 33), value = "}"),
            )
        val result = parser.parse(ASTProviderFactory(tokens, "1.1.0"))
        assert(result is ASTBuilderFailure)
        assertEquals("Missing semicolon at (2:23)", (result as ASTBuilderFailure).errorMessage)
    }
}

import astbuilder.ASTBuilderFailure
import astbuilder.ASTBuilderSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParserTest {
    @Test
    fun parseDeclareEmptyNumberVariable() {
        val parser = Parser()
        val tokens =

            listOf(
                Token("LET", position = Position(start = 0, end = 3), value = "let"),
                Token("ID", position = Position(start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "SEMICOLON", position = Position(start = 13, end = 14), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseDeclareEmptyStringVariable() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("LET", position = Position(start = 0, end = 3), value = "let"),
                Token("ID", position = Position(start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "string"),
                Token(type = "SEMICOLON", position = Position(start = 13, end = 14), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseDeclareStringVariable() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("LET", position = Position(start = 0, end = 3), value = "let"),
                Token("ID", position = Position(start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "string"),
                Token(type = "ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token(type = "STRING", position = Position(start = 16, end = 17), value = "A"),
                Token(type = "SEMICOLON", position = Position(start = 17, end = 18), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parsingGoodAssignmentExpressionShouldBeASTBuilderSuccess() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(start = 4, end = 5), value = "5"),
                Token("SEMICOLON", position = Position(start = 5, end = 6), value = ";"),
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
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    NumberLiteral(
                                        value = 5.toBigDecimal(),
                                        start = 4,
                                        end = 5,
                                    ),
                                start = 0,
                                end = 5,
                            ),
                        start = 0,
                        end = 6,
                    ),
            )
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parsingAssignmentExpressionWithMissingIdentifierShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ASSIGN", position = Position(start = 0, end = 1), value = "="),
                Token("NUMBER", position = Position(start = 2, end = 3), value = "5"),
                Token("SEMICOLON", position = Position(start = 3, end = 4), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals("Invalid expression: missing identifier at (0, 0)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingAssignmentExpressionWithMissingAssignmentExpressionShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token("SEMICOLON", position = Position(start = 3, end = 4), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals("Invalid expression: missing expression after assignment at (0, 3)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parseMultiplicationOperation() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(start = 4, end = 5), value = "5"),
                Token("MUL", position = Position(start = 6, end = 7), value = "*"),
                Token("NUMBER", position = Position(start = 8, end = 9), value = "5"),
                Token("SEMICOLON", position = Position(start = 9, end = 10), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseDivisionOperation() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(start = 4, end = 5), value = "5"),
                Token("DIV", position = Position(start = 6, end = 7), value = "/"),
                Token("NUMBER", position = Position(start = 8, end = 9), value = "5"),
                Token("SEMICOLON", position = Position(start = 9, end = 10), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parsePlusOperationInExpressionStatement() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(start = 0, end = 1), value = "a"),
                Token(type = "ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token(type = "NUMBER", position = Position(start = 4, end = 5), value = "5"),
                Token(type = "PLUS", position = Position(start = 6, end = 7), value = "+"),
                Token(type = "NUMBER", position = Position(start = 8, end = 9), value = "5"),
                Token(type = "SEMICOLON", position = Position(start = 9, end = 10), value = ";"),
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

        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseMinusOperationInExpressionStatement() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(start = 4, end = 5), value = "5"),
                Token("MINUS", position = Position(start = 6, end = 7), value = "-"),
                Token("NUMBER", position = Position(start = 8, end = 9), value = "5"),
                Token("SEMICOLON", position = Position(start = 9, end = 10), value = ";"),
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
                                        start = 0,
                                        end = 1,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 5.toBigDecimal(), start = 4, end = 5),
                                        right = NumberLiteral(value = 5.toBigDecimal(), start = 8, end = 9),
                                        operator = "-",
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseBinaryExpressionWithAllOperatorsInStatementExpression() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 1), value = "a"),
                Token("ASSIGN", position = Position(start = 2, end = 3), value = "="),
                Token("NUMBER", position = Position(start = 4, end = 5), value = "1"),
                Token("PLUS", position = Position(start = 6, end = 7), value = "+"),
                Token("NUMBER", position = Position(start = 8, end = 9), value = "2"),
                Token("MUL", position = Position(start = 10, end = 11), value = "*"),
                Token("NUMBER", position = Position(start = 12, end = 13), value = "3"),
                Token("DIV", position = Position(start = 14, end = 15), value = "/"),
                Token("NUMBER", position = Position(start = 16, end = 17), value = "4"),
                Token("MINUS", position = Position(start = 17, end = 18), value = "-"),
                Token("NUMBER", position = Position(start = 19, end = 20), value = "5"),
                Token("MODULE", position = Position(start = 21, end = 22), value = "%"),
                Token("NUMBER", position = Position(start = 23, end = 24), value = "6"),
                Token("SEMICOLON", position = Position(start = 24, end = 25), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            AssignmentExpression(
                                left = Identifier(name = "a", start = 0, end = 1),
                                right =
                                    BinaryExpression(
                                        left =
                                            BinaryExpression(
                                                left = NumberLiteral(value = 1.toBigDecimal(), start = 4, end = 5),
                                                right =
                                                    BinaryExpression(
                                                        left =
                                                            BinaryExpression(
                                                                left = NumberLiteral(value = 2.toBigDecimal(), start = 8, end = 9),
                                                                right = NumberLiteral(value = 3.toBigDecimal(), start = 12, end = 13),
                                                                operator = "*",
                                                                start = 8,
                                                                end = 13,
                                                            ),
                                                        right = NumberLiteral(value = 4.toBigDecimal(), start = 16, end = 17),
                                                        operator = "/",
                                                        start = 8,
                                                        end = 17,
                                                    ),
                                                operator = "+",
                                                start = 4,
                                                end = 17,
                                            ),
                                        right =
                                            BinaryExpression(
                                                left = NumberLiteral(value = 5.toBigDecimal(), start = 19, end = 20),
                                                right = NumberLiteral(value = 6.toBigDecimal(), start = 23, end = 24),
                                                operator = "%",
                                                start = 19,
                                                end = 24,
                                            ),
                                        operator = "-",
                                        start = 4,
                                        end = 24,
                                    ),
                                start = 0,
                                end = 24,
                            ),
                        start = 0,
                        end = 25,
                    ),
            )

        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseBinaryExpressionsWithFunctionIdentifierAndNumbersInStatementExpression() {
        val parser = Parser()
        // function(1 + 2) * a / (4 + 5)
        val tokens =
            listOf(
                Token("ID", position = Position(start = 0, end = 8), value = "function"),
                Token("OPAREN", position = Position(start = 8, end = 9), value = "("),
                Token("NUMBER", position = Position(start = 9, end = 10), value = "1"),
                Token("PLUS", position = Position(start = 11, end = 12), value = "+"),
                Token("NUMBER", position = Position(start = 13, end = 14), value = "2"),
                Token("CPAREN", position = Position(start = 14, end = 15), value = ")"),
                Token("MUL", position = Position(start = 16, end = 17), value = "*"),
                Token("ID", position = Position(start = 18, end = 19), value = "a"),
                Token("DIV", position = Position(start = 20, end = 21), value = "/"),
                Token("OPAREN", position = Position(start = 22, end = 23), value = "("),
                Token("NUMBER", position = Position(start = 23, end = 24), value = "4"),
                Token("PLUS", position = Position(start = 25, end = 26), value = "-"),
                Token("NUMBER", position = Position(start = 27, end = 28), value = "5"),
                Token("CPAREN", position = Position(start = 28, end = 29), value = ")"),
                Token("SEMICOLON", position = Position(start = 29, end = 30), value = ";"),
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
                                                callee = Identifier(name = "function", start = 0, end = 8),
                                                arguments =
                                                    listOf(
                                                        BinaryExpression(
                                                            left = NumberLiteral(value = 1.toBigDecimal(), start = 9, end = 10),
                                                            right = NumberLiteral(value = 2.toBigDecimal(), start = 13, end = 14),
                                                            operator = "+",
                                                            start = 9,
                                                            end = 14,
                                                        ),
                                                    ),
                                                start = 0,
                                                end = 15,
                                            ),
                                        right = Identifier(name = "a", start = 18, end = 19),
                                        operator = "*",
                                        start = 0,
                                        end = 19,
                                    ),
                                right =
                                    BinaryExpression(
                                        left = NumberLiteral(value = 4.toBigDecimal(), start = 23, end = 24),
                                        right = NumberLiteral(value = 5.toBigDecimal(), start = 27, end = 28),
                                        operator = "-",
                                        start = 23,
                                        end = 28,
                                    ),
                                operator = "/",
                                start = 0,
                                end = 28,
                            ),
                        start = 0,
                        end = 30,
                    ),
            )
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseBinaryExpressionWithStringAndNumberInStatementExpression() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("STRING", position = Position(start = 0, end = 13), value = "Hello world"),
                Token("PLUS", position = Position(start = 14, end = 15), value = "+"),
                Token("NUMBER", position = Position(start = 16, end = 17), value = "5"),
                Token("SEMICOLON", position = Position(start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            BinaryExpression(
                                left = StringLiteral(value = "Hello world", start = 0, end = 13),
                                right = NumberLiteral(value = 5.toBigDecimal(), start = 16, end = 17),
                                operator = "+",
                                start = 0,
                                end = 17,
                            ),
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseBinaryExpressionWithMismatchedParenthesisInStatementExpressionReturnsFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("OPAREN", position = Position(start = 0, end = 1), value = "("),
                Token("NUMBER", position = Position(start = 1, end = 2), value = "1"),
                Token("PLUS", position = Position(start = 3, end = 4), value = "+"),
                Token("NUMBER", position = Position(start = 5, end = 6), value = "2"),
                Token("CPAREN", position = Position(start = 6, end = 7), value = ")"),
                Token("CPAREN", position = Position(start = 7, end = 8), value = ")"),
                Token("SEMICOLON", position = Position(start = 8, end = 9), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals("Invalid expression: Mismatched parenthesis at (0, 7)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parseDeclareNumberVariable() {
        val parser = Parser()
        val tokens =
            listOf(
                Token("LET", position = Position(start = 0, end = 3), value = "let"),
                Token("ID", position = Position(start = 4, end = 5), value = "a"),
                Token("COLON", position = Position(start = 5, end = 6), value = ":"),
                Token("TYPE", position = Position(start = 7, end = 13), value = "string"),
                Token("ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token("NUMBER", position = Position(start = 16, end = 17), value = "5"),
                Token("SEMICOLON", position = Position(start = 17, end = 18), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parsePrintFunctionWithString() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(start = 0, end = 5), value = "print"),
                Token(type = "OPAREN", position = Position(start = 5, end = 6), value = "("),
                Token(type = "STRING", position = Position(start = 6, end = 19), value = "Hello world"),
                Token(type = "CPAREN", position = Position(start = 19, end = 20), value = ")"),
                Token(type = "SEMICOLON", position = Position(start = 20, end = 21), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseCallExpressionWithMultipleArguments() {
        val parser = Parser()
        // function(1,2,3,4);
        val tokens =
            listOf(
                Token(type = "ID", position = Position(start = 0, end = 8), value = "function"),
                Token(type = "OPAREN", position = Position(start = 8, end = 9), value = "("),
                Token(type = "NUMBER", position = Position(start = 9, end = 10), value = "1"),
                Token(type = "COMMA", position = Position(start = 10, end = 11), value = ","),
                Token(type = "NUMBER", position = Position(start = 11, end = 12), value = "2"),
                Token(type = "COMMA", position = Position(start = 12, end = 13), value = ","),
                Token(type = "NUMBER", position = Position(start = 13, end = 14), value = "3"),
                Token(type = "COMMA", position = Position(start = 14, end = 15), value = ","),
                Token(type = "NUMBER", position = Position(start = 15, end = 16), value = "4"),
                Token(type = "CPAREN", position = Position(start = 16, end = 17), value = ")"),
                Token(type = "SEMICOLON", position = Position(start = 17, end = 18), value = ";"),
            )
        val result =
            ASTBuilderSuccess(
                astNode =
                    ExpressionStatement(
                        expression =
                            CallExpression(
                                callee = Identifier(name = "function", start = 0, end = 8),
                                arguments =
                                    listOf(
                                        NumberLiteral(value = 1.toBigDecimal(), start = 9, end = 10),
                                        NumberLiteral(value = 2.toBigDecimal(), start = 11, end = 12),
                                        NumberLiteral(value = 3.toBigDecimal(), start = 13, end = 14),
                                        NumberLiteral(value = 4.toBigDecimal(), start = 15, end = 16),
                                    ),
                                start = 0,
                                end = 17,
                            ),
                        start = 0,
                        end = 18,
                    ),
            )
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parsePrintFunctionWithNumber() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(start = 0, end = 5), value = "print"),
                Token(type = "OPAREN", position = Position(start = 5, end = 6), value = "("),
                Token(type = "NUMBER", position = Position(start = 6, end = 7), value = "1"),
                Token(type = "CPAREN", position = Position(start = 7, end = 8), value = ")"),
                Token(type = "SEMICOLON", position = Position(start = 8, end = 9), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseMultipleVariableDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(start = 16, end = 17), value = "5"),
                Token(type = "COMMA", position = Position(start = 17, end = 18), value = ","),
                Token(type = "ID", position = Position(start = 19, end = 20), value = "b"),
                Token(type = "COLON", position = Position(start = 20, end = 21), value = ":"),
                Token(type = "TYPE", position = Position(start = 22, end = 28), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 29, end = 30), value = "="),
                Token(type = "NUMBER", position = Position(start = 31, end = 33), value = "10"),
                Token(type = "SEMICOLON", position = Position(start = 33, end = 34), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseDeclareBinaryOperation() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(start = 16, end = 17), value = "5"),
                Token(type = "PLUS", position = Position(start = 18, end = 19), value = "+"),
                Token(type = "NUMBER", position = Position(start = 20, end = 21), value = "5"),
                Token(type = "SEMICOLON", position = Position(start = 21, end = 22), value = ";"),
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseErrorForMissingSemicolon() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(start = 16, end = 17), value = "5"),
            )

        assert(
            parser.parse(tokens, 0) is ASTBuilderFailure &&
                (parser.parse(tokens, 0) as ASTBuilderFailure).errorMessage.contains("Missing semicolon"),
        )
    }

    @Test
    fun parseErrorForExtraSemicolon() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "SEMICOLON", position = Position(start = 13, end = 14), value = ";"),
                Token(type = "SEMICOLON", position = Position(start = 14, end = 15), value = ";"),
            )
        try {
            parser.parse(tokens, 0)
        } catch (e: IllegalStateException) {
            assertEquals("Only one line of code is allowed at a time.", e.message)
        }
    }

    @Test
    fun parseErrorForMissingColonInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token(type = "NUMBER", position = Position(start = 16, end = 17), value = "a"),
                Token(type = "SEMICOLON", position = Position(start = 17, end = 18), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Missing colon at (0, ${tokens[1].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorForMultipleColonInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 3, end = 4), value = "a"),
                Token(type = "COLON", position = Position(start = 4, end = 5), value = ":"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 6, end = 12), value = "number"),
                Token(type = "SEMICOLON", position = Position(start = 12, end = 13), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Missing type at (0, ${tokens[2].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorForInvalidStatementDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(start = 0, end = 1), value = "a"),
                Token(type = "COLON", position = Position(start = 1, end = 2), value = ":"),
                Token(type = "TYPE", position = Position(start = 3, end = 9), value = "number"),
                Token(type = "SEMICOLON", position = Position(start = 9, end = 10), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure && result.errorMessage.contains("Invalid start of variable declaration"))
    }

    @Test
    fun parsingVariableDeclarationWithMissingAssignedExpression() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 14, end = 15), value = "="),
                Token(type = "SEMICOLON", position = Position(start = 15, end = 16), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Missing assigned expression at " +
                "(0, ${tokens[4].position.end})",
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
        assertEquals(result, parser.parse(tokens, 0))
    }

    @Test
    fun parseErrorExtraComasInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "COLON", position = Position(start = 5, end = 6), value = ":"),
                Token(type = "TYPE", position = Position(start = 7, end = 13), value = "number"),
                Token(type = "COMMA", position = Position(start = 13, end = 14), value = ","),
                Token(type = "COMMA", position = Position(start = 14, end = 15), value = ","),
                Token(type = "SEMICOLON", position = Position(start = 15, end = 16), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: not enough tokens for a variable declarator at " +
                "(0, ${tokens[4].position.end})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorFewerComasInDeclaration() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "LET", position = Position(start = 0, end = 3), value = "let"),
                Token(type = "ID", position = Position(start = 3, end = 4), value = "a"),
                Token(type = "COLON", position = Position(start = 4, end = 5), value = ":"),
                Token(type = "TYPE", position = Position(start = 6, end = 12), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 12, end = 13), value = "="),
                Token(type = "NUMBER", position = Position(start = 13, end = 14), value = "5"),
                Token(type = "ID", position = Position(start = 14, end = 15), value = "b"),
                Token(type = "COLON", position = Position(start = 15, end = 16), value = ":"),
                Token(type = "TYPE", position = Position(start = 16, end = 22), value = "number"),
                Token(type = "ASSIGN", position = Position(start = 22, end = 23), value = "="),
                Token(type = "NUMBER", position = Position(start = 23, end = 32), value = "10"),
                Token(type = "SEMICOLON", position = Position(start = 32, end = 33), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid variable declaration: Invalid declarator: Invalid assigned expression at " +
                "(0, ${tokens[5].position.start})",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parseErrorMissingCloseParenthesisInPrintFunction() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "ID", position = Position(start = 0, end = 5), value = "print"),
                Token(type = "OPAREN", position = Position(start = 5, end = 6), value = "("),
                Token(type = "STRING", position = Position(start = 6, end = 19), value = "Hello world"),
                Token(type = "SEMICOLON", position = Position(start = 19, end = 20), value = ";"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals(
            "Invalid expression: Call expression does not have close parenthesis at (0, 19)",
            (result as ASTBuilderFailure).errorMessage,
        )
    }

    @Test
    fun parsingSimpleConditionalStatementShouldBeAstBuilderSuccess() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 2, end = 3), value = "("),
                Token(type = "ID", position = Position(start = 3, end = 4), value = "a"),
                Token(type = "CPAREN", position = Position(start = 4, end = 5), value = ")"),
                Token(type = "OBRACE", position = Position(start = 5, end = 6), value = "{"),
                Token(type = "ID", position = Position(start = 11, end = 12), value = "a"),
                Token(type = "ASSIGN", position = Position(start = 13, end = 14), value = "="),
                Token(type = "NUMBER", position = Position(start = 15, end = 16), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 16, end = 17), value = ";"),
                Token(type = "CBRACE", position = Position(start = 18, end = 19), value = "}"),
                Token(type = "ELSE", position = Position(start = 20, end = 24), value = "else"),
                Token(type = "OBRACE", position = Position(start = 25, end = 26), value = "{"),
                Token(type = "ID", position = Position(start = 31, end = 32), value = "b"),
                Token(type = "ASSIGN", position = Position(start = 33, end = 34), value = "="),
                Token(type = "NUMBER", position = Position(start = 35, end = 36), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 36, end = 37), value = ";"),
                Token(type = "CBRACE", position = Position(start = 38, end = 39), value = "}"),
            )
        val expected =
            ASTBuilderSuccess(
                astNode =
                    ConditionalStatement(
                        test = Identifier(name = "a", start = 3, end = 4),
                        consequent =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "a", start = 11, end = 12),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 15, end = 16),
                                            start = 11,
                                            end = 16,
                                        ),
                                    start = 11,
                                    end = 17,
                                ),
                            ),
                        alternate =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", start = 31, end = 32),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 35, end = 36),
                                            start = 31,
                                            end = 36,
                                        ),
                                    start = 31,
                                    end = 37,
                                ),
                            ),
                        start = 5,
                        end = 39,
                    ),
            )

        assertEquals(expected, parser.parse(tokens, 0))
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
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(start = 7, end = 8), value = "{"),
                Token(type = "ID", position = Position(start = 13, end = 14), value = "a"),
                Token(type = "ASSIGN", position = Position(start = 15, end = 16), value = "="),
                Token(type = "NUMBER", position = Position(start = 17, end = 18), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 18, end = 19), value = ";"),
                Token(type = "ID", position = Position(start = 24, end = 25), value = "b"),
                Token(type = "ASSIGN", position = Position(start = 26, end = 27), value = "="),
                Token(type = "NUMBER", position = Position(start = 28, end = 29), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 29, end = 30), value = ";"),
                Token(type = "ID", position = Position(start = 35, end = 36), value = "c"),
                Token(type = "ASSIGN", position = Position(start = 37, end = 38), value = "="),
                Token(type = "NUMBER", position = Position(start = 39, end = 40), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 40, end = 41), value = ";"),
                Token(type = "CBRACE", position = Position(start = 42, end = 43), value = "}"),
                Token(type = "ELSE", position = Position(start = 44, end = 48), value = "else"),
                Token(type = "OBRACE", position = Position(start = 49, end = 50), value = "{"),
                Token(type = "ID", position = Position(start = 55, end = 56), value = "b"),
                Token(type = "ASSIGN", position = Position(start = 57, end = 58), value = "="),
                Token(type = "NUMBER", position = Position(start = 59, end = 60), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 60, end = 61), value = ";"),
                Token(type = "ID", position = Position(start = 66, end = 67), value = "c"),
                Token(type = "ASSIGN", position = Position(start = 68, end = 69), value = "="),
                Token(type = "NUMBER", position = Position(start = 70, end = 71), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 71, end = 72), value = ";"),
                Token(type = "ID", position = Position(start = 77, end = 78), value = "d"),
                Token(type = "ASSIGN", position = Position(start = 79, end = 80), value = "="),
                Token(type = "NUMBER", position = Position(start = 81, end = 82), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 82, end = 83), value = ";"),
                Token(type = "CBRACE", position = Position(start = 84, end = 85), value = "}"),
            )
        val expected =
            ASTBuilderSuccess(
                astNode =
                    ConditionalStatement(
                        test = Identifier(name = "a", start = 4, end = 5),
                        consequent =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "a", start = 13, end = 14),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 17, end = 18),
                                            start = 13,
                                            end = 18,
                                        ),
                                    start = 13,
                                    end = 19,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", start = 24, end = 25),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 28, end = 29),
                                            start = 24,
                                            end = 29,
                                        ),
                                    start = 24,
                                    end = 30,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "c", start = 35, end = 36),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 39, end = 40),
                                            start = 35,
                                            end = 40,
                                        ),
                                    start = 35,
                                    end = 41,
                                ),
                            ),
                        alternate =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", start = 55, end = 56),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 59, end = 60),
                                            start = 55,
                                            end = 60,
                                        ),
                                    start = 55,
                                    end = 61,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "c", start = 66, end = 67),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 70, end = 71),
                                            start = 66,
                                            end = 71,
                                        ),
                                    start = 66,
                                    end = 72,
                                ),
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "d", start = 77, end = 78),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 81, end = 82),
                                            start = 77,
                                            end = 82,
                                        ),
                                    start = 77,
                                    end = 83,
                                ),
                            ),
                        start = 7,
                        end = 85,
                    ),
            )

        assertEquals(expected, parser.parse(tokens, 0))
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
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(start = 7, end = 8), value = "{"),
                Token(type = "IF", position = Position(start = 13, end = 15), value = "if"),
                Token(type = "OPAREN", position = Position(start = 16, end = 17), value = "("),
                Token(type = "ID", position = Position(start = 17, end = 18), value = "b"),
                Token(type = "CPAREN", position = Position(start = 18, end = 19), value = ")"),
                Token(type = "OBRACE", position = Position(start = 20, end = 21), value = "{"),
                Token(type = "ID", position = Position(start = 30, end = 31), value = "a"),
                Token(type = "ASSIGN", position = Position(start = 32, end = 33), value = "="),
                Token(type = "NUMBER", position = Position(start = 34, end = 35), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 35, end = 36), value = ";"),
                Token(type = "CBRACE", position = Position(start = 41, end = 42), value = "}"),
                Token(type = "ELSE", position = Position(start = 43, end = 47), value = "else"),
                Token(type = "OBRACE", position = Position(start = 48, end = 49), value = "{"),
                Token(type = "ID", position = Position(start = 58, end = 59), value = "b"),
                Token(type = "ASSIGN", position = Position(start = 60, end = 61), value = "="),
                Token(type = "NUMBER", position = Position(start = 62, end = 63), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 63, end = 64), value = ";"),
                Token(type = "CBRACE", position = Position(start = 69, end = 70), value = "}"),
                Token(type = "CBRACE", position = Position(start = 71, end = 72), value = "}"),
                Token(type = "ELSE", position = Position(start = 73, end = 77), value = "else"),
                Token(type = "OBRACE", position = Position(start = 78, end = 79), value = "{"),
                Token(type = "ID", position = Position(start = 84, end = 85), value = "b"),
                Token(type = "ASSIGN", position = Position(start = 86, end = 87), value = "="),
                Token(type = "NUMBER", position = Position(start = 88, end = 89), value = "3"),
                Token(type = "SEMICOLON", position = Position(start = 89, end = 90), value = ";"),
                Token(type = "CBRACE", position = Position(start = 91, end = 92), value = "}"),
            )

        val expected =
            ASTBuilderSuccess(
                astNode =
                    ConditionalStatement(
                        test = Identifier(name = "a", start = 4, end = 5),
                        consequent =
                            listOf(
                                ConditionalStatement(
                                    test = Identifier(name = "b", start = 17, end = 18),
                                    consequent =
                                        listOf(
                                            ExpressionStatement(
                                                expression =
                                                    AssignmentExpression(
                                                        left = Identifier(name = "a", start = 30, end = 31),
                                                        right = NumberLiteral(value = 3.toBigDecimal(), start = 34, end = 35),
                                                        start = 30,
                                                        end = 35,
                                                    ),
                                                start = 30,
                                                end = 36,
                                            ),
                                        ),
                                    alternate =
                                        listOf(
                                            ExpressionStatement(
                                                expression =
                                                    AssignmentExpression(
                                                        left = Identifier(name = "b", start = 58, end = 59),
                                                        right = NumberLiteral(value = 3.toBigDecimal(), start = 62, end = 63),
                                                        start = 58,
                                                        end = 63,
                                                    ),
                                                start = 58,
                                                end = 64,
                                            ),
                                        ),
                                    start = 20,
                                    end = 70,
                                ),
                            ),
                        alternate =
                            listOf(
                                ExpressionStatement(
                                    expression =
                                        AssignmentExpression(
                                            left = Identifier(name = "b", start = 84, end = 85),
                                            right = NumberLiteral(value = 3.toBigDecimal(), start = 88, end = 89),
                                            start = 84,
                                            end = 89,
                                        ),
                                    start = 84,
                                    end = 90,
                                ),
                            ),
                        start = 7,
                        end = 92,
                    ),
            )

        assertEquals(expected, parser.parse(tokens, 0))
    }

    @Test
    fun parsingIncompleteConditionalStatementWithMissingOParenAtStartShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "ID", position = Position(start = 3, end = 4), value = "a"),
            )
        val result = parser.parse(tokens, 0)
        assertEquals("Invalid conditional expression: expected '(' after 'if' at (0, 2)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingIncompleteConditionalStatementWithMissingIdentifierInArgumentShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 2, end = 3), value = "("),
                Token(type = "CPAREN", position = Position(start = 3, end = 4), value = ")"),
            )
        val result = parser.parse(tokens, 0)
        assertEquals("Invalid conditional expression: expected identifier after '(' at (0, 3)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingIncompleteConditionalStatementWithMissingCParenAtEndShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 2, end = 3), value = "("),
                Token(type = "ID", position = Position(start = 3, end = 4), value = "a"),
            )
        val result = parser.parse(tokens, 0)
        assertEquals("Invalid conditional expression: expected ')' after identifier at (0, 4)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingConditionalStatementWithUnsupportedExpressionTestShouldBeASTBuilderFailure() {
        val parser = Parser()
        val tokens =
            listOf(
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 2, end = 3), value = "("),
                Token(type = "NUMBER", position = Position(start = 3, end = 4), value = "3"),
                Token(type = "CPAREN", position = Position(start = 4, end = 5), value = ")"),
            )
        val result = parser.parse(tokens, 0)
        assertEquals("Invalid conditional expression: expected identifier after '(' at (0, 3)", (result as ASTBuilderFailure).errorMessage)
    }

    @Test
    fun parsingBadNestedConditionalStatementWithAdditionalElseTokenShouldBeAstBuilderFailure() {
        val parser = Parser()
        /*
        if (a) {
            if (b) {
            } else {
        } else {
        }
         */
        val tokens =
            listOf(
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(start = 7, end = 8), value = "{"),
                Token(type = "IF", position = Position(start = 13, end = 15), value = "if"),
                Token(type = "OPAREN", position = Position(start = 16, end = 17), value = "("),
                Token(type = "ID", position = Position(start = 17, end = 18), value = "b"),
                Token(type = "CPAREN", position = Position(start = 18, end = 19), value = ")"),
                Token(type = "OBRACE", position = Position(start = 20, end = 21), value = "{"),
                Token(type = "CBRACE", position = Position(start = 26, end = 27), value = "}"),
                Token(type = "ELSE", position = Position(start = 28, end = 32), value = "else"),
                Token(type = "OBRACE", position = Position(start = 33, end = 34), value = "{"),
                Token(type = "CBRACE", position = Position(start = 35, end = 36), value = "}"),
                Token(type = "ELSE", position = Position(start = 37, end = 41), value = "else"),
                Token(type = "OBRACE", position = Position(start = 42, end = 43), value = "{"),
                Token(type = "CBRACE", position = Position(start = 44, end = 45), value = "}"),
            )

        val result = parser.parse(tokens, 0)
        assertEquals("Unexpected 'else' at (0, 37)", (result as ASTBuilderFailure).errorMessage)
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
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 3, end = 4), value = "("),
                Token(type = "ID", position = Position(start = 4, end = 5), value = "a"),
                Token(type = "CPAREN", position = Position(start = 5, end = 6), value = ")"),
                Token(type = "OBRACE", position = Position(start = 7, end = 8), value = "{"),
                Token(type = "IF", position = Position(start = 13, end = 15), value = "if"),
                Token(type = "OPAREN", position = Position(start = 16, end = 17), value = "("),
                Token(type = "ID", position = Position(start = 17, end = 18), value = "b"),
                Token(type = "CPAREN", position = Position(start = 18, end = 19), value = ")"),
                Token(type = "OBRACE", position = Position(start = 20, end = 21), value = "{"),
                Token(type = "CBRACE", position = Position(start = 26, end = 27), value = "}"),
                Token(type = "ELSE", position = Position(start = 28, end = 32), value = "else"),
                Token(type = "OBRACE", position = Position(start = 33, end = 34), value = "{"),
                Token(type = "CBRACE", position = Position(start = 35, end = 36), value = "}"),
            )

        val result = parser.parse(tokens, 0)
        assertEquals("Unmatched braces in expression at (0, 36)", (result as ASTBuilderFailure).errorMessage)
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
                Token(type = "IF", position = Position(start = 0, end = 2), value = "if"),
                Token(type = "OPAREN", position = Position(start = 2, end = 3), value = "("),
                Token(type = "ID", position = Position(start = 3, end = 4), value = "a"),
                Token(type = "CPAREN", position = Position(start = 4, end = 5), value = ")"),
                Token(type = "OBRACE", position = Position(start = 5, end = 6), value = "{"),
                Token(type = "ID", position = Position(start = 10, end = 11), value = "a"),
                Token(type = "ASSIGN", position = Position(start = 12, end = 13), value = "="),
                Token(type = "NUMBER", position = Position(start = 14, end = 15), value = "3"),
                Token(type = "CBRACE", position = Position(start = 16, end = 17), value = "}"),
            )
        val result = parser.parse(tokens, 0)
        assert(result is ASTBuilderFailure)
        assertEquals("Missing semicolon at (0, 15)", (result as ASTBuilderFailure).errorMessage)
    }
}

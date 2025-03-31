import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class CompilationEngine {
    JackTokenizer tokenizer;
    Writer writer;

    public CompilationEngine(File inputFile, File outputFile) throws IOException {
        tokenizer = new JackTokenizer(inputFile);
        writer = new FileWriter(outputFile);
        compileClass();
        writer.close();
    }

    public void compileClass() throws IOException {
        writer.write("<class>\n");
        writeKeyWord(JackTokenizer.KeyWord.CLASS);

        tokenizer.advance();
        expectKeyword(JackTokenizer.KeyWord.CLASS);

        tokenizer.advance();
        expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
        String identifier = tokenizer.identifier();
        writeIdentifier(identifier);

        tokenizer.advance();
        expectSymbol('{');
        writeSymbol('{');

        tokenizer.advance();
        compileClassVarDec();
        compileSubroutine();


        expectSymbol('}');
        writeSymbol('}');

        writer.write("</class>\n");
    }

    public void compileClassVarDec() throws IOException {
        while (matchKeyWord(JackTokenizer.KeyWord.STATIC) | matchKeyWord(JackTokenizer.KeyWord.FIELD)){
            writer.write("<classVarDec>\n");
            writeKeyWord(tokenizer.keyWord());

            tokenizer.advance();
            if (!matchKeyWord(JackTokenizer.KeyWord.INT) && !matchKeyWord(JackTokenizer.KeyWord.CHAR) && !matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) && !matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                throw new RuntimeException("Unexpected token: " + tokenizer.keyWord());
            }
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                writeIdentifier(tokenizer.identifier());
            } else {
                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
            writeIdentifier(identifier);

            tokenizer.advance();
            while(matchSymbol(',')) {
                writeSymbol(',');

                tokenizer.advance();
                expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
                String varName = tokenizer.identifier();
                writeIdentifier(varName);

                tokenizer.advance();
            }

            expectSymbol(';');
            writeSymbol(';');

            tokenizer.advance();
            writer.write("</classVarDec>\n");
        }
    }

    public void compileSubroutine() throws IOException {

        while(matchKeyWord(JackTokenizer.KeyWord.METHOD)||matchKeyWord(JackTokenizer.KeyWord.FUNCTION)||matchKeyWord(JackTokenizer.KeyWord.CONSTRUCTOR)){
            writer.write("<subroutineDec>\n");
            JackTokenizer.KeyWord functionType = tokenizer.keyWord();
            writeKeyWord(functionType);

            tokenizer.advance();
            if (!matchKeyWord(JackTokenizer.KeyWord.INT) && !matchKeyWord(JackTokenizer.KeyWord.CHAR) && !matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) && !matchKeyWord(JackTokenizer.KeyWord.VOID) && !matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                throw new RuntimeException("Unexpected token: " + tokenizer.keyWord());
            }
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                writeIdentifier(tokenizer.identifier());
            } else {
                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
            if (functionType == JackTokenizer.KeyWord.CONSTRUCTOR && !identifier.equals("new")) {
                throw new RuntimeException("Unexpected token: " + tokenizer.identifier());
            }
            writeIdentifier(identifier);

            tokenizer.advance();
            expectSymbol('(');
            writeSymbol('(');

            tokenizer.advance();
            compileParameterList();

//            tokenizer.advance();
            expectSymbol(')');
            writeSymbol(')');

            writer.write("<subroutineBody>\n");

            tokenizer.advance();
            expectSymbol('{');
            writeSymbol('{');

            tokenizer.advance();
            compileVarDec();
            compileStatements();

            expectSymbol('}');
            writeSymbol('}');

            writer.write("</subroutineBody>\n");

            tokenizer.advance();

            writer.write("</subroutineDec>\n");
        }
    }

    public void compileParameterList() throws IOException {
        writer.write("<parameterList>\n");
        while (matchKeyWord(JackTokenizer.KeyWord.INT)||matchKeyWord(JackTokenizer.KeyWord.CHAR)||matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) || matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {

            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                writeIdentifier(tokenizer.identifier());
            } else {
                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
            writeIdentifier(identifier);

            tokenizer.advance();
            if (matchSymbol(',')){
                writeSymbol(',');
            } else {
                continue;
            }


            tokenizer.advance();
        }
        writer.write("</parameterList>\n");
    }

    public void compileVarDec() throws IOException {
        while (matchKeyWord(JackTokenizer.KeyWord.VAR)){
            writer.write("<varDec>\n");

            writeKeyWord(JackTokenizer.KeyWord.VAR);

            tokenizer.advance();
            if(!matchKeyWord(JackTokenizer.KeyWord.INT) && !matchKeyWord(JackTokenizer.KeyWord.CHAR) && !matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) && !matchTokenType(JackTokenizer.TokenType.IDENTIFIER)){
                throw new RuntimeException("Unexpected token type: " + tokenizer.tokenType());
            }
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                writeIdentifier(tokenizer.identifier());
            } else {
                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
            writeIdentifier(identifier);

            tokenizer.advance();
            while(matchSymbol(',')) {
                writeSymbol(',');

                tokenizer.advance();
                expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
                String varName = tokenizer.identifier();
                writeIdentifier(varName);

                tokenizer.advance();
            }

            expectSymbol(';');
            writeSymbol(';');

            tokenizer.advance();
            writer.write("</varDec>\n");
        }
    }

    public void compileStatements() throws IOException {
        writer.write("<statements>\n");
        while (matchKeyWord(JackTokenizer.KeyWord.LET) || matchKeyWord(JackTokenizer.KeyWord.IF) || matchKeyWord(JackTokenizer.KeyWord.WHILE) || matchKeyWord(JackTokenizer.KeyWord.DO) || matchKeyWord(JackTokenizer.KeyWord.RETURN)) {
            switch (tokenizer.keyWord()) {
                case LET -> {
                    compileLet();
                    tokenizer.advance();
                }
                case IF -> compileIf();
                case WHILE -> {
                    compileWhile();
                    tokenizer.advance();
                }
                case DO -> {
                    compileDo();
                    tokenizer.advance();
                }
                case RETURN -> {
                    compileReturn();
                    tokenizer.advance();
                }
                default -> throw new RuntimeException("Unexpected token: " + tokenizer.keyWord());
            }
        }
        writer.write("</statements>\n");
    }

    public void compileDo() throws IOException {
        writer.write("<doStatement>\n");
        writeKeyWord(JackTokenizer.KeyWord.DO);

        tokenizer.advance();
        expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
        String identifier = tokenizer.identifier();
        writeIdentifier(identifier);

        tokenizer.advance();
        if (matchSymbol('(')){
            writeSymbol('(');

            tokenizer.advance();
            compileExpressionList();

            expectSymbol(')');
            writeSymbol(')');

            tokenizer.advance();
        } else if (matchSymbol('.')){
            writeSymbol('.');

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String subroutineName = tokenizer.identifier();
            writeIdentifier(subroutineName);

            tokenizer.advance();
            expectSymbol('(');
            writeSymbol('(');

            tokenizer.advance();
            compileExpressionList();

            expectSymbol(')');
            writeSymbol(')');

            tokenizer.advance();
        }

        expectSymbol(';');
        writeSymbol(';');

        writer.write("</doStatement>\n");
    }

    public void compileLet() throws IOException {
        writer.write("<letStatement>\n");
        writeKeyWord(JackTokenizer.KeyWord.LET);

        tokenizer.advance();
        expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
        String identifier = tokenizer.identifier();
        writeIdentifier(identifier);

        tokenizer.advance();
        if (matchSymbol('[')){
            writeSymbol('[');

            tokenizer.advance();
            compileExpression();

            expectSymbol(']');
            writeSymbol(']');

            tokenizer.advance();
        }
        expectSymbol('=');
        writeSymbol('=');

        tokenizer.advance();
        compileExpression();

        expectSymbol(';');
        writeSymbol(';');

        writer.write("</letStatement>\n");
    }

    public void compileWhile() throws IOException {
        writer.write("<whileStatement>\n");
        writeKeyWord(JackTokenizer.KeyWord.WHILE);

        tokenizer.advance();
        expectSymbol('(');
        writeSymbol('(');

        tokenizer.advance();
        compileExpression();

//        tokenizer.advance();
        expectSymbol(')');
        writeSymbol(')');

        tokenizer.advance();
        expectSymbol('{');
        writeSymbol('{');

        tokenizer.advance();
        compileStatements();

//        tokenizer.advance();
        expectSymbol('}');
        writeSymbol('}');

        writer.write("</whileStatement>\n");
    }

    public void compileReturn() throws IOException {
        writer.write("<returnStatement>\n");

        writeKeyWord(JackTokenizer.KeyWord.RETURN);

        tokenizer.advance();
        if (matchSymbol(';')){
            writeSymbol(';');
            writer.write("</returnStatement>\n");
            return;
        }else {
            compileExpression();
            expectSymbol(';');
            writeSymbol(';');
        }
        writer.write("</returnStatement>\n");
    }

    public void compileIf() throws IOException {
        writer.write("<ifStatement>\n");
        writeKeyWord(JackTokenizer.KeyWord.IF);

        tokenizer.advance();
        expectSymbol('(');
        writeSymbol('(');

        tokenizer.advance();
        compileExpression();

//        tokenizer.advance();
        expectSymbol(')');
        writeSymbol(')');

        tokenizer.advance();
        expectSymbol('{');
        writeSymbol('{');

        tokenizer.advance();
        compileStatements();

//        tokenizer.advance();
        expectSymbol('}');
        writeSymbol('}');

        tokenizer.advance();
        if (matchKeyWord(JackTokenizer.KeyWord.ELSE)) {
            writeKeyWord(JackTokenizer.KeyWord.ELSE);

            tokenizer.advance();
            expectSymbol('{');
            writeSymbol('{');

            tokenizer.advance();
            compileStatements();

            expectSymbol('}');
            writeSymbol('}');

            tokenizer.advance();
        }
        writer.write("</ifStatement>\n");
    }

    public void compileExpression() throws IOException {
        writer.write("<expression>\n");
        compileTerm();

        while (contains(OP, tokenizer.symbol())){
            writeSymbol(tokenizer.symbol());
            tokenizer.advance();
            compileTerm();
        }
        writer.write("</expression>\n");
    }

    private char[] OP = {'+','-','*','/','&','|','<','>','='};

    private char[] UNARYOP = {'-','~'};

    private String[] KEYWORDCONSTANT = {"true", "false", "null", "this"};

    private boolean contains(char[] charArr, char ch){
        for (int i = 0; i < charArr.length; i++) {
            if (charArr[i] == ch) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(String[] arr, String str){
        for (int i = 0; i < arr.length; i++) {
            if (Objects.equals(arr[i], str)) {
                return true;
            }
        }
        return false;
    }

    public void compileTerm() throws IOException {
        writer.write("<term>\n");
        if (matchTokenType(JackTokenizer.TokenType.INT_CONST)){
            writer.write("<integerConstant>" + tokenizer.intVal() + "</integerConstant>\n");
            tokenizer.advance();

            writer.write("</term>\n");
            return;
        }
        if (matchTokenType(JackTokenizer.TokenType.STRING_CONST)){
            writer.write("<stringConstant>" + tokenizer.stringVal() + "</stringConstant>\n");
            tokenizer.advance();

            writer.write("</term>\n");
            return;

        }
        if (contains(KEYWORDCONSTANT,tokenizer.stringVal())){
            writeKeyWord(tokenizer.keyWord());

            tokenizer.advance();

            writer.write("</term>\n");
            return;

        }
        if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)){
            writeIdentifier(tokenizer.identifier());

            tokenizer.advance();
            if (matchSymbol('[')){
                writeSymbol('[');

                tokenizer.advance();
                compileExpression();

                expectSymbol(']');
                writeSymbol(']');
            } else if (matchSymbol('(')){ // subroutine call
                writeSymbol('(');

                tokenizer.advance();
                compileExpressionList();

                expectSymbol(')');
                writeSymbol(')');

            } else if (matchSymbol('.')){ // subroutine call
                writeSymbol('.');

                tokenizer.advance();
                expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
                String subroutineName = tokenizer.identifier();
                writeIdentifier(subroutineName);

                tokenizer.advance();
                expectSymbol('(');
                writeSymbol('(');

                tokenizer.advance();
                compileExpressionList();

                expectSymbol(')');
                writeSymbol(')');

            } else if (matchSymbol(';')){

                writer.write("</term>\n");
                return;
            } else {
                writer.write("</term>\n");
                return;
            }
        }
        if (matchSymbol('(')){
            writeSymbol('(');

            tokenizer.advance();
            compileExpression();

            expectSymbol(')');
            writeSymbol(')');

        }
        if (contains(UNARYOP, tokenizer.symbol())){
            writeSymbol(tokenizer.symbol());

            tokenizer.advance();
            compileTerm();

            writer.write("</term>\n");
            return;
        }

        tokenizer.advance();
        writer.write("</term>\n");
    }

    public void compileExpressionList() throws IOException {
        writer.write("<expressionList>\n");

        if (!matchSymbol(')')) {

            compileExpression();

            while (matchSymbol(',')) {
                writeSymbol(',');

                tokenizer.advance();
                compileExpression();
            }
        }
        writer.write("</expressionList>\n");
    }

    private void expectKeyword(JackTokenizer.KeyWord keyWord) {
        if (tokenizer.tokenType() != JackTokenizer.TokenType.KEYWORD) {
            throw new RuntimeException("Unexpected token type: " + tokenizer.tokenType() + " expected keyword");
        }
        if (tokenizer.keyWord() != keyWord) {
            throw new RuntimeException("Unexpected token: " + tokenizer.keyWord() + " expected: " + keyWord);
        }
    }

    private boolean matchKeyWord(JackTokenizer.KeyWord keyWord) {
        if (tokenizer.tokenType() != JackTokenizer.TokenType.KEYWORD) {
            return false;
        }
        if (tokenizer.keyWord() != keyWord) {
            return false;
        }
        return true;
    }

    private void expectSymbol(char symbol) {
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL) {
            throw new RuntimeException("Unexpected token type: " + tokenizer.tokenType() + " expected symbol");
        }
        if (tokenizer.symbol() != symbol) {
            throw new RuntimeException("Unexpected token: " + tokenizer.symbol() + " expected: " + symbol);
        }
    }

    private boolean matchSymbol(char symbol) {
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL) {
            return false;
        }
        if (tokenizer.symbol() != symbol) {
            return false;
        }
        return true;
    }

    private void expectTokenType(JackTokenizer.TokenType tokenType) {
        if (tokenizer.tokenType() != tokenType) {
            throw new RuntimeException("Unexpected token: " + tokenizer.tokenType());
        }
    }

    private boolean matchTokenType(JackTokenizer.TokenType tokenType) {
        if (tokenizer.tokenType() != tokenType) {
            return false;
        }
        return true;
    }

    private void writeKeyWord(JackTokenizer.KeyWord keyWord) throws IOException {
        String keywordString = keyWord.toString().toLowerCase();
        writer.write("<keyword>"+keywordString+"</keyword>\n");
    }

    private void writeSymbol(char symbol) throws IOException {
        String symbolString = String.valueOf(symbol);
        if (symbol=='<'){
            symbolString = "&lt;";
        }
        if (symbol=='>'){
            symbolString = "&gt;";
        }
        if (symbol=='"'){
            symbolString = "&quot;";
        }
        if (symbol=='&'){
            symbolString = "&amp;";
        }
        writer.write("<symbol>"+symbolString+"</symbol>\n");
    }

    private void writeIdentifier(String identifier) throws IOException {
        writer.write("<identifier>"+identifier+"</identifier>\n");
    }

}

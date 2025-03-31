import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class CompilationEngine {
    JackTokenizer tokenizer;
    Writer writer;
    SymbolTable symbolTable;
    VMWriter vmwriter;
    String className;

    public CompilationEngine(File inputFile, File outputFile) throws IOException {
        tokenizer = new JackTokenizer(inputFile);
//        writer = new FileWriter(outputFile);
        vmwriter = new VMWriter(outputFile);
        symbolTable = new SymbolTable();
        compileClass();
        vmwriter.close();
    }

    public void compileClass() throws IOException {
        tokenizer.advance();
        expectKeyword(JackTokenizer.KeyWord.CLASS);

        tokenizer.advance();
        expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
        String identifier = tokenizer.identifier();
        className = identifier;
//        writeIdentifierExt(identifier,"","CLASS");
//        writeIdentifier(identifier); // class identifier

        tokenizer.advance();
        expectSymbol('{');

        tokenizer.advance();
        compileClassVarDec();
        compileSubroutine();


        expectSymbol('}');
    }

    public void compileClassVarDec() throws IOException {
        while (matchKeyWord(JackTokenizer.KeyWord.STATIC) | matchKeyWord(JackTokenizer.KeyWord.FIELD)){
            String category = tokenizer.keyWord().toString();

            tokenizer.advance();
            String type;
            if (!matchKeyWord(JackTokenizer.KeyWord.INT) && !matchKeyWord(JackTokenizer.KeyWord.CHAR) && !matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) && !matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                throw new RuntimeException("Unexpected token: " + tokenizer.keyWord());
            }
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                type = tokenizer.identifier();
            } else {
                type = tokenizer.keyWord().toString();
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
            symbolTable.define(identifier,type, SymbolTable.Kind.valueOf(category));
//            writeIdentifierExt(identifier,type,category);

            tokenizer.advance();
            while(matchSymbol(',')) {

                tokenizer.advance();
                expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
                String varName = tokenizer.identifier();
                symbolTable.define(varName,type, SymbolTable.Kind.valueOf(category));
//                writeIdentifierExt(varName,type,category);

                tokenizer.advance();
            }

            expectSymbol(';');

            tokenizer.advance();
        }
    }

    public void compileSubroutine() throws IOException {

        while(matchKeyWord(JackTokenizer.KeyWord.METHOD)||matchKeyWord(JackTokenizer.KeyWord.FUNCTION)||matchKeyWord(JackTokenizer.KeyWord.CONSTRUCTOR)){
            symbolTable.startSubroutine();

            JackTokenizer.KeyWord functionType = tokenizer.keyWord();

            if (functionType == JackTokenizer.KeyWord.METHOD) {
                symbolTable.define("this",className, SymbolTable.Kind.ARG);
            }

            if (functionType == JackTokenizer.KeyWord.CONSTRUCTOR) {
                symbolTable.define("this",className, SymbolTable.Kind.VAR);
            }

            tokenizer.advance();
            String type;
            if (!matchKeyWord(JackTokenizer.KeyWord.INT) && !matchKeyWord(JackTokenizer.KeyWord.CHAR) && !matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) && !matchKeyWord(JackTokenizer.KeyWord.VOID) && !matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                throw new RuntimeException("Unexpected token: " + tokenizer.keyWord());
            }
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                type = tokenizer.identifier();
//                writeIdentifier(tokenizer.identifier());
            } else {
                type = tokenizer.keyWord().toString();
//                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String functionIdentifier = tokenizer.identifier();
            if (functionType == JackTokenizer.KeyWord.CONSTRUCTOR && !functionIdentifier.equals("new")) {
                throw new RuntimeException("Unexpected token: " + tokenizer.identifier());
            }
//            writeIdentifier(identifier);
//            writeIdentifierExt(identifier,type,"SUBROUTINE");

            tokenizer.advance();
            expectSymbol('(');

            tokenizer.advance();
            compileParameterList();


//            tokenizer.advance();
            expectSymbol(')');


            tokenizer.advance();
            expectSymbol('{');

            tokenizer.advance();
            compileVarDec();

            vmwriter.writeFunction(className+"."+functionIdentifier, symbolTable.varCount(SymbolTable.Kind.VAR));
            if (functionType==JackTokenizer.KeyWord.METHOD) {
//                symbolTable.define("other",className, SymbolTable.Kind.ARG);
                vmwriter.writePush(VMWriter.Segment.ARG,symbolTable.indexOf("this"));
                vmwriter.writePop(VMWriter.Segment.POINTER,0);
            } else if (functionType==JackTokenizer.KeyWord.CONSTRUCTOR) {
                vmwriter.writePush(VMWriter.Segment.CONST,symbolTable.varCount(SymbolTable.Kind.FIELD));
                vmwriter.writeCall("Memory.alloc",1);
                vmwriter.writePop(VMWriter.Segment.LOCAL,symbolTable.indexOf("this"));
                vmwriter.writePush(VMWriter.Segment.LOCAL,symbolTable.indexOf("this"));
                vmwriter.writePop(VMWriter.Segment.POINTER,0);
            }
            compileStatements();

            expectSymbol('}');


            tokenizer.advance();
            System.out.println(functionIdentifier);
            System.out.println(symbolTable.subroutineKindTable.keySet());
            System.out.println(symbolTable.subroutineKindTable.values());
            System.out.println(symbolTable.subroutineTypeTable.values());




        }
    }

    public void compileParameterList() throws IOException {
        while (matchKeyWord(JackTokenizer.KeyWord.INT)||matchKeyWord(JackTokenizer.KeyWord.CHAR)||matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) || matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {

            String type;
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                type = tokenizer.identifier();
//                writeIdentifier(tokenizer.identifier());
            } else {
                type = tokenizer.keyWord().toString();
//                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
//            writeIdentifier(identifier);
//            writeIdentifierExt(identifier,type,"ARG");
            symbolTable.define(identifier,type, SymbolTable.Kind.ARG);

//            System.out.println(symbolTable.varCount(SymbolTable.Kind.ARG));
//            System.out.println(identifier);

            tokenizer.advance();
            if (matchSymbol(',')){

            } else {
                continue; // closing bracket
            }


            tokenizer.advance();
        }
    }

    public void compileVarDec() throws IOException {
        while (matchKeyWord(JackTokenizer.KeyWord.VAR)){

//            writeKeyWord(JackTokenizer.KeyWord.VAR);

            tokenizer.advance();
            String type;
            if(!matchKeyWord(JackTokenizer.KeyWord.INT) && !matchKeyWord(JackTokenizer.KeyWord.CHAR) && !matchKeyWord(JackTokenizer.KeyWord.BOOLEAN) && !matchTokenType(JackTokenizer.TokenType.IDENTIFIER)){
                throw new RuntimeException("Unexpected token type: " + tokenizer.tokenType());
            }
            if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)) {
                type = tokenizer.identifier();
//                writeIdentifier(tokenizer.identifier());
            } else {
                type = tokenizer.keyWord().toString();
//                writeKeyWord(tokenizer.keyWord());
            }

            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            String identifier = tokenizer.identifier();
//            writeIdentifier(identifier);
//            writeIdentifierExt(identifier,type,"VAR");
            symbolTable.define(identifier,type, SymbolTable.Kind.VAR);

            tokenizer.advance();
            while(matchSymbol(',')) {

                tokenizer.advance();
                expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
                String varName = tokenizer.identifier();
//                writeIdentifier(varName);
//                writeIdentifierExt(varName,type,"VAR");
                symbolTable.define(varName,type, SymbolTable.Kind.VAR);

                tokenizer.advance();
            }

            expectSymbol(';');

            tokenizer.advance();
        }
    }

    public void compileStatements() throws IOException {
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
    }

    public void compileDo() throws IOException {
        tokenizer.advance();
        expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
        String identifier = tokenizer.identifier();
//        writeIdentifier(identifier);

        tokenizer.advance();
        String subroutineName;
        int nArgs;
        if (matchSymbol('(')){

            if (Character.isLowerCase(identifier.charAt(0))){ // method
                if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                    vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                    vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                    vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                    vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
                } else {
                    vmwriter.writePush(VMWriter.Segment.POINTER,0);
                }
            }

            tokenizer.advance();
            nArgs = compileExpressionList();

            expectSymbol(')');

            if (Character.isLowerCase(identifier.charAt(0))){ // method
                vmwriter.writeCall(className+"."+identifier,nArgs+1);
            }

            vmwriter.writePop(VMWriter.Segment.TEMP,0);

            tokenizer.advance();
        } else if (matchSymbol('.')){
            tokenizer.advance();
            expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
            subroutineName = tokenizer.identifier();
//            writeIdentifier(subroutineName);

            if (Character.isLowerCase(identifier.charAt(0))){ // method
                if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                    vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                    vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                    vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                    vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
                }
            }

            tokenizer.advance();
            expectSymbol('(');

            tokenizer.advance();
            nArgs = compileExpressionList();

            expectSymbol(')');

            if (Character.isUpperCase(identifier.charAt(0))){ // function or constructor
                if (Objects.equals(subroutineName, "new")){ //constructor
                    vmwriter.writeCall(identifier+"."+subroutineName,nArgs);
                } else {//function
                    vmwriter.writeCall(identifier+"."+subroutineName,nArgs);
                }
            } else { //method
                vmwriter.writeCall(symbolTable.typeOf(identifier)+"."+subroutineName,nArgs+1);
            }

            vmwriter.writePop(VMWriter.Segment.TEMP,0);

            tokenizer.advance();
        } else {
            throw new RuntimeException("Unexpected token: " + tokenizer.tokenType());
        }

        expectSymbol(';');
    }

    public void compileLet() throws IOException {

        tokenizer.advance();
        expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
        String identifier = tokenizer.identifier();
//        writeIdentifier(identifier);
//        writeIdentifierExt(identifier, symbolTable.typeOf(identifier),symbolTable.kindOf(identifier).toString());
        int index = symbolTable.indexOf(identifier);

        boolean isArr = false;
        tokenizer.advance();
        if (matchSymbol('[')){
            //TODO

            isArr = true;
            if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
            } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
            } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
            } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
            }

            tokenizer.advance();
            compileExpression();

            vmwriter.writeArithmetic(VMWriter.Command.ADD);

            expectSymbol(']');

            tokenizer.advance();
        }
        expectSymbol('=');

        tokenizer.advance();
        compileExpression();

        if (isArr){
            vmwriter.writePop(VMWriter.Segment.TEMP,0);
            vmwriter.writePop(VMWriter.Segment.POINTER,1);
            vmwriter.writePush(VMWriter.Segment.TEMP,0);
            vmwriter.writePop(VMWriter.Segment.THAT,0);
        } else {
            if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                vmwriter.writePop(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
            } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                vmwriter.writePop(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
            } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                vmwriter.writePop(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
            } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                vmwriter.writePop(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
            }
        }

        expectSymbol(';');
    }

    int whileCount=0;
    public void compileWhile() throws IOException {

        tokenizer.advance();
        expectSymbol('(');

        int currentWhileCount = whileCount;
        whileCount++;
        vmwriter.writeLabel(String.format("while%d_begin",currentWhileCount));


        tokenizer.advance();
        compileExpression();

        vmwriter.writeArithmetic(VMWriter.Command.NOT);
        vmwriter.writeIf(String.format("while%d_end",currentWhileCount));

//        tokenizer.advance();
        expectSymbol(')');


        tokenizer.advance();
        expectSymbol('{');


        tokenizer.advance();
        compileStatements();

        vmwriter.writeGoto(String.format("while%d_begin",currentWhileCount));

//        tokenizer.advance();
        expectSymbol('}');

        vmwriter.writeLabel(String.format("while%d_end",currentWhileCount));
    }

    public void compileReturn() throws IOException {

        tokenizer.advance();
        if (matchSymbol(';')){
            vmwriter.writePush(VMWriter.Segment.CONST,0);
            vmwriter.writeReturn();

            return;
        }else {
            compileExpression();
            expectSymbol(';');

            vmwriter.writeReturn();
        }

    }

    int ifCount = 0;

    public void compileIf() throws IOException {


        tokenizer.advance();
        expectSymbol('(');


        tokenizer.advance();
        compileExpression();

        vmwriter.writeArithmetic(VMWriter.Command.NOT);
        int currentIfCount = ifCount;
        ifCount++;
        vmwriter.writeIf(String.format("if%d_else_branch",currentIfCount));

//        tokenizer.advance();
        expectSymbol(')');


        tokenizer.advance();
        expectSymbol('{');


        tokenizer.advance();
        compileStatements();

//        tokenizer.advance();
        expectSymbol('}');


        tokenizer.advance();
        vmwriter.writeGoto(String.format("if%d_end",currentIfCount));
        vmwriter.writeLabel(String.format("if%d_else_branch",currentIfCount));
        if (matchKeyWord(JackTokenizer.KeyWord.ELSE)) {


            tokenizer.advance();
            expectSymbol('{');


            tokenizer.advance();
            compileStatements();

            expectSymbol('}');


            tokenizer.advance();
        }
        vmwriter.writeLabel(String.format("if%d_end",currentIfCount));
    }

    public int compileExpressionList() throws IOException {
        int count = 0;
        if (!matchSymbol(')')) {

            compileExpression();
            count++;

            while (matchSymbol(',')) {
                count++;
                tokenizer.advance();
                compileExpression();
            }
        }
        return count;
    }

    public void compileExpression() throws IOException {

        compileTerm();

        while (contains(OP, tokenizer.symbol())){
            char symbol = tokenizer.symbol();

            tokenizer.advance();
            compileTerm();
            writeOp(symbol);
        }
    }

    public void compileTerm() throws IOException {

        if (matchTokenType(JackTokenizer.TokenType.INT_CONST)){
            vmwriter.writePush(VMWriter.Segment.CONST, tokenizer.intVal());
            tokenizer.advance();

            return;
        }
        if (matchTokenType(JackTokenizer.TokenType.STRING_CONST)){
            String stringVal = tokenizer.stringVal();
            vmwriter.writePush(VMWriter.Segment.CONST,stringVal.length());
            vmwriter.writeCall("String.new",1);
            for (int i = 0; i < stringVal.length(); i++) {
                vmwriter.writePush(VMWriter.Segment.CONST,(int)stringVal.charAt(i));
                vmwriter.writeCall("String.appendChar",2);
            }

            tokenizer.advance();

            return;
        }
        if (contains(KEYWORDCONSTANT,tokenizer.stringVal())){
            writeKeywordConstant(tokenizer.stringVal());
            tokenizer.advance();

            return;
        }
        if (matchTokenType(JackTokenizer.TokenType.IDENTIFIER)){
            String identifier = tokenizer.identifier();
//            writeIdentifier(tokenizer.identifier());



            tokenizer.advance();
            if (matchSymbol('[')){
                //TODO

                if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                    vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                    vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                    vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                    vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
                }

                tokenizer.advance();
                compileExpression();

                vmwriter.writeArithmetic(VMWriter.Command.ADD);
                vmwriter.writePop(VMWriter.Segment.POINTER,1);
                vmwriter.writePush(VMWriter.Segment.THAT,0);


                expectSymbol(']');
            } else if (matchSymbol('(')){ // subroutine call
                tokenizer.advance();
                compileExpressionList();

                expectSymbol(')');
            } else if (matchSymbol('.')){ // subroutine call
                tokenizer.advance();
                expectTokenType(JackTokenizer.TokenType.IDENTIFIER);
                String subroutineName = tokenizer.identifier();
//                writeIdentifier(subroutineName);

                tokenizer.advance();
                expectSymbol('(');

                tokenizer.advance();

                if (Character.isLowerCase(identifier.charAt(0))){
                    if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                        vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
                    } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                        vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
                    } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                        vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
                    } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                        vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
                    }
                    //                    vmwriter.writePop(VMWriter.Segment.POINTER,0);
                }

                int nArgs = compileExpressionList();

                if (Character.isUpperCase(identifier.charAt(0))){ // function or constructor
                    if (Objects.equals(subroutineName, "new")){ //constructor
                        vmwriter.writeCall(identifier+"."+subroutineName,nArgs);
                    } else {
                        vmwriter.writeCall(identifier+"."+subroutineName,nArgs);
                    }
                } else { // method
                    vmwriter.writeCall(symbolTable.typeOf(identifier)+"."+subroutineName,nArgs+1);
                }

                expectSymbol(')');


            } else if (matchSymbol(';')){ // end of line term
                if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                    vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                    vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                    vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                    vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
                }
                return;
            } else { // not end of line term
                if (symbolTable.kindOf(identifier)== SymbolTable.Kind.ARG){
                    vmwriter.writePush(VMWriter.Segment.ARG, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.VAR){
                    vmwriter.writePush(VMWriter.Segment.LOCAL, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.STATIC){
                    vmwriter.writePush(VMWriter.Segment.STATIC, symbolTable.indexOf(identifier));
                } else if (symbolTable.kindOf(identifier)== SymbolTable.Kind.FIELD){
                    vmwriter.writePush(VMWriter.Segment.THIS, symbolTable.indexOf(identifier));
                }
                return;
            }
        }
        if (matchSymbol('(')){
            tokenizer.advance();
            compileExpression();

            expectSymbol(')');
        }
        if (contains(UNARYOP, tokenizer.symbol())){
            char op = tokenizer.symbol();

            tokenizer.advance();
            compileTerm();
            writeUnaryOp(op);

            return;
        }

        tokenizer.advance();
    }

    private void writeOp(char op) throws IOException {
        switch (op) {
            case '+':vmwriter.writeArithmetic(VMWriter.Command.ADD);break;
            case '-':vmwriter.writeArithmetic(VMWriter.Command.SUB);break;
            case '*':vmwriter.writeCall("Math.multiply",2);break;
            case '/':vmwriter.writeCall("Math.divide",2);break;
            case '&':vmwriter.writeArithmetic(VMWriter.Command.AND);break;
            case '|':vmwriter.writeArithmetic(VMWriter.Command.OR);break;
            case '<':vmwriter.writeArithmetic(VMWriter.Command.LT);break;
            case '>':vmwriter.writeArithmetic(VMWriter.Command.GT);break;
            case '=':vmwriter.writeArithmetic(VMWriter.Command.EQ);break;
        }
    }

    private void writeUnaryOp(char op) throws IOException {
        switch (op) {
            case '-':vmwriter.writeArithmetic(VMWriter.Command.NEG);break;
            case '~':vmwriter.writeArithmetic(VMWriter.Command.NOT);break;
        }
    }

    private char[] OP = {'+','-','*','/','&','|','<','>','='};

    private char[] UNARYOP = {'-','~'};

    private String[] KEYWORDCONSTANT = {"true", "false", "null", "this"};

    private void writeKeywordConstant(String keywordConstant) throws IOException {
        switch (keywordConstant){
            case "true": vmwriter.writePush(VMWriter.Segment.CONST,1);vmwriter.writeArithmetic(VMWriter.Command.NEG);break;
            case "false": vmwriter.writePush(VMWriter.Segment.CONST,0);break;
            case "null":vmwriter.writePush(VMWriter.Segment.CONST,0);break;
            case "this":vmwriter.writePush(VMWriter.Segment.POINTER,0);break;
        }
    }

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

/*  - the identifier category (var, argument, static, field, class, subroutine);
    - whether the identifier is presently being defined (e.g., the identifier stands for a variable declared in a var statement) or used (e.g., the identifier stands for a variable in an expression);
    - whether the identifier represents a variable of one of the four kinds (var, argument, static, field ), and the running index assigned to the identifier by the symbol table.*/

    private void writeIdentifierExt(String identifier, String type, String category) throws IOException {
        SymbolTable.Kind kind;
        switch (category) {
            case "STATIC": kind = SymbolTable.Kind.STATIC; break;
            case "FIELD": kind = SymbolTable.Kind.FIELD; break;
            case "VAR": kind = SymbolTable.Kind.VAR; break;
            case "ARG":  kind = SymbolTable.Kind.ARG; break;
            default: kind = SymbolTable.Kind.NONE; break;
        }

        boolean notDefined = symbolTable.kindOf(identifier) == SymbolTable.Kind.NONE || kind == SymbolTable.Kind.NONE; // for notDefined=true, not found in table || not valid kind

        if (notDefined && kind!=SymbolTable.Kind.NONE) {
            symbolTable.define(identifier,type,kind);
        }

        int index = symbolTable.indexOf(identifier);

        writer.write(String.format("<identifier category=\" %s \" notDefined=\" %b \" kind = \" %s \" index = \" %d \">%s</identifier>\n", category, notDefined, kind.toString(), index, identifier));
    }

}

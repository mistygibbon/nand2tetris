import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class JackTokenizer {
    public enum TokenType {
        KEYWORD, SYMBOL,
        IDENTIFIER, INT_CONST,
        STRING_CONST
    }

    public enum KeyWord {
        CLASS, METHOD, FUNCTION,
        CONSTRUCTOR, INT,
        BOOLEAN, CHAR, VOID,
        VAR, STATIC, FIELD, LET,
        DO, IF, ELSE, WHILE,
        RETURN, TRUE, FALSE,
        NULL, THIS
    }

    private int tokenIndex = -1;

    public Scanner scanner;
    public Reader reader;
    public String fileContents;
    public ArrayList<String> fileContentsArray;
    public ArrayList<String> tokens = new ArrayList<String>();
    public ArrayList<TokenType> tokenTypes = new ArrayList<TokenType>();

    public final String[] KEYWORDS = {"class","constructor","function","method","field","static","var","int","char","boolean","void","true","false","null","this","let","do","if","else","while","return"};
    public final char[] SYMBOLS = {'{','}','(',')','[',']','.',',',';','+','-','*','/','&','|','<','>','=','~'};
    public final char[] DIGITS = {'0','1','2','3','4','5','6','7','8','9'};
    public final String azAZ = "abcdefghijklmnopqrstuuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public JackTokenizer(File inputFile) throws IOException {
        scanner = new Scanner(inputFile);
        reader = new BufferedReader(new FileReader(inputFile));

        fileContents = scanner.useDelimiter("\\A").next();
//        fileContentsArray = new ArrayList<String>(List.of(fileContents.split("\n+|\r\n+")));

        fileContents = fileContents.replaceAll("//.*|/\\*(.|\\n|\\r\\n)*?\\*/",""); // remove all comments

        int current = 0;
        while (current < fileContents.length()) {
            char currentChar = fileContents.charAt(current);

            if (contains(SYMBOLS, currentChar)) {
                tokens.add(String.valueOf(currentChar));
                tokenTypes.add(TokenType.SYMBOL);
                current++;
                continue;
            }

            if (contains(DIGITS, currentChar)) {
                StringBuilder digits = new StringBuilder();
                while (contains(DIGITS, currentChar)) {
                    digits.append(currentChar);
                    current++;
                    currentChar = fileContents.charAt(current);
                }
                tokens.add(digits.toString());
                tokenTypes.add(TokenType.INT_CONST);
                continue;
            }

            if (currentChar == '"') {
                StringBuilder string = new StringBuilder();
                current++;
                currentChar = fileContents.charAt(current);

                while (currentChar != '"') {
                    string.append(currentChar);
                    current++;
                    currentChar = fileContents.charAt(current);
                }
                current++;
                tokens.add(string.toString());
                tokenTypes.add(TokenType.STRING_CONST);
                continue;
            }

            if (azAZ.indexOf(currentChar)!=-1) {
                StringBuilder identifier = new StringBuilder();
                identifier.append(currentChar);
                current++;
                currentChar = fileContents.charAt(current);

                while (azAZ.indexOf(currentChar)!=-1 | contains(DIGITS, currentChar)) {
                    identifier.append(currentChar);
                    current++;
                    currentChar = fileContents.charAt(current);
                }


                tokens.add(identifier.toString());
                if (contains(KEYWORDS,identifier.toString())){
                    tokenTypes.add(TokenType.KEYWORD);
                } else {
                    tokenTypes.add(TokenType.IDENTIFIER);
                }
                continue;
            }

            current++;
        }


//        fileContents = (ArrayList<String>) Files.readAllLines(inputFile.toPath());

//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine().replaceAll("//.*|/\\*.*\\*/","").trim(); // remove all comments
//            if (line.isBlank()){
//                continue;
//            }
//
//            tokens.addAll(List.of(line.split("\\s+")));
//        }
//        for (int i = 0; i<tokens.size(); i++){
//            String element = tokens.get(i);
//            List<String> splitted = List.of(element.split(getSymbolSplitRegex()));
//            int splitSize = splitted.size();
//            tokens.addAll(i,splitted);
//            tokens.remove(i+splitSize);
//        }
    }

    private String getSymbolSplitRegex(){
        StringBuilder s = new StringBuilder();
        s.append("\\s+|");
        for (int i = 0; i < SYMBOLS.length; i++) {
            if (i>0){
                s.append("|");
            }
            s.append(String.format("(?<=\\%c)|(?=\\%c)", SYMBOLS[i], SYMBOLS[i]));
        }
        return s.toString();
    }

    private boolean contains(char[] charArr, char ch){
        for (int i = 0; i < charArr.length; i++) {
            if (charArr[i] == ch) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(String[] strArr, String ch){
        for (int i = 0; i < strArr.length; i++) {
            if (Objects.equals(strArr[i], ch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMoreTokens(){
        return tokenIndex < tokens.size();
    }
//
    public void advance(){
        if(hasMoreTokens()){
            tokenIndex++;
        }
    }
//
    public TokenType tokenType(){
        return tokenTypes.get(tokenIndex);
    }

    public KeyWord keyWord(){
        switch(tokens.get(tokenIndex)){
            case "class": return KeyWord.CLASS;
            case "constructor": return KeyWord.CONSTRUCTOR;
            case "function": return KeyWord.FUNCTION;
            case "method": return KeyWord.METHOD;
            case "field": return KeyWord.FIELD;
            case "static": return KeyWord.STATIC;
            case "var": return KeyWord.VAR;
            case "int": return KeyWord.INT;
            case "char": return KeyWord.CHAR;
            case "boolean": return KeyWord.BOOLEAN;
            case "void": return KeyWord.VOID;
            case "true": return KeyWord.TRUE;
            case "false": return KeyWord.FALSE;
            case "null": return KeyWord.NULL;
            case "this": return KeyWord.THIS;
            case "let": return KeyWord.LET;
            case "do": return KeyWord.DO;
            case "if": return KeyWord.IF;
            case "else": return KeyWord.ELSE;
            case "while": return KeyWord.WHILE;
            case "return": return KeyWord.RETURN;
            default: throw new Error("Unreachable");
        }
    }
//
    public char symbol(){
        return tokens.get(tokenIndex).charAt(0);
    }
//
    public String identifier(){
        return tokens.get(tokenIndex);
    }
//
    public int intVal(){
        return Integer.parseInt(tokens.get(tokenIndex));
    }
//
    public String stringVal(){
        return tokens.get(tokenIndex);
    }
}

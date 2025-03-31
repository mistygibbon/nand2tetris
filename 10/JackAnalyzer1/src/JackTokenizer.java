import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JackTokenizer {
    public enum TokenType {

    }

    public enum KeyWord {

    }

    public Scanner scanner;
    public Reader reader;
    public ArrayList<String> fileContents;
    public ArrayList<String> tokens;

    public final String[] KEYWORDS = {"class","constructor","function","method","field","static","var","int","char","boolean","void","true","false","null","this","let","do","if","else","while","return"};
    public final char[] SYMBOLS = {'{','}','(',')','[',']','-',',',';','+','-','*','/','&','|','<','>','=','~'};

    public JackTokenizer(File inputFile) throws IOException {
        scanner = new Scanner(inputFile);
//        reader = new BufferedReader(inputFile);
//        fileContents = (ArrayList<String>) Files.readAllLines(inputFile.toPath());

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replaceAll("//.*","").trim(); // replace all comments
            if (line.isBlank()){
                continue;
            }

            tokens.addAll(List.of(line.split(getSymbolSplitRegex())));
        }
    }

    private String getSymbolSplitRegex(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < SYMBOLS.length; i++) {
            if (i>0){
                s.append("|");
            }
            s.append(String.format("(?<=%c)|(?=%c)", SYMBOLS[i], SYMBOLS[i]));
        }
        s.append("|\\s+");
        return s.toString();
    }

//    public boolean hasMoreTokens(){
//        return scanner.hasNext();
//    }
//
//    public void advance(){
//        if(hasMoreTokens()){
//            scanner.has
//        }
//    }
//
//    public TokenType tokenType(){
//
//    }
//
//    public char symbol(){
//
//    }
//
//    public String identifier(){
//
//    }
//
//    public int intVal(){
//
//    }
//
//    public String stringVal(){
//
//    }
}

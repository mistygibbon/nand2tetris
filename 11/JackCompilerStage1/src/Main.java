import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        File outputFile = new File("./output.xml");
        FileWriter out = new FileWriter(outputFile);
        out.write("hello world!");
        out.close();
        File inputFile = new File(args[0]);
        JackTokenizer jt = new JackTokenizer(inputFile);
        CompilationEngine ct = new CompilationEngine(inputFile,outputFile);

        System.out.println("Hello, World!");
    }
}
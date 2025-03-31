import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = args[0];
        File input = new File(path);

        File outputFile = null;

        File[] files = new File[]{};
        if (input.isDirectory()){
            files = input.listFiles();
        } else if (input.isFile()){
            files = new File[]{input};
        }

        assert files != null;
        for (File currentFile : files){
            if (currentFile.isFile()&&currentFile.getName().endsWith(".jack")){
                outputFile = new File(currentFile.getPath().replace(".jack",".vm"));

                CompilationEngine ct = new CompilationEngine(currentFile, outputFile);
            }

        }

        System.out.println("Hello, World!");
    }
}
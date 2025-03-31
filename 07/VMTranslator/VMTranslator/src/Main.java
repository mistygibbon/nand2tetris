import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = args[0];
        File input = new File(path);
        String name = input.getName().replace(".vm","");
        System.out.println(name);

        File outputFile = null;

        File[] files = new File[]{};
        if (input.isDirectory()){
            outputFile = new File(input.getAbsolutePath() + "/"+name+".asm");
            files = input.listFiles();
        } else if (input.isFile()){
            outputFile = new File(name+".asm");
            files = new File[]{input};
        }

        CodeGenerator cg = new CodeGenerator(outputFile);
        cg.writeInit();

        boolean SysInitFound = false;
        
        for (int i = 0; i< Objects.requireNonNull(files).length; i++){
            File currentFile = files[i];
            if (currentFile.isFile()&&currentFile.getName().matches(".*\\.vm$")){ // operate only on .vm files
                String currentFilePath = currentFile.getPath();
                String currentFileName = currentFilePath.replace(".vm","");
                String cgName = currentFile.getName().replace(".vm","");
                File inputFile = new File(currentFilePath);

                Parser p = new Parser(inputFile);
                cg.setFileName(cgName);
                System.out.println(cgName);

                Stack<String> functions = new Stack<>();

                while (p.hasMoreCommands()){
                    p.advance();
                    if (p.commandType()== Parser.CommandType.C_PUSH||p.commandType()== Parser.CommandType.C_POP){
                        cg.writePushPop(p.commandType(),p.arg1(),p.arg2());
                    } else if (p.commandType() == Parser.CommandType.C_ARITHMETIC){
                        cg.writeArithmetic(p.arg1());
                    } else if (p.commandType() == Parser.CommandType.C_LABEL){
                        if (functions.empty()){
                            cg.writeLabel(p.arg1());
                        } else {
                            String functionLabel = functions.peek()+"$"+p.arg1();
                            cg.writeLabel(functionLabel);
                        }
                    } else if (p.commandType() == Parser.CommandType.C_GOTO){
                        if (functions.empty()){
                            cg.writeGoto(p.arg1());
                        } else {
                            String functionLabel = functions.peek()+"$"+p.arg1();
                            cg.writeGoto(functionLabel);
                        }
                    } else if (p.commandType() == Parser.CommandType.C_IF){
                        if (functions.empty()){
                            cg.writeIf(p.arg1());
                        } else {
                            String functionLabel = functions.peek()+"$"+p.arg1();
                            cg.writeIf(functionLabel);
                        }
                    }else if (p.commandType() == Parser.CommandType.C_FUNCTION){
                        if (Objects.equals(p.arg1(), "Sys.init")){
                            SysInitFound = true;
                        }
                        cg.writeFunction(p.arg1(),p.arg2());
                        functions.push(p.arg1());
                    } else if (p.commandType() == Parser.CommandType.C_RETURN){
                        cg.writeReturn();
//                        functions.pop();
                    } else if (p.commandType() == Parser.CommandType.C_CALL){
                        cg.writeCall(p.arg1(),p.arg2());
                    }
                }
            }
        }
        if (!SysInitFound){
            cg.writeFunction("Sys.init",0);
            cg.writeLabel("Sys.init.LOOP");
            cg.writeGoto("Sys.init.LOOP");
        }
        cg.close();

//        String fileName = path.replace(".vm","");
//        File inputFile = new File(path);
//        File outputFile = new File(fileName+".asm");
//
//        Parser p = new Parser(inputFile);
//        CodeGenerator cg = new CodeGenerator(outputFile);
//        cg.setFileName(fileName);

    }
}
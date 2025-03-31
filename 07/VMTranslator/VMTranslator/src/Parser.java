import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    enum CommandType{
        C_ARITHMETIC,
        C_PUSH,
        C_POP,
        C_LABEL,
        C_GOTO,
        C_IF,
        C_FUNCTION,
        C_RETURN,
        C_CALL
    }

    Scanner scanner;
    ArrayList<String> commands = new ArrayList<>();
    int index = 0;


    public Parser(File inputFile) throws FileNotFoundException {
        scanner = new Scanner(inputFile);
        commands.add("");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replaceAll("//.*","").trim();
            if (line.isBlank()){
                continue;
            }
            commands.add(line);
        }
    }

    public boolean hasMoreCommands() {
        return index < commands.size()-1;
    }

    public void advance(){
        index++;
    }

    public CommandType commandType(){
        String name = commands.get(index).split(" ")[0];
        switch (name){
            case "push":
                return CommandType.C_PUSH;
            case "pop":
                return CommandType.C_POP;
            case "add":
                return CommandType.C_ARITHMETIC;
            case "sub":
                return CommandType.C_ARITHMETIC;
            case "neg":
                return CommandType.C_ARITHMETIC;
            case "eq":
                return CommandType.C_ARITHMETIC;
            case "gt":
                return CommandType.C_ARITHMETIC;
            case "lt":
                return CommandType.C_ARITHMETIC;
            case "and":
                return CommandType.C_ARITHMETIC;
            case "or":
                return CommandType.C_ARITHMETIC;
            case "not":
                return CommandType.C_ARITHMETIC;
            case "label":
                return CommandType.C_LABEL;
            case "goto":
                return CommandType.C_GOTO;
            case "if-goto":
                return CommandType.C_IF;
            case "function":
                return CommandType.C_FUNCTION;
            case "return":
                return CommandType.C_RETURN;
            case "call":
                return CommandType.C_CALL;
            default:
                throw new RuntimeException("Unrecognized command type: " + name + " for "+ commands.get(index));
        }
    }

    public String arg1(){
        if (commandType() == CommandType.C_ARITHMETIC || commandType() == CommandType.C_RETURN){
            return commands.get(index);
        } else {
            return commands.get(index).split(" ")[1];
        }
    }

    public int arg2(){
        return Integer.parseInt(commands.get(index).split(" ")[2]);
    }

}

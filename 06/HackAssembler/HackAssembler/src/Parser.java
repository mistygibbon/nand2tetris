import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
    public enum CommandType{
        A_COMMAND,
        C_COMMAND,
        L_COMMAND,
    }

    private File assemblyFile;
    private Scanner scanner;
    private ArrayList<String> fileContents = new ArrayList<>();
    private Integer index = 0;

    public Parser(String filePath) throws FileNotFoundException {
        assemblyFile = new File(filePath);
        scanner = new Scanner(assemblyFile);
        fileContents.add(""); // Added to have a blank entry in the array as there should be no current commands before calling advance according to the textbook
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replace(" ","").replaceAll("//.*",""); // Replace spaces and comments with nothing
            if(line.isBlank()){
                continue;
            }
            fileContents.add(line);
        }
    }

    public boolean hasMoreCommands(){
        return index < fileContents.size()-1;
    }

    public void advance(){
        if(hasMoreCommands()){
            index++;
        }
    }

    private boolean isC(String line){
        String[] args = line.split("[=;]");
        // Size 3, dest=comp;jump
        // Size 2 dest=comp comp;jump
        // Size 1 comp
        if(args.length == 3){
            return isDest(args[0])&&isComp(args[1])&&isJump(args[2]);
        } else if(args.length == 2&&line.contains("=")){
            return isDest(args[0])&&isComp(args[1]);
        } else if(args.length == 2&&line.contains(";")){
            return isComp(args[0])&&isJump(args[1]);
        } else if(args.length ==1){
            return isComp(args[0]);
        }
        return false;
    }

    private boolean isDest(String dest){
        String[] validDest = {"M","D","MD","DM","A","AM","MA","AD","DA","AMD","ADM","DAM","DMA","MAD","MDA"};
        return Arrays.asList(validDest).contains(dest);
    }

    private boolean isComp(String comp){
        String[] validComp = {"0","1","-1","D","A","!D","!A","-D","-A","D+1","A+1","D-1","A-1","D+A","D-A","A-D","D&A","D|A","M","!M","-M","M+1","M-1","D+M","D-M","M-D","D&M","D|M"};
        return Arrays.asList(validComp).contains(comp);
    }

    private boolean isJump(String jump){
        String[] validJump = {"JGT","JEQ","JGE","JLT","JNE","JLE","JMP"};
        return Arrays.asList(validJump).contains(jump);
    }

    public CommandType commandType(){
        String line = fileContents.get(index);
        if(line.startsWith("@")){
            return CommandType.A_COMMAND; // A_COMMAND
        } else if (isC(line)) {
            return CommandType.C_COMMAND; // C_COMMAND
        } if (line.startsWith("(")&&line.endsWith(")")){
            return CommandType.L_COMMAND; //L_COMMAND
        }
        throw new RuntimeException("No such command type");
    }

    public String symbol(){
        String line = fileContents.get(index);
        if(commandType() == CommandType.A_COMMAND){
            return line.replace("@","");
        }
        if(commandType() == CommandType.L_COMMAND){
            return line.substring(1,line.length()-1);
        }
        throw new RuntimeException("No symbol found");
    }

    private HashMap<String,String> splitC(){
        String line = fileContents.get(index);
        String[] args = line.split("[=;]");
        HashMap<String,String> map = new HashMap<>();

        String dest, comp, jump;
        // Size 3, dest=comp;jump
        // Size 2 dest=comp comp;jump
        // Size 1 comp
        if(args.length == 3){
            map.put("dest",args[0]);
            map.put("comp",args[1]);
            map.put("jump",args[2]);
        } else if(args.length == 2&&line.contains("=")){
            map.put("dest",args[0]);
            map.put("comp",args[1]);
            map.put("jump",null);
        } else if(args.length == 2&&line.contains(";")){
            map.put("dest",null);
            map.put("comp",args[0]);
            map.put("jump",args[1]);
        } else if(args.length ==1){
            map.put("dest",null);
            map.put("comp",args[0]);
            map.put("jump",null);
        }
        return map;
    }

    public String dest(){
        if(commandType() == CommandType.C_COMMAND){
            return splitC().get("dest");
        }
        throw new RuntimeException("No dest found");
    }

    public String comp(){
        if(commandType() == CommandType.C_COMMAND){
            return splitC().get("comp");
        }
        throw new RuntimeException("No comp found");
    }

    public String jump(){
        if(commandType() == CommandType.C_COMMAND){
            return splitC().get("jump");
        }
        throw new RuntimeException("No jump found");
    }
}

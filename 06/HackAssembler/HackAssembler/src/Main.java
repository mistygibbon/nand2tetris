import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        String fileName = filePath.replace(".asm","");
        File assemblyFile = new File(filePath);

        Scanner scanner = new Scanner(assemblyFile);

        File machineCodeFile = new File(fileName + ".hack");
        if(!machineCodeFile.exists()) {
            machineCodeFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(fileName + ".hack");

//        while(scanner.hasNextLine()) {
//            String line = scanner.nextLine().replace(" ","");
//            if(line.isBlank()) {
//                System.out.println("Blank line");
//            } else {
//                System.out.println(line);
//            }
//        }

        SymbolTable st = new SymbolTable();

        // First pass
        Parser p= new Parser(filePath);
        int counter = 0;
        while (p.hasMoreCommands()){
            p.advance();
            if (p.commandType()== Parser.CommandType.L_COMMAND){
                if (!st.contains(p.symbol())){
                    st.addEntry(p.symbol(),counter);
                }
            }else{
                counter++;
            }
        }

        // Second pass
        p = new Parser(filePath);
        int baseAddress = 16;

        while (p.hasMoreCommands()) {
            p.advance();
            if (p.commandType() == Parser.CommandType.C_COMMAND) {
                fileWriter.write("111" + Code.comp(p.comp()) + Code.dest(p.dest()) + Code.jump(p.jump()) + "\n");
            }
            if (p.commandType() == Parser.CommandType.A_COMMAND) {
                String symbol = p.symbol();
                int address;
                if (symbol.matches("^\\d+$")){ // if symbol is just numbers
                    address = Integer.parseInt(symbol);
                } else if(st.contains(symbol)){ // if symbol is found in symbol table
                    address = st.getAddress(symbol);
                } else { // if symbol is not number and not found in symbol table
                    address = baseAddress;
                    st.addEntry(symbol,address);
                    baseAddress = incrementAddress(baseAddress);
                }
                String binaryString = Integer.toBinaryString(address);
                fileWriter.write("0" + String.format("%15s",binaryString).replace(' ','0')+"\n");
            }
        }

        fileWriter.close();
        System.out.println(filePath+" compiled successfully");
    }

    private static int incrementAddress(int baseAddress){
        if(baseAddress == 16383){ // Skip screen and keyboard address
            return 24577;
        } else {
            return baseAddress+1;
        }
    }
}
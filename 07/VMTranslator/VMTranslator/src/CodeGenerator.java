import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CodeGenerator {
    enum Command {
        C_PUSH,
        C_POP,
    }

    Writer VMWriter;
    int eqCount = 0, gtCount = 0, ltCount = 0, andCount = 0, orCount = 0, notCount = 0;
    String fName = "";

    public CodeGenerator(File outputFile) throws IOException {
        VMWriter = new FileWriter(outputFile);
    }

    public void setFileName(String fileName){
        fName = fileName;
    }

    public void writeArithmetic(String command) throws IOException {
        String write="";
        switch(command){
            case "add":
                write = """
                // add
                @SP // A=0
                M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
                A=M // Set address to value in SP
                D=M // Pop value to D register
                A=A-1 // Set address to 1 less than stack pointer
                M=D+M // Add values together
                """;
                break;
            case "sub":
                write = """
                // sub
                @SP // A=0
                M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
                A=M // Set address to value in SP
                D=M // Pop value to D register
                A=A-1 // Set address to 1 less than stack pointer
                M=M-D // Subtract values together
                """;
                break;
            case "neg":
                write = """
                // neg
                @SP
                A=M-1
                M=!M
                M=M+1
                """;
                break;
            case "eq":
                write = """
                // eq
                @SP // A=0
                M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
                A=M // Set address to value in SP
                D=M // Pop value to D register
                A=A-1 // Set address to 1 less than stack pointer
                D=M-D
                
                @EQ_TRUE_%d
                D;JEQ
                
                @SP
                A=M-1
                M=0
                @EQ_END_%d
                0;JMP
                
                (EQ_TRUE_%d)
                @SP
                A=M-1
                M=-1
                (EQ_END_%d)
                """;
                write = String.format(write, eqCount,eqCount,eqCount,eqCount);
                eqCount++;
                break;
            case "gt":
                write = """
                // gt
                @SP
                M=M-1
                A=M
                D=M
                A=A-1
                D=M-D
                
                @GT_TRUE_%d
                D;JGT
                
                @SP
                A=M-1
                M=0
                @GT_END_%d
                0;JMP
                
                (GT_TRUE_%d)
                @SP
                A=M-1
                M=-1
                (GT_END_%d)
                """;
                write = String.format(write, gtCount,gtCount,gtCount,gtCount);
                gtCount++;
                break;
            case "lt":
                write = """
                // lt
                @SP
                M=M-1
                A=M
                D=M
                A=A-1
                D=M-D
                
                @LT_TRUE_%d
                D;JLT
                
                @SP
                A=M-1
                M=0
                @LT_END_%d
                0;JMP                    
                
                (LT_TRUE_%d)
                @SP
                A=M-1
                M=-1
                (LT_END_%d)
                """;
                write = String.format(write, ltCount,ltCount,ltCount,ltCount);
                ltCount++;
                break;

            case "and":
                write = """
                // and
                @SP
                M=M-1
                A=M
                D=M
                A=A-1
                M=D&M
                """;
                break;

            case "or":
                write = """
                // or
                @SP
                M=M-1
                A=M
                D=M
                A=A-1
                M=D|M
                """;
                break;

            case "not":
                write = """
                // not
                @SP
                A=M-1
                M=!M
                """;
                break;
        }
        VMWriter.write(write.replaceAll("\n\n+","\n")+"\n");
    }

    public void writePushPop(Parser.CommandType command, String segment, int index) throws IOException {
        String write="";
        if (command== Parser.CommandType.C_PUSH){
            switch (segment){
                case "local":
                    write = """
                    // push local %d
                    @LCL
                    D=M // Save address of LCL to D register
                    @%d
                    A=D+A // Store address+index to address register
                    D=M // Store value of LCL[index] to D register
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "argument":
                    write = """
                    // push argument %d
                    @ARG
                    D=M // Save address of ARG to D register
                    @%d
                    A=D+A // Store address+index to address register
                    D=M // Store value of ARG[index] to D register
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "this":
                    write = """
                    // push this %d
                    @THIS
                    D=M // Save address of THIS to D register
                    @%d
                    A=D+A // Store address+index to address register
                    D=M // Store value of THIS[index] to D register
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "that":
                    write = """
                    // push that %d
                    @THAT
                    D=M // Save address of THAT to D register
                    @%d
                    A=D+A // Store address+index to address register
                    D=M // Store value of THAT[index] to D register
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "pointer":
                    write = """
                    //push pointer %d
                    @3
                    D=A // D=3
                    @%d
                    A=D+A // Store address+index to address register A=3+i
                    D=M // Store value of pointer[index] to D register
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "temp":
                    write = """
                    // push temp %d
                    @5
                    D=A // D=5
                    @%d
                    A=D+A // Store address+index to address register A=5+i
                    D=M // Store value of temp[index] to D register
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "constant":
                    write = """
                    // push constant %d
                    @%d
                    D=A
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "static":
                    write = """
                    // push static %d
                    @%s.%d
                    D=M
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
                    write = String.format(write, index, fName, index);
                    break;
            }
        } else if (command== Parser.CommandType.C_POP){
            switch (segment){
                case "local":
                    write= """
                    // pop local %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @R13
                    M=D
                    
                    @LCL
                    D=M
                    @%d
                    D=D+A
                    @R14
                    M=D
                    
                    @R13
                    D=M
                    
                    @R14
                    A=M
                    
                    M=D
                    """;
                    write = String.format(write, index, index);
                    break;
                case "argument":
                    write= """
                    // pop argument %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @R13
                    M=D
                    
                    @ARG
                    D=M
                    @%d
                    D=D+A
                    @R14
                    M=D
                    
                    @R13
                    D=M
                    
                    @R14
                    A=M
                    
                    M=D   
                    """;
                    write = String.format(write,index, index);
                    break;
                case "this":
                    write= """
                    // pop this %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @R13
                    M=D
                    
                    @THIS
                    D=M
                    @%d
                    D=D+A
                    @R14
                    M=D
                    
                    @R13
                    D=M
                    
                    @R14
                    A=M
                    
                    M=D   
                    """;
                    write = String.format(write, index, index);
                    break;
                case "that":
                    write= """
                    // pop that %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @R13
                    M=D
                    
                    @THAT
                    D=M
                    @%d
                    D=D+A
                    @R14
                    M=D
                    
                    @R13
                    D=M
                    
                    @R14
                    A=M
                    
                    M=D   
                    """;
                    write = String.format(write, index, index);
                    break;
                case "pointer":
                    write= """
                    // pop pointer %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @R13
                    M=D
                    
                    @3
                    D=A
                    @%d
                    D=D+A
                    @R14
                    M=D
                    
                    @R13
                    D=M
                    
                    @R14
                    A=M
                    
                    M=D   
                    """;
                    write = String.format(write, index, index);
                    break;
                case "temp":
                    write= """
                    // pop temp %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @R13
                    M=D
                    
                    @5
                    D=A
                    @%d
                    D=D+A
                    @R14
                    M=D
                    
                    @R13
                    D=M
                    
                    @R14
                    A=M
                    
                    M=D   
                    """;
                    write = String.format(write, index, index);
                    break;
                case "constant":
                    write = "// pop constant %d";
                    write = String.format(write, index);
                    break;
                case "static":
                    write= """
                    // pop static %d
                    @SP
                    M=M-1
                    A=M
                    D=M
                    
                    @%s.%d
                    M=D
                    """;
                    write = String.format(write, index, fName, index);
                    break;
            }
        } else {
            throw new IllegalArgumentException("Command not recognized");
        }
        VMWriter.write(write.replaceAll("\n\n+","\n")+"\n");
    }

    public void writeInit() throws IOException {
        String write = """
                // init
                @256
                D=A
                @SP
                M=D
                """;
        VMWriter.write(write);
        writeCall("Sys.init",0);
    }

    public void writeLabel(String label) throws IOException {
        String write = """
                // label %s
                (%s)
                
                """;
        write = String.format(write,label, label);
        VMWriter.write(write);
    }

    public void writeGoto(String label) throws IOException {
        String write = """
                // goto %s
                @%s
                0;JMP
                
                """;
        write = String.format(write,label, label);
        VMWriter.write(write);
    }

    public void writeIf(String label) throws IOException {
        String write = """
                // if-goto %s
                @SP
                M=M-1
                A=M
                D=M
                @%s
                D;JNE
                
                """;
        write = String.format(write,label, label);
        VMWriter.write(write);
    }

    int callCount = 0;

    public void writeCall(String functionName, int numArgs) throws IOException {
        String write = """
                    // call %s
                    @%s // return address
                    D=A
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    
                    // push LCL
                    @LCL
                    D=M
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    
                    // push ARG
                    @ARG
                    D=M
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    
                    // push THIS
                    @THIS
                    D=M
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    
                    // push THAT
                    @THAT
                    D=M
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    
                    // ARG = SP-n-5
                    @SP
                    D=M
                    @5
                    D=D-A
                    @%d
                    D=D-A
                    @ARG
                    M=D
                    
                    // LCL = SP
                    @SP
                    D=M
                    @LCL
                    M=D
                    
                    // goto %s
                    @%s
                    0;JMP
                    
                    (%s)
                    """;
        String returnLabel = functionName + "_RETURN" + callCount;
        callCount++;
        write = String.format(write,functionName, returnLabel, numArgs,functionName,functionName,returnLabel);
        VMWriter.write(write);
    }

    public void writeReturn() throws IOException {
        String write = """
                       // return
                       // FRAME=LCL
                       @LCL
                       D=M
                       
                       @R13
                       M=D
                       
                       // RET = *(FRAME-5)
                       @5
                       D=D-A
                       A=D
                       D=M
                       @R14
                       M=D
                       
                       // *ARG = pop()
                       @SP
                       M=M-1
                       A=M
                       D=M
                       
                       @ARG
                       A=M
                       M=D
                       
                       // SP = (ARG+1)
                       @ARG
                       D=M
                       D=D+1
                       @SP
                       M=D
                       
                       // THAT = *(FRAME-1)
                       @R13
                       D=M
                       A=D-1
                       D=M
                       @THAT
                       M=D
                       
                       // THIS = *(FRAME-2)
                       @R13
                       D=M
                       @2
                       A=D-A
                       D=M
                       @THIS
                       M=D
                       
                       // ARG = *(FRAME-3)
                       @R13
                       D=M
                       @3
                       A=D-A
                       D=M
                       @ARG
                       M=D
                       
                       // LCL = *(FRAME-4)
                       @R13
                       D=M
                       @4
                       A=D-A
                       D=M
                       @LCL
                       M=D
                       
                       // goto RET
                       @R14
                       A=M
                       0;JMP
                       
                       """;
        VMWriter.write(write);
    }

    public void writeFunction(String functionName, int numLocals) throws IOException {
        String write = """
                // function %s %d
                (%s)
                """;
        write = String.format(write,functionName, numLocals, functionName);
        for(int i=0; i<numLocals; i++){
            write += """
                    // push constant 0
                    @0
                    D=A
                    
                    @SP
                    M=M+1
                    A=M-1
                    M=D
                    """;
        }
        VMWriter.write(write+"\n");
    }

    public void close() throws IOException {
        VMWriter.close();
    }
}

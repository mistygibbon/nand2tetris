import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class VMWriter {
    Writer writer;

    public VMWriter(File outputFile) throws IOException {
        writer = new FileWriter(outputFile);
    }

    enum Segment {
        CONST,ARG,LOCAL,STATIC,THIS,THAT,POINTER,TEMP
    }

    enum Command {
        ADD,SUB,NEG,EQ,GT,LT,AND,OR,NOT
    }

    public void writePush(Segment segment, int index) throws IOException {
        writer.write(String.format("push %s %d",segment.toString().toLowerCase(),index));
    }

    public void writePop(Segment segment, int index) throws IOException {
        writer.write(String.format("pop %s %d",segment.toString().toLowerCase(),index));
    }

    public void writeArithmetic(Command command) throws IOException {
        writer.write(String.format("%s",command.toString().toLowerCase()));
    }

    public void writeLabel(String label) throws IOException {
        writer.write(String.format("label %s",label));
    }

    public void writeGoto(String label) throws IOException {
        writer.write(String.format("goto %s",label));
    }

    public void writeIf(String label) throws IOException {
        writer.write(String.format("if-goto %s",label));
    }

    public void writeCall(String name, int nArgs) throws IOException {
        writer.write(String.format("call %s %d",name,nArgs));
    }

    public void writeFunction(String name, int nLocals) throws IOException {
        writer.write(String.format("function %s %d",name,nLocals));
    }

    public void writeReturn() throws IOException {
        writer.write("return");
    }

    public void close() throws IOException {
        writer.close();
    }
}

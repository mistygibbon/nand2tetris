import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    File VMCodeFile;

    public ParserTest() throws IOException {
        VMCodeFile = new File("test" + ".vm");
        if(!VMCodeFile.exists()) {
            VMCodeFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter("test" + ".vm");
        fileWriter.write("push argument 0\n");
        fileWriter.write("add");
        fileWriter.close();
    }

    @Test
    public void test1() throws FileNotFoundException {
        Parser p = new Parser(VMCodeFile);
        p.advance();
        assertEquals(p.arg1(),"argument");
    }

    @Test
    public void test2() throws FileNotFoundException {
        Parser p = new Parser(VMCodeFile);
        p.advance();
        assertEquals(p.arg2(),0);
    }

    @Test
    public void test3() throws FileNotFoundException {
        Parser p = new Parser(VMCodeFile);
        p.advance();
        p.advance();
        assertEquals(p.arg1(),"add");
    }

}

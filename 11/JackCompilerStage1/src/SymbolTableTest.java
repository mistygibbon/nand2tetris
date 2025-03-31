import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SymbolTableTest {

    @Test
    // test index of class table
    public void testSymbolTable1() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.FIELD);
        assertEquals(0, symbolTable.indexOf("test"));
    }

    @Test
    // test index of subroutine table
    public void testSymbolTable2() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.ARG);
        assertEquals(0, symbolTable.indexOf("test"));
    }

    @Test
    // return -1 if element not found
    public void testSymbolTable3() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.ARG);
        symbolTable.startSubroutine();
        assertEquals(-1, symbolTable.indexOf("test"));
    }

    @Test
    // test varcount on class table
    public void testSymbolTable4() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.FIELD);
        assertEquals(1, symbolTable.varCount(SymbolTable.Kind.FIELD));
    }

    @Test
    // test varcount on class table
    public void testSymbolTable5() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.STATIC);
        assertEquals(0, symbolTable.varCount(SymbolTable.Kind.FIELD));
    }

    @Test
    // test varcount on subroutine table
    public void testSymbolTable6() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.ARG);
        assertEquals(1, symbolTable.varCount(SymbolTable.Kind.ARG));
    }

    @Test
    // test varcount on subroutine table
    public void testSymbolTable7() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.VAR);
        assertEquals(0, symbolTable.varCount(SymbolTable.Kind.ARG));
    }

    @Test
    // test kindOf on class table
    public void testSymbolTable8() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.STATIC);
        assertEquals(SymbolTable.Kind.STATIC, symbolTable.kindOf("test"));
    }

    @Test
    // test kindOf on subroutine table
    public void testSymbolTable9() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.VAR);
        assertEquals(SymbolTable.Kind.VAR, symbolTable.kindOf("test"));
    }

    @Test
    // test kindOf on subroutine table
    public void testSymbolTable10() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.VAR);
        assertEquals(SymbolTable.Kind.NONE, symbolTable.kindOf("test2"));
    }

    @Test
    // test typeOf on class table
    public void testSymbolTable11() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.define("test","int",SymbolTable.Kind.STATIC);
        assertEquals("int", symbolTable.typeOf("test"));
    }
}

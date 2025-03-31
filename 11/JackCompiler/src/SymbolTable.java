import java.util.*;

public class SymbolTable {

    enum Scope {
        CLASS,SUBROUTINE
    }

//    private Scope keywordCheck(JackTokenizer.KeyWord keyword) {
//        if (keyword == JackTokenizer.KeyWord.STATIC || keyword == JackTokenizer.KeyWord.FIELD) {
//            return Scope.CLASS;
//        }
//        if (keyword == JackTokenizer.KeyWord.ARG || keyword) {}
//    }

    public enum Kind {
        STATIC,FIELD,ARG,VAR,
        // CLASS,FUNCTION, METHOD, CONSTRUCTOR,
        NONE
    }

    public static Kind keywordToKind(JackTokenizer.KeyWord kw, Kind kind){
        switch (kw){
            case STATIC: return Kind.STATIC;
            case FIELD: return Kind.FIELD;
            case VAR: return Kind.VAR;
            default: return Kind.NONE;
        }
    }

    private Scope kindCheck(Kind kind) {
        if (kind == Kind.STATIC||kind == Kind.FIELD) {
            return Scope.CLASS;
        } else if (kind == Kind.ARG || kind == Kind.VAR) {
            return Scope.SUBROUTINE;
        }
        throw new IllegalArgumentException("Invalid kind: " + kind);
    };

    HashMap<String, String> classTypeTable;
    HashMap<String, Kind> classKindTable;

    HashMap<String, String> subroutineTypeTable;
    HashMap<String, Kind> subroutineKindTable;

    public SymbolTable() {
        classTypeTable = new LinkedHashMap<>();
        classKindTable = new LinkedHashMap<>();
        subroutineTypeTable = new LinkedHashMap<>();
        subroutineKindTable = new LinkedHashMap<>();
    }

    public void startSubroutine(){
        subroutineTypeTable.clear();
        subroutineKindTable.clear();
    }

    public void define(String name, String type, Kind kind){
        if (kindCheck(kind) == Scope.CLASS){
            classTypeTable.put(name, type);
            classKindTable.put(name, kind);
        } else if (kindCheck(kind) == Scope.SUBROUTINE){
            subroutineTypeTable.put(name, type);
            subroutineKindTable.put(name, kind);
        }
        return;
    }

    public int varCount(Kind kind){
        if (kindCheck(kind)==Scope.CLASS){
            return (int) classKindTable.values().stream().filter(elem->elem==kind).count();
        } else if (kindCheck(kind)==Scope.SUBROUTINE){
            return (int) subroutineKindTable.values().stream().filter(elem->elem==kind).count();
        }
        return 0;
    }

    public Kind kindOf(String name){
        Kind kind = subroutineKindTable.get(name);
        if (kind==null){
            kind = classKindTable.get(name);
            if (kind!=null){
                return kind;
            } else {
                return Kind.NONE;
            }
        }else {
            return kind;
        }
    }

    public String typeOf(String name){
        String type = subroutineTypeTable.get(name);
        if (type==null){
            type = classTypeTable.get(name);
            if (type!=null){
                return type;
            } else {
                throw new RuntimeException("Type not found for name: "+name);
            }
        }else {
            return type;
        }
    }

    public int indexOf(String name){
        if (subroutineKindTable.containsKey(name)){
            Kind requiredKind = subroutineKindTable.get(name);
            List<Map.Entry<String, Kind>> requiredKindList = subroutineKindTable.entrySet().stream().filter(entry->entry.getValue()==requiredKind).toList();
            for (int i=0; i<requiredKindList.size(); i++){
                if (Objects.equals(requiredKindList.get(i).getKey(), name)){
                    return i;
                }
            }
        } else if (classKindTable.containsKey(name)){
            Kind requiredKind = classKindTable.get(name);
            List<Map.Entry<String, Kind>> requiredKindList = classKindTable.entrySet().stream().filter(entry->entry.getValue()==requiredKind).toList();
            for (int i=0; i<requiredKindList.size(); i++){
                if (Objects.equals(requiredKindList.get(i).getKey(), name)){
                    return i;
                }
            }
        }
        return -1;
    }
}

public class WinningLineInfo {
    public enum Type {
        ROW, COL, DIAG_TL_BR, DIAG_TR_BL
    }

    private Type type;
    private int index;

    public WinningLineInfo(Type type, int index) {
        this.type = type;
        this.index = index;
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
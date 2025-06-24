public class WinningLineInfo {
    // Tipe kemenangan: baris, kolom, diagonal dari kiri atas ke kanan bawah, atau diagonal dari kanan atas ke kiri bawah
    public enum Type {
        ROW, COL, DIAG_TL_BR, DIAG_TR_BL
    }

    private Type type; // Tipe kemenangan
    private int index; // Indeks baris atau kolom yang menang (tidak relevan untuk diagonal)

    /**
     * Konstruktor untuk WinningLineInfo.
     * @param type Tipe kemenangan (ROW, COL, DIAG_TL_BR, DIAG_TR_BL).
     * @param index Indeks baris atau kolom yang menang. Gunakan -1 untuk diagonal.
     */
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
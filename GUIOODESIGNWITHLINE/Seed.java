package GUIOODESIGNWITHLINE;

public enum Seed {
    CROSS("X"), NOUGHT("O"), NO_SEED(" ");

    // Variabel privat
    private String icon;
    // Konstruktor (harus privat)
    private Seed(String icon) {
        this.icon = icon;
    }
    // Getter Publik
    public String getIcon() {
        return icon;
    }
}
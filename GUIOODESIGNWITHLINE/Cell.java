package GUIOODESIGNWITHLINE;

import java.awt.*;
/**
 * Kelas Cell memodelkan setiap sel individual dari papan permainan.
 */
public class Cell {
    // Mendefinisikan konstanta bernama untuk penggambaran
    public static final int SIZE = 120; // Lebar/tinggi sel (persegi)
    // Simbol (silang/lingkaran) ditampilkan di dalam sel, dengan padding dari batas
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;
    public static final int SEED_STROKE_WIDTH = 8; // Lebar goresan pena

    // Mendefinisikan properti (terlihat oleh paket)
    /** Konten sel ini (Seed.EMPTY, Seed.CROSS, atau Seed.NOUGHT) */
    Seed content;
    /** Baris dan kolom sel ini */
    int row, col;

    /** Konstruktor untuk menginisialisasi sel ini dengan baris dan kolom yang ditentukan */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    /** Mengatur ulang konten sel ini menjadi EMPTY, siap untuk permainan baru */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /** Menggambar dirinya sendiri pada kanvas grafis, diberikan konteks Graphics */
    public void paint(Graphics g) {
        // Menggunakan Graphics2D yang memungkinkan kita untuk mengatur goresan pena
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(SEED_STROKE_WIDTH,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Menggambar Seed jika tidak kosong
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;
        if (content == Seed.CROSS) {
            g2d.setColor(GameMain.COLOR_CROSS);  // Menggambar silang 2 garis
            int x2 = (col + 1) * SIZE - PADDING;
            int y2 = (row + 1) * SIZE - PADDING;
            g2d.drawLine(x1, y1, x2, y2);
            g2d.drawLine(x2, y1, x1, y2);
        } else if (content == Seed.NOUGHT) {  // Menggambar lingkaran
            g2d.setColor(GameMain.COLOR_NOUGHT);
            g2d.drawOval(x1, y1, SEED_SIZE, SEED_SIZE);
        }
    }
}
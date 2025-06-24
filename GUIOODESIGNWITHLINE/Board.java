package GUIOODESIGNWITHLINE;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Board merepresentasikan papan permainan Tic-Tac-Toe secara keseluruhan.
 * Ini mengelola array objek Cell, logika pemeriksaan kemenangan,
 * dan menyimpan informasi garis kemenangan.
 */
public class Board {
    // Definisi konstanta untuk ukuran papan
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS; // Lebar kanvas gambar
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS; // Tinggi kanvas gambar
    public static final int GRID_WIDTH = 8; // Lebar garis grid
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;

    Cell[][] cells; // Array 2D dari objek Cell
    private List<Point> winningLinePoints; // Menyimpan koordinat untuk garis kemenangan (titik awal dan akhir)

    /**
     * Konstruktor untuk menginisialisasi papan permainan.
     * Membuat objek Cell untuk setiap posisi di papan.
     */
    public Board() {
        cells = new Cell[ROWS][COLS]; // Mengalokasikan array 2D
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col); // Mengalokasikan setiap sel
            }
        }
        winningLinePoints = new ArrayList<>(); // Menginisialisasi daftar titik garis kemenangan
    }

    /**
     * Mengatur ulang semua sel menjadi Seed.NO_SEED dan menghapus informasi garis kemenangan,
     * siap untuk permainan baru.
     */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // Memanggil metode newGame() dari Cell
            }
        }
        winningLinePoints.clear(); // Menghapus informasi garis kemenangan
    }

    /**
     * Pemain yang diberikan melakukan langkah di (selectedRow, selectedCol).
     * Memperbarui konten cells[selectedRow][selectedCol]. Menghitung dan mengembalikan
     * status permainan baru (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     *
     * @param player Pemain saat ini (CROSS atau NOUGHT).
     * @param selectedRow Baris yang dipilih.
     * @param selectedCol Kolom yang dipilih.
     * @return Status permainan yang baru setelah langkah.
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player; // Menempatkan Seed pemain di sel yang dipilih

        // Memeriksa kondisi kemenangan dan menyimpan garis kemenangan
        // 3-dalam-baris
        if (cells[selectedRow][0].content == player &&
                cells[selectedRow][1].content == player &&
                cells[selectedRow][2].content == player) {
            // Menambahkan titik tengah sel awal dan akhir dari garis kemenangan
            addWinningLinePoints(selectedRow, 0, selectedRow, 2);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        // 3-dalam-kolom
        if (cells[0][selectedCol].content == player &&
                cells[1][selectedCol].content == player &&
                cells[2][selectedCol].content == player) {
            // Menambahkan titik tengah sel awal dan akhir dari garis kemenangan
            addWinningLinePoints(0, selectedCol, 2, selectedCol);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        // 3-dalam-diagonal utama (dari kiri atas ke kanan bawah)
        if (selectedRow == selectedCol &&
                cells[0][0].content == player &&
                cells[1][1].content == player &&
                cells[2][2].content == player) {
            // Menambahkan titik tengah sel awal dan akhir dari garis kemenangan
            addWinningLinePoints(0, 0, 2, 2);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        // 3-dalam-diagonal anti (dari kanan atas ke kiri bawah)
        if (selectedRow + selectedCol == 2 &&
                cells[0][2].content == player &&
                cells[1][1].content == player &&
                cells[2][0].content == player) {
            // Menambahkan titik tengah sel awal dan akhir dari garis kemenangan
            addWinningLinePoints(0, 2, 2, 0);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Tidak ada yang menang. Periksa apakah DRAW (semua sel terisi) atau PLAYING.
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING; // Masih ada sel kosong
                }
            }
        }
        return State.DRAW; // Tidak ada sel kosong, itu seri
    }

    /**
     * Metode pembantu untuk menyimpan titik awal dan akhir garis kemenangan.
     * Titik-titik ini adalah titik tengah sel.
     */
    private void addWinningLinePoints(int r1, int c1, int r2, int c2) {
        // Titik tengah sel (x, y) = (col * CELL_SIZE + CELL_SIZE / 2, row * CELL_SIZE + CELL_SIZE / 2)
        winningLinePoints.add(new Point(c1 * Cell.SIZE + Cell.SIZE / 2, r1 * Cell.SIZE + Cell.SIZE / 2));
        winningLinePoints.add(new Point(c2 * Cell.SIZE + Cell.SIZE / 2, r2 * Cell.SIZE + Cell.SIZE / 2));
    }

    /**
     * Mendapatkan daftar titik yang membentuk garis kemenangan.
     *
     * @return Daftar objek Point (dua poin: awal dan akhir garis).
     */
    public List<Point> getWinningLinePoints() {
        return winningLinePoints;
    }

    /**
     * Menggambar dirinya sendiri (papan dan isinya) pada kanvas grafis.
     *
     * @param g Objek Graphics yang digunakan untuk menggambar.
     */
    public void paint(Graphics g) {
        // Gambar garis grid
        g.setColor(Color.LIGHT_GRAY); // Warna abu-abu terang untuk garis grid
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0,
                    GRID_WIDTH, CANVAS_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
        }

        // Gambar Seed dari semua sel
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g); // Mendelegasikan penggambaran ke setiap sel
            }
        }
    }
}
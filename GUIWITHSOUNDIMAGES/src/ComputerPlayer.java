// ComputerPlayer.java
import java.util.Random;

public class ComputerPlayer {

    private Board board;
    private Seed computerSeed; // Seed yang digunakan komputer (CROSS atau NOUGHT)

    public ComputerPlayer(Board board, Seed computerSeed) {
        this.board = board;
        this.computerSeed = computerSeed;
    }

    /**
     * Memilih langkah untuk komputer.
     * Untuk AI level mudah: memilih sel kosong secara acak.
     * @return Array int {row, col} dari sel yang dipilih.
     * Mengembalikan null jika tidak ada sel kosong (seharusnya tidak terjadi jika dipanggil saat PLAYING).
     */
    public int[] makeMove() {
        // AI level mudah: Cari sel kosong secara acak
        Random rand = new Random();
        int availableCells = 0;

        // Hitung jumlah sel kosong
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    availableCells++;
                }
            }
        }

        if (availableCells == 0) {
            return null; // Tidak ada langkah yang bisa dibuat
        }

        // Pilih indeks acak dari sel kosong
        int targetIndex = rand.nextInt(availableCells);
        int currentIndex = 0;

        // Temukan sel kosong pada indeks target
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    if (currentIndex == targetIndex) {
                        System.out.println("Komputer memilih (" + row + ", " + col + ")"); // Debugging
                        return new int[]{row, col};
                    }
                    currentIndex++;
                }
            }
        }
        return null; // Seharusnya tidak tercapai
    }

    public Seed getComputerSeed() {
        return computerSeed;
    }

    public void setComputerSeed(Seed computerSeed) {
        this.computerSeed = computerSeed;
    }
}
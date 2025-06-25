import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    public static final Color COLOR_WIN_LINE = Color.RED; // Warna garis kemenangan
    public static final int WIN_LINE_THICKNESS = 12; // Ketebalan garis kemenangan
    public static final int WIN_LINE_OFFSET = 10; // Offset dari tepi sel untuk garis diagonal

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    private WinningLineInfo winningLineInfo = null;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
        // Reset informasi garis kemenangan saat game baru
        winningLineInfo = null;
    }

    /**
     * The given player makes a move on (selectedRow, selectedCol).
     * Update cells[selectedRow][selectedCol]. Compute and return the
     * new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Compute and return the new game state
        State newState;
        if (cells[selectedRow][0].content == player && cells[selectedRow][1].content == player && cells[selectedRow][2].content == player) { // 3-in-the-row
            newState = (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            winningLineInfo = new WinningLineInfo(WinningLineInfo.Type.ROW, selectedRow);
        } else if (cells[0][selectedCol].content == player && cells[1][selectedCol].content == player && cells[2][selectedCol].content == player) { // 3-in-the-column
            newState = (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            winningLineInfo = new WinningLineInfo(WinningLineInfo.Type.COL, selectedCol);
        } else if (selectedRow == selectedCol && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) { // 3-in-the-diagonal (top-left to bottom-right)
            newState = (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            winningLineInfo = new WinningLineInfo(WinningLineInfo.Type.DIAG_TL_BR, -1); // Index tidak relevan untuk diagonal
        } else if (selectedRow + selectedCol == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) { // 3-in-the-opposite-diagonal (top-right to bottom-left)
            newState = (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            winningLineInfo = new WinningLineInfo(WinningLineInfo.Type.DIAG_TR_BL, -1); // Index tidak relevan untuk diagonal
        } else {
            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
            boolean isDraw = true;
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        isDraw = false;
                        break;
                    }
                }
                if (!isDraw) break;
            }
            newState = isDraw ? State.DRAW : State.PLAYING;
            winningLineInfo = null; // Pastikan null jika tidak ada kemenangan
        }
        return newState;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    // `@Override` dihapus
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }

        // Gambar garis kemenangan jika ada
        if (winningLineInfo != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(COLOR_WIN_LINE);
            g2d.setStroke(new BasicStroke(WIN_LINE_THICKNESS)); // Atur ketebalan garis

            int x1, y1, x2, y2;

            switch (winningLineInfo.getType()) {
                case ROW:
                    // Garis horizontal di tengah baris yang menang
                    y1 = y2 = winningLineInfo.getIndex() * Cell.SIZE + Cell.SIZE / 2;
                    x1 = 0 + WIN_LINE_OFFSET; // Sedikit offset dari tepi
                    x2 = CANVAS_WIDTH - WIN_LINE_OFFSET;
                    g2d.drawLine(x1, y1, x2, y2);
                    break;
                case COL:
                    // Garis vertikal di tengah kolom yang menang
                    x1 = x2 = winningLineInfo.getIndex() * Cell.SIZE + Cell.SIZE / 2;
                    y1 = 0 + WIN_LINE_OFFSET; // Sedikit offset dari tepi
                    y2 = CANVAS_HEIGHT - WIN_LINE_OFFSET;
                    g2d.drawLine(x1, y1, x2, y2);
                    break;
                case DIAG_TL_BR:
                    // Garis diagonal dari kiri atas ke kanan bawah
                    x1 = 0 + WIN_LINE_OFFSET; y1 = 0 + WIN_LINE_OFFSET;
                    x2 = CANVAS_WIDTH - WIN_LINE_OFFSET; y2 = CANVAS_HEIGHT - WIN_LINE_OFFSET;
                    g2d.drawLine(x1, y1, x2, y2);
                    break;
                case DIAG_TR_BL:
                    // Garis diagonal dari kanan atas ke kiri bawah
                    x1 = CANVAS_WIDTH - WIN_LINE_OFFSET; y1 = 0 + WIN_LINE_OFFSET;
                    x2 = 0 + WIN_LINE_OFFSET; y2 = CANVAS_HEIGHT - WIN_LINE_OFFSET;
                    g2d.drawLine(x1, y1, x2, y2);
                    break;
            }
        }
    }
}
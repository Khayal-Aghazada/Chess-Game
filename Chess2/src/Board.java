// Represents the chessboard, holding all the cells and pieces for a game of chess.




















import java.awt.*;

public class Board {

    private int boardSize;
    private Cell[][] cells;

    //Constructor for the board. Initializes the cells and places the pieces in their initial positions.
    public Board(int boardSize) {

        this.boardSize = boardSize;

       cells = new Cell[boardSize][boardSize]; // 2D array of cells representing the 8x8 grid of the chessboard.

        initCells(); // Initializes the cells with their respective colors.
        initPieces(); // Places all pieces on the board.

        for (int i = 0; i < cells.length; i++){
            for (int j = 0; j < cells[i].length; j++){

                Piece piece = cells[i][j].getPiece();

                if (piece != null) {
                    System.out.println("(x,y) = (" + i + ", " + j + "); Name = " + piece.getName());
                }
            }
        }
    }

    // Initializes the cells on the board with alternating colors.
    private void initCells() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                // Set cell colors in a checkerboard pattern.
                Color color = (i + j) % 2 == 0 ? Color.WHITE : Color.BLACK;
                cells[i][j] = new Cell(i, j, color);
            }
        }
    }

    //Places all chess pieces on their initial positions on the board.
    private void initPieces() {

        Color color;
        int y = 0;

        for (int x = 0; x < 2; x++){
            color = x%2 == 0 ? Color.WHITE : Color.BLACK;

            for (int i = 0; i < 2*cells[0].length; i++) {

                if (i < boardSize && x == 0) {
                    y = boardSize-2;
                } else if (i >= boardSize && x == 0) {
                    y = boardSize-1;
                }  else if (i < boardSize && x == 1) {
                    y = 1;
                }  else if (i >= boardSize && x == 1) {
                    y = 0;
                }

                PieceType pieceType = PieceType.ptPawn;

                if (i == boardSize || i == 2*boardSize-1) {
                    pieceType = PieceType.ptRook;
                } else if (i == boardSize+1 || i == 2*boardSize-2) {
                    pieceType = PieceType.ptKnight;
                } else if (i == boardSize+2 || i == 2*boardSize-3) {
                    pieceType = PieceType.ptBishop;
                } else if (i == boardSize+3) {
                    pieceType = PieceType.ptQueen;
                } else if (i == boardSize+4) {
                    pieceType = PieceType.ptKing;
                }

                Piece piece = new Piece(pieceType, color);
                cells[y][i % boardSize].setPiece(piece);

            }
        }
    }
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}
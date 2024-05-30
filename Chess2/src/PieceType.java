import java.awt.*;

public class PieceType {

    private String name;

    public PieceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final PieceType ptPawn = new PieceType("Pawn");
    public static final PieceType ptKnight = new PieceType("Knight");
    public static final PieceType ptBishop = new PieceType("Bishop");
    public static final PieceType ptRook = new PieceType("Rook");
    public static final PieceType ptQueen = new PieceType("Queen");
    public static final PieceType ptKing = new PieceType("King");

}

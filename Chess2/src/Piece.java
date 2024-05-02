import java.awt.*;

public class Piece {
    private PieceType pieceType;
    private Color color;
    private boolean isCaptured;

    private String name;

    public Piece(PieceType pieceType, Color color) {
        this.pieceType = pieceType;
        this.color = color;
        this.isCaptured = false;
    }

    public String getName() {
        return (color.equals(Color.WHITE) ? "w" : "b") + pieceType.getName();
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Color getColor() {
        return color;
    }
}

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





 /*   public boolean isValid(Cell from, Cell to) {
        int dx = Math.abs(from.getX() - to.getX());
        int dy = Math.abs(from.getY() - to.getY());
        return switch (type.toLowerCase()) {
            case "pawn" -> isValidPawnMove(from, to, dx, dy);
            case "rook" -> dx == 0 || dy == 0; // Rooks move in a straight line along x or y axis
            case "bishop" -> dx == dy; // Bishops move diagonally
            case "knight" -> (dx == 2 && dy == 1) || (dx == 1 && dy == 2); // Knights move in 'L' shape
            case "queen" -> (dx == 0 || dy == 0 || dx == dy); // Queens move like both a rook and a bishop
            case "king" -> dx <= 1 && dy <= 1; // Kings move one square in any direction
            default -> false;
        };
    }

    /*private boolean isValidPawnMove(Cell from, Cell to, int dx, int dy) {
        int forwardDirection = this.color.equals("white") ? -1 : 1; // Direction factor based on color
        int intendedYMove = to.getY() - from.getY(); // Vertical movement calculation

        // Simple move forward
        boolean simpleMove = dx == 0 && intendedYMove == forwardDirection && to.getPiece() == null;

        // Double move from start position
        boolean initialMove = dx == 0 && Math.abs(intendedYMove) == 2 && to.getPiece() == null &&
                ((color.equals("white") && from.getY() == 6) || (color.equals("black") && from.getY() == 1));

        // Diagonal capture
        boolean captureMove = Math.abs(dx) == 1 && intendedYMove == forwardDirection &&
                to.getPiece() != null && !to.getPiece().getColor().equals(this.color);

        return simpleMove || initialMove || captureMove; // Return true if any valid move condition is met
    }


    // CHANGES REQUIRED
    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }*/
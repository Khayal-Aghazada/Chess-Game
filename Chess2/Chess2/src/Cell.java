import java.awt.*;

public class Cell {
    private int x, y;
    private Color color;
    private Piece piece; // Reference to the piece on this cell
    private boolean isSelected;

    // Constructor
    public Cell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.isSelected = false;
        this.piece = null; // Initially, no piece on this cell
    }

    // Getter and setter methods
    public int getX() { return x; }
    public int getY() { return y; }
    public Color getColor() { return color; }
    public boolean getIsSelected() { return isSelected; }
    public void setSelected(boolean isSelected) { this.isSelected = isSelected; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
}

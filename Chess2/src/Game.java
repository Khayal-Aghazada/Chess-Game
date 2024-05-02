import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame {
    final int boardSize = 8;
    private JButton sourceCell = null;
    private JButton targetCell = null;
    private Board board;
    private JButton[][] buttons = new JButton[boardSize][boardSize];
    private Color currentTurn = Color.WHITE; // White starts first

    public Game() {
        initializeGUI();
    }

    private void initializeGUI() {
        board = new Board(boardSize);
        initializeBoard();
        
        setTitle("Chess Game");
        setSize(boardSize * 100, boardSize * 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(boardSize, boardSize));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(100, 100));
                Cell cell = board.getCell(i, j);

                setCellColor(i, j, false);
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorderPainted(false);
                add(buttons[i][j]);

                Piece piece = cell.getPiece();
                
                if (piece != null) {
                    buttons[i][j].setIcon(new ImageIcon(getClass().getResource("/resources/" + piece.getName() + ".png")));
                }

                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton) e.getSource();
                        int curX = (int) clickedButton.getClientProperty("row");
                        int curY = (int) clickedButton.getClientProperty("col");
                        Piece piece = board.getCell(curX, curY).getPiece();

                        if (piece == null || piece.getColor() == currentTurn) {
                            
                            if (sourceCell == null && piece != null) { // Selecting piece of correct color
                                sourceCell = clickedButton;
                                setCellColor(curX, curY, true);
                            } else if (sourceCell == clickedButton) { // Deselecting
                                setCellColor(curX, curY, false);
                                sourceCell = null;
                            } else if (sourceCell != null && targetCell == null) { // Moving
                                targetCell = clickedButton;
                                setCellColor(curX, curY, true);
                                moveTo(curX, curY);
                                switchTurn(); // Switch turns after a move
                            }
                        }
                    }
                });

                buttons[i][j].putClientProperty("row", i);
                buttons[i][j].putClientProperty("col", j);
            }
        }
    }

    private void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void setCellColor(int x, int y, boolean isSelected) {
        Color cellColor;
        
        if (isSelected) {
            cellColor = new Color(144, 238, 144); // Highlight color
        } else {
            cellColor = (x + y) % 2 == 0 ? Color.LIGHT_GRAY : new Color(120, 120, 120);
        }
        
        buttons[x][y].setBackground(cellColor);
    }

    private void moveTo(int x, int y) {
        if (sourceCell != null && targetCell != null) {
            int sourceX = (int) sourceCell.getClientProperty("row");
            int sourceY = (int) sourceCell.getClientProperty("col");
            int targetX = (int) targetCell.getClientProperty("row");
            int targetY = (int) targetCell.getClientProperty("col");

            Cell source = board.getCell(sourceX, sourceY);
            Cell target = board.getCell(targetX, targetY);
            Piece piece = source.getPiece();
            
            if (piece != null && target.getPiece() == null || target.getPiece().getColor() != piece.getColor()) { // Only move if target is empty or contains an opponent's piece
                source.setPiece(null);
                target.setPiece(piece);
                buttons[sourceX][sourceY].setIcon(null);
                buttons[targetX][targetY].setIcon(new ImageIcon(getClass().getResource("/resources/" + piece.getName() + ".png")));
            }

            setCellColor(sourceX, sourceY, false);
            setCellColor(targetX, targetY, false);
            sourceCell = null;
            targetCell = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game());
    }
}

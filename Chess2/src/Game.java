import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame {
    final int boardSize = 8;
    private JButton sourceCell = null;
    private JButton targetCell = null;
    private Board board;
    private JButton[][] buttons = new JButton[boardSize][boardSize];
    private Color currentTurn = Color.WHITE; // White starts first

    private int whiteRemaining;
    private int blackRemaining;
    private Timer whiteTimer;
    private Timer blackTimer;
    private JLabel whiteLabel;
    private JLabel blackLabel;

    public Game() {
        int chosenTime = chooseTimeAmount();
        whiteRemaining = chosenTime;
        blackRemaining = chosenTime;

        whiteLabel = new JLabel("White Timer: " + formatTime(whiteRemaining));
        blackLabel = new JLabel("Black Timer: " + formatTime(blackRemaining));

        initializeGUI();
    }

    private void initializeGUI() {
        board = new Board(boardSize);
        initializeBoard();

        setTitle("Chess Game");
        setSize(boardSize * 100 + 50, boardSize * 100 + 100); // Extra space for the timers
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardPanel.add(buttons[i][j]);
            }
        }

        // Styling the timer labels
        whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        whiteLabel.setFont(new Font("Arial", Font.BOLD, 20));
        whiteLabel.setOpaque(true);
        whiteLabel.setBackground(Color.WHITE);
        whiteLabel.setForeground(Color.BLACK);
        whiteLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        whiteLabel.setPreferredSize(new Dimension(200, 50));

        blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blackLabel.setFont(new Font("Arial", Font.BOLD, 20));
        blackLabel.setOpaque(true);
        blackLabel.setBackground(Color.BLACK);
        blackLabel.setForeground(Color.WHITE);
        blackLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        blackLabel.setPreferredSize(new Dimension(200, 50));

        // Positioning the timers
        JPanel whiteTimerPanel = new JPanel();
        whiteTimerPanel.add(whiteLabel);
        whiteTimerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel blackTimerPanel = new JPanel();
        blackTimerPanel.add(blackLabel);
        blackTimerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(blackTimerPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(whiteTimerPanel, BorderLayout.SOUTH);
        setVisible(true);

        initializeTimers();
    }


    private int chooseTimeAmount() {
        String[] options = {"1 minute", "5 minutes", "10 minutes", "15 minutes", "30 minutes", "45 minutes"};
        int[] times = {60, 300, 600, 900, 1800, 2700}; // corresponding times in seconds

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(400, 150)); // Increase the size of the panel

        // Load and resize the chess icon
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/wpawn.png"));
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(75, 75,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);  // transform it back

        JLabel iconLabel = new JLabel(icon);
        panel.add(iconLabel, BorderLayout.WEST);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Choose the time amount per player:"));

        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedIndex(2);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        comboBox.setBackground(Color.WHITE);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(new Color(17, 97, 124)); // Blue
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return c;
            }
        });
        inputPanel.add(comboBox);

        panel.add(inputPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, panel, "Time Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String input = (String) comboBox.getSelectedItem();
            if (input != null) {
                for (int i = 0; i < options.length; i++) {
                    if (options[i].equals(input)) {
                        return times[i];
                    }
                }
            }
        }

        return 600; // default to 10 minutes if no selection is made
    }


    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (whiteRemaining > 0) {
                    whiteRemaining--;
                    whiteLabel.setText("White Timer: " + formatTime(whiteRemaining));
                } else {
                    whiteTimer.stop();
                    showGameOverDialog("Game ended:\nTime ran out,\nBlack wins", "Black");
                }
            }
        });

        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (blackRemaining > 0) {
                    blackRemaining--;
                    blackLabel.setText("Black Timer: " + formatTime(blackRemaining));
                } else {
                    blackTimer.stop();
                    showGameOverDialog("Game ended:\nTime ran out,\nWhite wins", "White");
                }
            }
        });
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void showGameOverDialog(String message, String winner) {
        // Stop both timers when the game ends
        whiteTimer.stop();
        blackTimer.stop();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(50, 205, 50));

        JLabel messageLabel = new JLabel("<html>" + message.replaceAll("\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(Color.WHITE);

        ImageIcon crownIcon = new ImageIcon(getClass().getResource("/resources/" + (winner.equals("White") ? "wcrown" : "bcrown") + ".png"));
        JLabel crownLabel = new JLabel(crownIcon);

        JButton newGameButton = new JButton("Start New Game");
        newGameButton.setBackground(new Color(0, 128, 0)); // Set button background color to green
        newGameButton.setForeground(Color.WHITE); // Set button text color to white
        newGameButton.setFocusPainted(false);
        newGameButton.setFont(new Font("Arial", Font.BOLD, 24));
        newGameButton.setPreferredSize(new Dimension(100, 200));
        newGameButton.setBorder(BorderFactory.createRaisedBevelBorder()); // Make the button appear 3D
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose(); // Close the current game window
                SwingUtilities.invokeLater(() -> new Game()); // Start a new game
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Add some spacing

        panel.add(messageLabel, gbc);

        gbc.gridy++;
        panel.add(crownLabel, gbc);

        gbc.gridy++;
        panel.add(newGameButton, gbc);

        JDialog dialog = new JDialog(Game.this, "Game Over", true);
        dialog.getContentPane().add(panel);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(Game.this);
        dialog.setVisible(true);
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

                        if (sourceCell == null) { // Selecting piece of correct color
                            if (piece != null && piece.getColor() == currentTurn) {
                                sourceCell = clickedButton;
                                setCellColor(curX, curY, true);
                            }
                        } else { // Moving
                            targetCell = clickedButton;
                            if (moveTo(curX, curY)) {
                                switchTurn(); // Switch turns only after a successful move
                                if (!hasValidMoves(currentTurn)) {
                                    String winner = (currentTurn == Color.WHITE) ? "Black" : "White";
                                    String message = isKingInCheck(currentTurn) ? "Game ended:\nCHECKMATE,\n" + winner + " wins" : "Game ended:\nDRAW";

                                    // Stop the timers
                                    whiteTimer.stop();
                                    blackTimer.stop();

                                    showGameOverDialog(message, winner);
                                }
                            } else {
                                // Clear the selection if the move is invalid
                                if (sourceCell != null) {
                                    int sourceX = (int) sourceCell.getClientProperty("row");
                                    int sourceY = (int) sourceCell.getClientProperty("col");
                                    setCellColor(sourceX, sourceY, false);
                                }
                                setCellColor(curX, curY, false);
                                System.out.println("Invalid move");
                            }
                            sourceCell = null;
                            targetCell = null;
                        }
                    }
                });

                buttons[i][j].putClientProperty("row", i);
                buttons[i][j].putClientProperty("col", j);
            }
        }
    }

    private void switchTurn() {
        if (currentTurn == Color.WHITE) {
            blackTimer.start();
            whiteTimer.stop();
        } else {
            whiteTimer.start();
            blackTimer.stop();
        }
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

    private boolean moveTo(int x, int y) {
        if (sourceCell != null && targetCell != null) {
            int sourceX = (int) sourceCell.getClientProperty("row");
            int sourceY = (int) sourceCell.getClientProperty("col");
            int targetX = (int) targetCell.getClientProperty("row");
            int targetY = (int) targetCell.getClientProperty("col");

            Cell source = board.getCell(sourceX, sourceY);
            Cell target = board.getCell(targetX, targetY);
            Piece piece = source.getPiece();

            boolean validMove = false;

            if (piece != null) {
                System.out.println("Moving piece: " + piece.getName());
                if (piece.getPieceType().getName().equals("Pawn")) {
                    if (isValidPawnMove(sourceX, sourceY, targetX, targetY, piece.getColor())) {
                        validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                        if (validMove && (targetX == 0 || targetX == boardSize - 1)) {
                            promotePawn(targetX, targetY, piece.getColor());
                        }
                    }
                } else if (piece.getPieceType().getName().equals("Bishop")) {
                    if (isValidBishopMove(sourceX, sourceY, targetX, targetY)) {
                        validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                    }
                } else if (piece.getPieceType().getName().equals("Knight")) {
                    if (isValidKnightMove(sourceX, sourceY, targetX, targetY)) {
                        validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                    }
                } else if (piece.getPieceType().getName().equals("Rook")) {
                    if (isValidRookMove(sourceX, sourceY, targetX, targetY)) {
                        validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                    }
                } else if (piece.getPieceType().getName().equals("King")) {
                    if (isValidKingMove(sourceX, sourceY, targetX, targetY)) {
                        validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                    }
                } else if (piece.getPieceType().getName().equals("Queen")) {
                    if (isValidQueenMove(sourceX, sourceY, targetX, targetY)) {
                        validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                    }
                } else {
                    validMove = tryMove(source, target, sourceX, sourceY, targetX, targetY);
                }
            }

            setCellColor(sourceX, sourceY, false);
            setCellColor(targetX, targetY, false);

            // Check for checks on both kings
            highlightKingInCheck(Color.WHITE);
            highlightKingInCheck(Color.BLACK);

            sourceCell = null;
            targetCell = null;

            return validMove;
        }
        System.out.println("Invalid move");
        return false;
    }

    private void promotePawn(int x, int y, Color color) {
        // Options with piece names
        String[] pieceNames = {"Queen", "Rook", "Bishop", "Knight"};

        // Icons for each piece
        ImageIcon[] pieceIcons = {
                new ImageIcon(getClass().getResource("/resources/wqueen.png")),
                new ImageIcon(getClass().getResource("/resources/wrook.png")),
                new ImageIcon(getClass().getResource("/resources/wbishop.png")),
                new ImageIcon(getClass().getResource("/resources/wknight.png"))
        };

        // Panel to hold icons and names
        JPanel panel = new JPanel(new GridLayout(1, 4));
        JDialog dialog = new JDialog(this, "Choose piece for promotion", true);

        for (int i = 0; i < pieceNames.length; i++) {
            JButton button = new JButton(pieceNames[i], pieceIcons[i]);
            button.addActionListener(e -> {
                PieceType newPieceType;
                switch (button.getText()) {
                    case "Rook":
                        newPieceType = PieceType.ptRook;
                        break;
                    case "Bishop":
                        newPieceType = PieceType.ptBishop;
                        break;
                    case "Knight":
                        newPieceType = PieceType.ptKnight;
                        break;
                    case "Queen":
                    default:
                        newPieceType = PieceType.ptQueen;
                        break;
                }
                // Create the new piece based on the user's choice
                Piece newPiece = new Piece(newPieceType, color);
                // Set the new piece in the target cell
                board.getCell(x, y).setPiece(newPiece);
                // Update the button icon
                buttons[x][y].setIcon(new ImageIcon(getClass().getResource("/resources/" + newPiece.getName() + ".png")));
                dialog.dispose();
            });
            panel.add(button);
        }

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean tryMove(Cell source, Cell target, int sourceX, int sourceY, int targetX, int targetY) {
        // Simulate the move
        Piece movingPiece = source.getPiece();
        Piece targetPiece = target.getPiece();

        source.setPiece(null);
        target.setPiece(movingPiece);

        boolean isValid = !isKingInCheck(currentTurn);

        // Revert the move
        source.setPiece(movingPiece);
        target.setPiece(targetPiece);

        if (isValid) {
            movePiece(source, target, sourceX, sourceY, targetX, targetY);
        }

        return isValid;
    }

    private boolean isKingInCheck(Color kingColor) {
        int[] kingPosition = findKing(kingColor);
        if (kingPosition != null) {
            int kingX = kingPosition[0];
            int kingY = kingPosition[1];
            return isInCheck(kingX, kingY, kingColor);
        }
        return false;
    }

    private boolean isInCheck(int kingX, int kingY, Color kingColor) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Piece piece = board.getCell(i, j).getPiece();
                if (piece != null && piece.getColor() != kingColor) {
                    if (isValidMove(i, j, kingX, kingY, piece)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidMove(int sourceX, int sourceY, int targetX, int targetY, Piece piece) {
        switch (piece.getPieceType().getName()) {
            case "Pawn":
                return isValidPawnMove(sourceX, sourceY, targetX, targetY, piece.getColor());
            case "Bishop":
                return isValidBishopMove(sourceX, sourceY, targetX, targetY);
            case "Knight":
                return isValidKnightMove(sourceX, sourceY, targetX, targetY);
            case "Rook":
                return isValidRookMove(sourceX, sourceY, targetX, targetY);
            case "Queen":
                return isValidQueenMove(sourceX, sourceY, targetX, targetY);
            case "King":
                return isValidKingMove(sourceX, sourceY, targetX, targetY);
            default:
                return false;
        }
    }

    private void highlightKingInCheck(Color kingColor) {
        int[] kingPosition = findKing(kingColor);
        if (kingPosition != null) {
            int kingX = kingPosition[0];
            int kingY = kingPosition[1];
            if (isInCheck(kingX, kingY, kingColor)) {
                if (!hasValidMoves(kingColor)) {
                    buttons[kingX][kingY].setBackground(Color.RED); // Checkmate
                } else {
                    buttons[kingX][kingY].setBackground(Color.PINK); // Check
                }
            } else {
                setCellColor(kingX, kingY, false);
            }
        }
    }

    private int[] findKing(Color kingColor) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Piece piece = board.getCell(i, j).getPiece();
                if (piece != null && piece.getPieceType() == PieceType.ptKing && piece.getColor() == kingColor) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean isValidPawnMove(int sourceX, int sourceY, int targetX, int targetY, Color color) {
        int direction = (color == Color.WHITE) ? -1 : 1;
        int startRow = (color == Color.WHITE) ? 6 : 1;

        // Normal move
        if (targetX == sourceX + direction && targetY == sourceY && board.getCell(targetX, targetY).getPiece() == null) {
            return true;
        }

        // Double move from starting position
        if (sourceX == startRow && targetX == sourceX + 2 * direction && targetY == sourceY && board.getCell(targetX, targetY).getPiece() == null && board.getCell(sourceX + direction, sourceY).getPiece() == null) {
            return true;
        }

        // Capture move (diagonal)
        if (targetX == sourceX + direction && Math.abs(targetY - sourceY) == 1) {
            Piece targetPiece = board.getCell(targetX, targetY).getPiece();
            if (targetPiece != null && targetPiece.getColor() != color) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidBishopMove(int sourceX, int sourceY, int targetX, int targetY) {
        if (Math.abs(targetX - sourceX) == Math.abs(targetY - sourceY)) { // Check if move is diagonal
            int xDirection = (targetX - sourceX) > 0 ? 1 : -1;
            int yDirection = (targetY - sourceY) > 0 ? 1 : -1;
            int x = sourceX + xDirection;
            int y = sourceY + yDirection;

            while (x != targetX && y != targetY) {
                if (!isWithinBounds(x, y) || board.getCell(x, y).getPiece() != null) { // Path is not clear or out of bounds
                    return false;
                }
                x += xDirection;
                y += yDirection;
            }
            // Ensure target cell is either empty or contains opponent's piece
            return isWithinBounds(targetX, targetY) && (board.getCell(targetX, targetY).getPiece() == null || board.getCell(targetX, targetY).getPiece().getColor() != board.getCell(sourceX, sourceY).getPiece().getColor());
        }
        return false;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    private boolean isValidKnightMove(int sourceX, int sourceY, int targetX, int targetY) {
        if (!isWithinBounds(targetX, targetY)) {
            return false;
        }
        int dx = Math.abs(targetX - sourceX);
        int dy = Math.abs(targetY - sourceY);

        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            return board.getCell(targetX, targetY).getPiece() == null || board.getCell(targetX, targetY).getPiece().getColor() != board.getCell(sourceX, sourceY).getPiece().getColor();
        }

        return false;
    }

    private boolean isValidRookMove(int sourceX, int sourceY, int targetX, int targetY) {
        if (!isWithinBounds(targetX, targetY)) {
            return false;
        }
        if (sourceX == targetX || sourceY == targetY) {
            int xDirection = Integer.compare(targetX, sourceX);
            int yDirection = Integer.compare(targetY, sourceY);

            int x = sourceX + xDirection;
            int y = sourceY + yDirection;

            while ((x != targetX || y != targetY) && isWithinBounds(x, y)) {
                if (board.getCell(x, y).getPiece() != null) {
                    return false;
                }
                x += xDirection;
                y += yDirection;
            }
            return board.getCell(targetX, targetY).getPiece() == null || board.getCell(targetX, targetY).getPiece().getColor() != board.getCell(sourceX, sourceY).getPiece().getColor();
        }
        return false;
    }

    private boolean isValidQueenMove(int sourceX, int sourceY, int targetX, int targetY) {
        return isValidRookMove(sourceX, sourceY, targetX, targetY) || isValidBishopMove(sourceX, sourceY, targetX, targetY);
    }


    // Added instance variables to track if rooks and kings have moved
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteRookMovedKingSide = false;
    private boolean whiteRookMovedQueenSide = false;
    private boolean blackRookMovedKingSide = false;
    private boolean blackRookMovedQueenSide = false;

    private boolean isValidKingMove(int sourceX, int sourceY, int targetX, int targetY) {
        int dx = Math.abs(targetX - sourceX);
        int dy = Math.abs(targetY - sourceY);

        // Castling
        if (dx == 0 && dy == 2) {
            if (currentTurn == Color.WHITE && !whiteKingMoved) {
                // Kingside castling
                if (targetY == 6 && !whiteRookMovedKingSide && board.getCell(sourceX, 5).getPiece() == null && board.getCell(sourceX, 6).getPiece() == null) {
                    return !isInCheck(sourceX, sourceY, currentTurn) && !isInCheck(sourceX, 5, currentTurn) && !isInCheck(sourceX, 6, currentTurn);
                }
                // Queenside castling
                if (targetY == 2 && !whiteRookMovedQueenSide && board.getCell(sourceX, 1).getPiece() == null && board.getCell(sourceX, 2).getPiece() == null && board.getCell(sourceX, 3).getPiece() == null) {
                    return !isInCheck(sourceX, sourceY, currentTurn) && !isInCheck(sourceX, 2, currentTurn) && !isInCheck(sourceX, 3, currentTurn);
                }
            } else if (currentTurn == Color.BLACK && !blackKingMoved) {
                // Kingside castling
                if (targetY == 6 && !blackRookMovedKingSide && board.getCell(sourceX, 5).getPiece() == null && board.getCell(sourceX, 6).getPiece() == null) {
                    return !isInCheck(sourceX, sourceY, currentTurn) && !isInCheck(sourceX, 5, currentTurn) && !isInCheck(sourceX, 6, currentTurn);
                }
                // Queenside castling
                if (targetY == 2 && !blackRookMovedQueenSide && board.getCell(sourceX, 1).getPiece() == null && board.getCell(sourceX, 2).getPiece() == null && board.getCell(sourceX, 3).getPiece() == null) {
                    return !isInCheck(sourceX, sourceY, currentTurn) && !isInCheck(sourceX, 2, currentTurn) && !isInCheck(sourceX, 3, currentTurn);
                }
            }
        }

        // King moves one square in any direction
        if (dx <= 1 && dy <= 1) {
            return board.getCell(targetX, targetY).getPiece() == null || board.getCell(targetX, targetY).getPiece().getColor() != board.getCell(sourceX, sourceY).getPiece().getColor();
        }

        return false;
    }


    private void movePiece(Cell source, Cell target, int sourceX, int sourceY, int targetX, int targetY) {
        Piece piece = source.getPiece();
        Piece capturedPiece = target.getPiece();

        if (capturedPiece != null) {
            System.out.println("Captured piece: " + capturedPiece.getName());
        }

        // Handle castling
        if (piece.getPieceType() == PieceType.ptKing && Math.abs(targetY - sourceY) == 2) {
            // Kingside castling
            if (targetY == 6) {
                movePiece(board.getCell(sourceX, 7), board.getCell(sourceX, 5), sourceX, 7, sourceX, 5);
            }
            // Queenside castling
            else if (targetY == 2) {
                movePiece(board.getCell(sourceX, 0), board.getCell(sourceX, 3), sourceX, 0, sourceX, 3);
            }
            if (currentTurn == Color.WHITE) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        }

        // Mark king and rook as moved
        if (piece.getPieceType() == PieceType.ptKing) {
            if (currentTurn == Color.WHITE) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        }
        if (piece.getPieceType() == PieceType.ptRook) {
            if (sourceX == 7) {
                if (sourceY == 0) {
                    whiteRookMovedQueenSide = true;
                } else if (sourceY == 7) {
                    whiteRookMovedKingSide = true;
                }
            } else if (sourceX == 0) {
                if (sourceY == 0) {
                    blackRookMovedQueenSide = true;
                } else if (sourceY == 7) {
                    blackRookMovedKingSide = true;
                }
            }
        }

        source.setPiece(null);
        target.setPiece(piece);
        buttons[sourceX][sourceY].setIcon(null);
        buttons[targetX][targetY].setIcon(new ImageIcon(getClass().getResource("/resources/" + piece.getName() + ".png")));
    }


    private boolean hasValidMoves(Color color) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Piece piece = board.getCell(i, j).getPiece();
                if (piece != null && piece.getColor() == color) {
                    for (int x = 0; x < boardSize; x++) {
                        for (int y = 0; y < boardSize; y++) {
                            if (isValidMove(i, j, x, y, piece)) {
                                // Simulate the move
                                Cell source = board.getCell(i, j);
                                Cell target = board.getCell(x, y);
                                Piece originalTargetPiece = target.getPiece();
                                target.setPiece(piece);
                                source.setPiece(null);

                                boolean isValid = !isKingInCheck(color);

                                // Revert the move
                                target.setPiece(originalTargetPiece);
                                source.setPiece(piece);

                                if (isValid) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game());
    }
}

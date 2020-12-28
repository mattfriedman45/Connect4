import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class C4GameBoard extends JPanel {

    private Connect4 c4; 
    private JLabel status;
    private int col;
    private int row;


    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 600;

    public C4GameBoard(JLabel statusInit, String playerOneName, String playerTwoName) {
        setBorder(BorderFactory.createLineBorder(Color.BLUE));
        setBackground(Color.WHITE);
        setFocusable(true);
        
        c4 = new Connect4(); // initializes model for the game

        c4.setPlayerOneName(playerOneName);
        c4.setPlayerTwoName(playerTwoName);
        status = statusInit; // initializes the status JLabel

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                col = (p.x / 100);
                row = c4.fall(col);
                if (row >= 0) {
                    c4.playTurn(col, row);
                }
                
                updateStatus(true); 
                repaint(); 
            }
        });
    }

    public void reset() {
        c4.reset();
        status.setText(c4.getPlayerOneName() + "'s Turn");
        repaint();
        
        requestFocusInWindow();
    }
    
    public void undo() {
        c4.undo();
        updateStatus(false); 
        repaint();
    }
    // updates status of player's turn/who won on bottom line of game
    private void updateStatus(boolean checkWinner) {
        if (c4.getCurrentPlayer()) {
            status.setText(c4.getPlayerOneName() + "'s Turn");
        } else {
            status.setText(c4.getPlayerTwoName() + "'s Turn");
        }
        
        if (checkWinner) {
            String hs1 = c4.newHighScore(1);
            String hs2 = c4.newHighScore(2);
            int winner = c4.checkWinner();
            if (winner == 1) {
                status.setText(c4.getPlayerOneName() + " wins!!! " + hs1);
            } else if (winner == 2) {
                status.setText(c4.getPlayerTwoName() + " wins!!! " + hs2);
            } else if (winner == 3) {
                status.setText("It's a tie.");
            }
        }
    }
    // paints board with grid lines and cycles through board to draw shape
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLUE);

        g.drawLine(100, 0, 100, 600);
        g.drawLine(200, 0, 200, 600);
        g.drawLine(300, 0, 300, 600);
        g.drawLine(400, 0, 400, 600);
        g.drawLine(500, 0, 500, 600);
        g.drawLine(600, 0, 600, 600);
        
        g.drawLine(0, 100, 700, 100);
        g.drawLine(0, 200, 700, 200);
        g.drawLine(0, 300, 700, 300);
        g.drawLine(0, 400, 700, 400);
        g.drawLine(0, 500, 700, 500);
        
        Pieces[][] pieceBoard = c4.getB2();
        
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 5; j++) {
                if (pieceBoard[j][i] != null) {
                    pieceBoard[j][i].drawShape(g, i, j);
                }
            }
        }   
    }

    
    @Override
public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
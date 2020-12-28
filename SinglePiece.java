import java.awt.Color;
import java.awt.Graphics;

public class SinglePiece implements Pieces  {
    
    private int player;
    
    public SinglePiece(int player) {
        this.player = player;
    }

    public void drawShape(Graphics g, int row, int col) {
        if (player == 1) {
            g.setColor(Color.RED);
        } else if (player == 2) {
            g.setColor(Color.BLACK);
        } else if (player == 0) {
            g.setColor(Color.WHITE);
        }
        g.drawOval(30 + 100 * row, 30 + 100 * col, 40, 40);
    }
    
    public int getPlayer() {
        return player;
    }

}

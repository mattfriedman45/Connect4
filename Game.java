
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game implements Runnable {
    
    private String directions = "How To Play Connect 4: \n"
            + "You and another player compete to get four in a row of your pieces. \nThis can be in"
            + " any direction (vertical, horizontal, diagonal). \nSelect a column to place your "
            + "piece in that column. Make sure to watch your opponent to block them from winning.\n"
            + "You want to get the lowest number of moves to win as possible, so go ahead and smoke"
            + " your opponent! \nEnjoy!";

    public void run() {
        final JFrame frame = new JFrame("Connect Four!");
        frame.setLocation(700, 600);
        
        JOptionPane.showMessageDialog(frame, directions, "Instructions", JOptionPane.PLAIN_MESSAGE);
        String playerOneName = JOptionPane.showInputDialog(frame, 
                "Please enter first player's name:", "Name 1", JOptionPane.PLAIN_MESSAGE);
        String playerTwoName = JOptionPane.showInputDialog(frame, 
                "Please enter second player's name:", "Name 2", JOptionPane.PLAIN_MESSAGE);
        

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        final C4GameBoard board = new C4GameBoard(status, playerOneName, playerTwoName);
        frame.add(board, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        //undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
               
            });
        
        //reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        
        control_panel.add(reset);
        control_panel.add(undo);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        board.reset();
      
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
        
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Connect4 {

    private Pieces[][] b2;
    private boolean player1;
    private String playerOneName;
    private String playerTwoName;
    private boolean gameOver;
    private int player1Turns;
    private int player2Turns;
    private Map <String, Integer> scores;
    private List<int []> undoPlaces = new ArrayList<int[]>();
    private boolean newHighScore;
    private boolean fileFound; 
    private String fileName;

    public Connect4() {
        reset();
    }
    
    public void undo() {
        if (undoPlaces.size() > 0) {
            int [] a = undoPlaces.get(undoPlaces.size() - 1);
            if (undoPlaces.size() >= 1) {
                undoPlaces.remove(undoPlaces.size() - 1);
                player1 = !player1;
            } 
            if (gameOver) {
                gameOver = !gameOver;
                player1 = !player1;
            }
            int c = a[0];
            int r = a[1];
            b2[r][c] = null;
            
            if (player1 && player1Turns > 0) {
                player1Turns--;
            } else if (!player1 && player2Turns > 0) {
                player2Turns--;
            }
        }
    }
    
    // takes in column that was clicked and finds the lowest empty box
    public int fall(int c) {
        int r = 5;
        while (r >= 0) {
            if (!(b2[r][c] == null)) {
                r--;
            } else {
                return r;
            }
        }
        return -1;
    }
    // calls the drawShape function with correct player, adds move to undoPlaces (collection)
    public boolean playTurn(int c, int r) {
        if (!(b2[r][c] == null) || gameOver) {
            return false;
        } else if (player1) {
            b2[r][c] = new SinglePiece(1);
            int [] coordinate = {c, r};
            undoPlaces.add(coordinate);
            player1Turns++;
        } else {
            b2[r][c] = new SinglePiece(2);
            int [] coordinate = {c, r};
            undoPlaces.add(coordinate);
            player2Turns++;
        }
        if (checkWinner() == 0) {
            player1 = !player1;
        }
        return true;
    }

    // checks who has one
    // 0 is inconclusive (no win)
    // 1 is player1 has won
    // 2 is player2 has won
    // 3 is tie
    
    public int checkWinner() {
        // draw
        if ((player1Turns + player2Turns) >= 42) {
            gameOver = true;
            return 3;
        // checking wins in all other direction
        } else if (horozontalWin() != 0) {
            return horozontalWin();
        } else if (verticalWin() != 0) {
            return verticalWin();
        } else if (checkDownwardDiagonalWin() != 0) {
            return checkDownwardDiagonalWin();
        } else if (checkUpwardDiagonalWin() != 0) {
            return checkUpwardDiagonalWin();
        }
        return 0;
    }
    
    // Runs through rows to find 4 in a row
    public int horozontalWin() {
        int tokens = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 1; c < 7; c++) {
                if ((b2[r][c] != null) && (b2[r][c - 1] != null)) {
                    if ((b2[r][c].getPlayer()) == b2[r][c - 1].getPlayer()) {
                        tokens++;
                        if (tokens >= 3) {
                            if ((b2[r][c].getPlayer() == 1)) {
                                recordHighScore(playerOneName, player1Turns);
                            } else {
                                recordHighScore(playerTwoName, player2Turns);
                            }
                            gameOver = true;
                            return (b2[r][c].getPlayer());
                        }
                    } else {
                        tokens = 0;
                    }
                } else {
                    tokens = 0;
                }
            }
       
        }
        return 0;
    }
        
    // runs through columns for 4 in a row
    public int verticalWin() {
        int tokens = 0;
        for (int c = 0; c < 7; c++) {
            for (int r = 1; r < 6; r++) {
                if ((b2[r][c] != null) && (b2[r - 1][c] != null)) {
                    if (b2[r][c].getPlayer() == b2[r - 1][c].getPlayer()) {
                        tokens++;
                        if (tokens >= 3) {
                            if ((b2[r][c].getPlayer() == 1)) {
                                recordHighScore(playerOneName, player1Turns);
                            } else {
                                recordHighScore(playerTwoName, player2Turns);
                            }
                            gameOver = true;
                            return (b2[r][c].getPlayer());
                        }
                    } else {
                        tokens = 0;
                    }
                } else {
                    tokens = 0;
                }
            }
        }
        return 0;
    }
    
    // runs through places where a diagonal might start (top left) and iterates 
    // right and down through the array for 4 in a row
    public int checkDownwardDiagonalWin() {
        int tokens = 0;
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 2; r++) {
                if ((b2[r][c] != null) && (b2[r + 1][c + 1] != null) && 
                        (b2[r + 2][c + 2] != null) && (b2[r + 3][c + 3] != null)) {
                    for (int i = 0; i <= 3; i++) {
                        if (b2[r][c].getPlayer() == b2[r + i][c + i].getPlayer()) {
                            tokens++;
                        } else {
                            tokens = 0;
                            break;
                        }
                    }
                    if (tokens >= 3) {
                        if ((b2[r][c].getPlayer() == 1)) {
                            recordHighScore(playerOneName, player1Turns);
                        } else {
                            recordHighScore(playerTwoName, player2Turns);
                        }
                        gameOver = true;
                        return (b2[r][c].getPlayer());
                    }
                } else {
                    tokens = 0;
                } 
            }
        }
        return 0;
    }
    
    // runs through places where a diagonal might start (bottom left) and iterates 
    // right and up through the array for 4 in a row
    public int checkUpwardDiagonalWin() {
        int tokens = 0;
        for (int c = 0; c <= 3; c++) {
            for (int r = 3; r <= 5; r++) {
                if ((b2[r][c] != null) && (b2[r - 1][c + 1] != null) && 
                        (b2[r - 2][c + 2] != null) && (b2[r - 3][c + 3] != null)) {
                    for (int i = 0; i <= 3; i++) {
                        if (b2[r][c].getPlayer() == b2[r - i][c + i].getPlayer()) {
                            tokens++;
                        } else {
                            tokens = 0;
                            break;
                        }
                    }
                    if (tokens >= 3) {
                        if ((b2[r][c].getPlayer() == 1)) {
                            recordHighScore(playerOneName, player1Turns);
                        } else {
                            recordHighScore(playerTwoName, player2Turns);
                        }
                        gameOver = true;
                        return (b2[r][c].getPlayer());
                    }
                } else {
                    tokens = 0;
                } 
            }
        }
        return 0;
    }
    
    // adds high scores to intermediary map in between reading and writing
    public boolean recordHighScore(String playerName, int score) {
        scores = getCurrentScores();
        if (scores != null) {
            if (scores.putIfAbsent(playerName, score) != null) {
                if (score < scores.get(playerName)) {
                    scores.remove(playerName);
                    scores.put(playerName, score);
                    newHighScore = true;
                }
            }
            File file = new File(fileName);
            FileWriter fw;
            try {
                fw = new FileWriter(file, false);
                BufferedWriter bw = new BufferedWriter(fw);
                for (Map.Entry<String, Integer> s : scores.entrySet()) { 
                    bw.write(s.getKey() + " " + s.getValue());
                    bw.newLine();
                }
                bw.flush();
                bw.close();
                return true;
            } catch (FileNotFoundException e) {
                fileFound = false;
                return false;
            } catch (IOException e) {
            
            }
        }
        return false;
    }
    
    // Reads the txt file and creates a map from that
    public Map<String, Integer> getCurrentScores() {
        Map<String, Integer> map = new TreeMap<String, Integer>();
        File file = new File(fileName);
        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String current = br.readLine();
            while (current != null) {
                String currentName = current.substring(0, current.lastIndexOf(" "));
                int currentScore = Integer.parseInt(current.substring(current.lastIndexOf(" ") 
                        + 1));
                map.put(currentName, currentScore);
                current = br.readLine();
            }
        } catch (FileNotFoundException e) {
            fileFound = false;
            return scores; 
            // this returns an older version of the file if it's lost (sort of like a backup.)
            // if there were no prior games to fill this map, it returns null
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    // returns new score to currentStatus to print only if the score is better than one on record
    public String newHighScore(int winner) {
        if (!fileFound) {
            return "File not found with high scores, oops!";
        } else if (newHighScore) {
            if (winner == 1) {
                return "NEW BEST SCORE: " + player1Turns;
            } else if (winner == 2) {
                return "NEW BEST SCORE: " + player2Turns;
            }
        }
        return "";
    }
    
    public void reset() {
        b2 = new Pieces[6][7];
        player1Turns = 0;
        player2Turns = 0;
        player1 = true;
        gameOver = false;
        newHighScore = false;
        fileFound = true;
        fileName = "bestscores.txt";
    }
    
    public boolean getCurrentPlayer() {
        return player1;
    }
    
    public void setPlayerOneName(String n) {
        playerOneName = n;
    }
    public void setPlayerTwoName(String n) {
        playerTwoName = n;
    }
    
    public String getPlayerOneName() {
        return playerOneName;
    }
    
    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public Pieces[][] getB2() {
        return b2;
    }
    
    public void setFileName(String name) {
        name = fileName;
    }
    
    public static void main(String[] args) {

    }
}
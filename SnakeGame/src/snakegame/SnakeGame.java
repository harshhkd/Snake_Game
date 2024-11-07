
package snakegame;

import javax.swing.*;

public class SnakeGame extends JFrame {
    private int difficultyLevel;

    SnakeGame(int difficultyLevel) {
        super("Snake Game");
        this.difficultyLevel = difficultyLevel;

        add(new Board(difficultyLevel));
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        String[] options = {"Easy", "Medium", "Hard"};
        int difficulty = JOptionPane.showOptionDialog(null, "Select Difficulty Level",
                "Difficulty Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        new SnakeGame(difficulty).setVisible(true);
    }
}
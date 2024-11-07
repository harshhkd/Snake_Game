
package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    private Image apple;
    private Image dot;
    private Image head;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RANDOM_POSITION = 29;
    private int dots;
    private int apple_x;
    private int apple_y;
    private int score; // Track the score
    private boolean inGame = true;
    private Timer timer;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    Board(int difficultyLevel) {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(300, 300));
        loadImages();
        initGame(difficultyLevel);
    }

    private void loadImages() {
        try {
            apple = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png")).getImage();
            dot = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png")).getImage();
            head = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initGame(int difficultyLevel) {
        dots = 3;
        score = 0; // Initialize score to 0
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }
        locateApple();
        int delay = 140 - difficultyLevel * 30;  // Set speed based on difficulty
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            score++; // Increase score by 1 for each apple eaten
            locateApple();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) x[0] -= DOT_SIZE;
        if (rightDirection) x[0] += DOT_SIZE;
        if (upDirection) y[0] -= DOT_SIZE;
        if (downDirection) y[0] += DOT_SIZE;
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) inGame = false;
        }
        if (y[0] >= 300 || x[0] >= 300 || x[0] < 0 || y[0] < 0) inGame = false;
        if (!inGame) timer.stop();
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over!";
        String scoreMsg = "Apples Eaten: " + score;
        
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        g.setColor(Color.WHITE);
        g.setFont(font);
        
        FontMetrics metrics = getFontMetrics(font);
        g.drawString(msg, (300 - metrics.stringWidth(msg)) / 2, 150);
        g.drawString(scoreMsg, (300 - metrics.stringWidth(scoreMsg)) / 2, 170);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}

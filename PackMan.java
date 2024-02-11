package com.example.packman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PacmanGame extends JPanel implements KeyListener {
    private int pacmanX = 50;
    private int pacmanY = 50;
    private int pacmanRadius = 20;
    private int dx = 0; // Pacman's movement in x-direction
    private int dy = 0; // Pacman's movement in y-direction

    public PacmanGame() {
        JFrame frame = new JFrame("Pacman Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().add(this);
        frame.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        // Start the game loop
        gameLoop();
    }

    private void gameLoop() {
        while (true) {
            // Update game state
            updateGameState();

            // Repaint the screen
            repaint();

            // Add a short delay to slow down the game
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGameState() {
        // Move Pacman
        pacmanX += dx;
        pacmanY += dy;

        // Keep Pacman within bounds
        if (pacmanX < 0) pacmanX = 0;
        if (pacmanY < 0) pacmanY = 0;
        if (pacmanX > getWidth()) pacmanX = getWidth();
        if (pacmanY > getHeight()) pacmanY = getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPacman(g);
    }

    private void drawPacman(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillArc(pacmanX - pacmanRadius, pacmanY - pacmanRadius,
                2 * pacmanRadius, 2 * pacmanRadius, 45, 270);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // Set Pacman's movement direction based on the key press
        switch (keyCode) {
            case KeyEvent.VK_W:
                dy = -5;
                dx = 0;
                break;
            case KeyEvent.VK_S:
                dy = 5;
                dx = 0;
                break;
            case KeyEvent.VK_A:
                dx = -5;
                dy = 0;
                break;
            case KeyEvent.VK_D:
                dx = 5;
                dy = 0;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this example
    }

    public static void main(String[] args) {
        new PacmanGame();
    }
}

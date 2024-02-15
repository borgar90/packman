package com.example.packmangame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PacmanGame extends JPanel implements KeyListener {
    private int pacX = 300, pacY = 300;
    private int pacDirection = 0; // 0: right, 1: left, 2: up, 3: down
    private PacmanMaze pacmanMaze;

    public PacmanGame() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(600, 600));
        setFocusable(true);
        addKeyListener(this);
        pacmanMaze = new PacmanMaze();
        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movePacman();
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        pacmanMaze.displayMaze(g);

        g.setColor(Color.YELLOW);
        switch (pacDirection) {
            case 0: // Right
                g.fillArc(pacX, pacY, 30, 30, 45, 270);
                break;
            case 1: // Left
                g.fillArc(pacX, pacY, 30, 30, 225, 270);
                break;
            case 2: // Up
                g.fillArc(pacX, pacY, 30, 30, 135, 270);
                break;
            case 3: // Down
                g.fillArc(pacX, pacY, 30, 30, -45, 270);
                break;
        }
    }


    private void movePacman() {
        int nextX = pacX, nextY = pacY;
        switch (pacDirection) {
            case 0:
                nextX += 5;
                break;
            case 1:
                nextX -= 5;
                break;
            case 2:
                nextY -= 5;
                break;
            case 3:
                nextY += 5;
                break;
        }
        if (nextX >= 0 && nextX < getWidth() - 30 && nextY >= 0 && nextY < getHeight() - 30 &&
                !pacmanMaze.isWall(nextY / 30, nextX / 30)) {
            pacX = nextX;
            pacY = nextY;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT) {
            pacDirection = 0;
        } else if (key == KeyEvent.VK_LEFT) {
            pacDirection = 1;
        } else if (key == KeyEvent.VK_UP) {
            pacDirection = 2;
        } else if (key == KeyEvent.VK_DOWN) {
            pacDirection = 3;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pacman Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new PacmanGame());
        frame.pack();
        frame.setVisible(true);
    }
}

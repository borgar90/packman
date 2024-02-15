package com.example.packmangame;

import java.awt.*;

public class PacmanMaze {
    private int rows, cols;
    private boolean[][] walls;

    public PacmanMaze() {
        rows = 15;
        cols = 19;
        walls = new boolean[][] {
                {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
                {true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true},
                {true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true},
                {true, false, true, false, true, true, true, true, true, true, true, true, true, true, true, false, true, false, true},
                {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true, false, true, false, true},
                {true, false, true, false, true, false, true, true, true, true, true, true, true, false, true, false, true, false, true},
                {true, false, true, false, true, false, true, false, false, false, false, false, true, false, true, false, true, false, true},
                {true, false, true, false, true, false, true, true, true, true, true, false, true, false, true, false, true, false, true},
                {true, false, true, false, true, false, false, false, false, true, true, false, true, false, true, false, true, false, true},
                {true, false, true, false, true, true, true, true, false, false, false, false, true, false, true, false, true, false, true},
                {true, false, false, false, false, false, false, true, true, true, true, true, true, false, true, false, true, false, true},
                {true, true, true, true, true, true, false, false, false, false, false, false, false, false, true, false, true, false, true},
                {true, false, false, false, false, false, false, true, true, true, true, true, true, true, true, false, true, false, true},
                {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
        };
    }

    public void displayMaze(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (walls[i][j]) {
                    g.fillRect(j * 30, i * 30, 30, 30);
                }
            }
        }
    }

    public boolean isWall(int row, int col) {
        return walls[row][col];
    }
}

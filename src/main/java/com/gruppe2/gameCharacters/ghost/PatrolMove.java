package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.map.GameBoard;
import com.gruppe2.utils.Node;
import com.gruppe2.gameCharacters.pacman.PacMan;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @usage begynelsen på en patrulje bevegelse for spøkelser
 * ikke ferdig implementert
 */

public class PatrolMove implements Move{
    private Ghost ghost;
    private PacMan pacMan;
    private GameBoard gameBoard;
    private List<Node> patrolPath;
    private int currentPathIndex = 0;
    private final double detectionRange = 5.0; // Example detection range

    public PatrolMove(Ghost ghost, PacMan pacMan, GameBoard gameBoard, List<Node> patrolPath) {
        this.ghost = ghost;
        this.pacMan = pacMan;
        this.gameBoard = gameBoard;
        this.patrolPath = new ArrayList<>(patrolPath);
    }

    @Override
    public void move() {
        if (isPacManInRange()) {
            Node target = new Node((int) pacMan.getX(), (int) pacMan.getY());
            ghost.move(target.getX(), target.getY());
        } else {
            Node nextStep = patrolPath.get(currentPathIndex);
            ghost.move(nextStep.getX(), nextStep.getY());
            currentPathIndex = (currentPathIndex + 1) % patrolPath.size();
        }
    }

    private boolean isPacManInRange() {
        double distance = Math.sqrt(Math.pow(pacMan.getX() - ghost.getX(), 2) + Math.pow(pacMan.getY() - ghost.getY(), 2));
        return distance <= detectionRange;
    }


}

package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.PacMan;

import java.util.List;

public class GhostAI {
    private double wanderTargetX = -1;
    private double wanderTargetY = -1;

    public void ghostChase(){
        int x = 5;
        int y = 5;
    }

    public void ghostMove( List<Ghost> ghosts ){
        for(Ghost ghost : ghosts){
            ghost.chase();
        }
    }
    public boolean isInLineOfSight( Ghost ghost, PacMan pacMan, GameBoard gameBoard ) {
        if (ghost.getX() == pacMan.getX()) {
            for (int y = Math.min((int)ghost.getY(), (int)pacMan.getY()); y <= Math.max(ghost.getY(), pacMan.getY()); y++) {
                if (gameBoard.hasWall(ghost.getX(), y)) return false;
            }
            return true;
        } else if (ghost.getY() == pacMan.getY()) {
            for (int x = Math.min((int)ghost.getX(), (int)pacMan.getX()); x <= Math.max(ghost.getX(), pacMan.getX()); x++) {
                if (gameBoard.hasWall(x, ghost.getY())) return false;
            }
            return true;
        }
        return false;
    }

    public void moveToPacManDirectly( double x, double y, Ghost ghost, GameBoard gameBoard ) {
        double pacManX = x;
        double pacManY = y;
        double ghostX = ghost.getX();
        double ghostY = ghost.getY();
        double deltaX = pacManX - ghost.getX();
        double deltaY = pacManY - ghost.getY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        System.out.println("MOVING TO PAC");
        if (distance > 0) { // Avoid division by zero

            double moveX = (deltaX / distance) * ghost.getSpeed();
            double moveY = (deltaX / distance) * ghost.getSpeed();

            // Attempt diagonal movement only if both directions are clear
            if (!gameBoard.ghostWillCollide(ghost.getX() + moveX, ghost.getY() + moveY, ghost)) {
                ghostX += moveX;
                ghostY += moveY;
            } else {
                // Try horizontal movement if direct path is blocked
                if (!gameBoard.ghostWillCollide(ghostX + moveX, ghostY, ghost)) {
                    ghostX += moveX;
                } else if (!gameBoard.ghostWillCollide(ghostX, ghostY + moveY, ghost)) {
                    // Try vertical movement if horizontal is blocked
                    ghostY += moveY;
                } else {
                    // If both horizontal and vertical movements are blocked,
                    // it might be at a corner or close to Pac-Man but blocked by walls
                    handleBeingStuck(gameBoard, ghost);
                }
            }
            ghost.move(ghostX , ghostY);
        }
    }
    public void handleBeingStuck(GameBoard gameBoard, Ghost ghost) {
        // Simple strategy: turn around by inverting direction
        double newTargetX = ghost.getX() - (wanderTargetX - ghost.getX());
        double newTargetY = ghost.getY() - (wanderTargetY - ghost.getY());

        wanderTargetX = Math.max(0, Math.min(newTargetX, gameBoard.getWidth() - ghost.ghostShape.getFitWidth() * 2));
        wanderTargetY = Math.max(0, Math.min(newTargetY, gameBoard.getHeight() - ghost.ghostShape.getFitHeight() * 2));
    }
}

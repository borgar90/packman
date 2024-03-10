package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.Node;
import com.gruppe2.packman.PacMan;

import java.util.ArrayList;
import java.util.List;

public class Flee {
    private Ghost ghost;
    private GameBoard gb;
    public Flee( Ghost ghost, GameBoard gb ){
        this.ghost = ghost;
        this.gb = gb;
    }
    public void fleeFromPacman(){
        flee(ghost);
    }

    private void flee(Ghost ghost){
        ghost.setSpeed(2);
        Node currentNode = new Node((int)ghost.getX(), (int)ghost.getY());
        List<Node> neighbors = getNeighbors(currentNode, ghost);
        PacMan pacMan = gb.getPacMan();
        System.out.println(neighbors.size());
        Node furthestNeighbor = null;
        double maxDistance = -1;
        for (Node neighbor : neighbors) {
            double distance = Math.sqrt(Math.pow(pacMan.getX() - neighbor.getX(), 2) + Math.pow(pacMan.getY() - neighbor.getY(), 2));
            if (distance > maxDistance) {
                maxDistance = distance;
                furthestNeighbor = neighbor;
            }
        }

        if (furthestNeighbor != null) {
            ghost.move(furthestNeighbor.getX(), furthestNeighbor.getY());
        }
    }
    private boolean isAlignedHorizontally(double ghostX, double pacManX, double radius) {
        return Math.abs(ghostX - pacManX) <= radius;
    }

    private boolean isAlignedVertically(double ghostY, double pacManY, double radius) {
        return Math.abs(ghostY - pacManY) <= radius;
    }

    private boolean isClearPathVertical(int x, int startY, int endY, GameBoard gameBoard) {
        for (int y = startY; y <= endY; y++) {
            if (gameBoard.hasWall(x, y)) return false;
        }
        return true;
    }


    private int[] lineOfSightDirection(Ghost ghost) {
        PacMan pacMan = gb.getPacMan();

        // Horizontal line of sight
        if (isAlignedHorizontally(ghost.getX(), pacMan.getX(), pacMan.getRadius())) {
            if (isClearPathVertical((int)ghost.getX(), (int)ghost.getY(), (int)pacMan.getY(), gb)) {
                return new int[]{0, pacMan.getY() > ghost.getY() ? -1 : 1}; // Move vertically towards Pac-Man
            }
        }

        // Vertical line of sight
        else if (isAlignedVertically(ghost.getY(), pacMan.getY(), pacMan.getRadius())) {
            if (isClearPathHorizontal((int)ghost.getY(), (int)ghost.getX(), (int)pacMan.getX(), gb)) {
                return new int[]{pacMan.getX() > ghost.getX() ? -1 : 1, 0}; // Move horizontally towards Pac-Man
            }
        }
        return null; // Not in direct line of sight
    }


    private boolean isClearPathHorizontal(int y, int startX, int endX, GameBoard gameBoard) {
        for (int x = startX; x <= endX; x++) {
            if (gameBoard.hasWall(x, y)) return false;
        }
        return true;
    }


    private List<Node> getNeighbors( Node node, Ghost ghost) {
        List<Node> neighbors = new ArrayList<>();
        int[] directLineOfSight = lineOfSightDirection(ghost);

        if (directLineOfSight != null) {
            int newX = node.getX() + directLineOfSight[0];
            int newY = node.getY() + directLineOfSight[1];
            // Check if the new position is valid and there are no walls directly in the path.
            if (isValidPosition(newX, newY) && !gb.hasWall(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }

        } else {
            int[][] offsets = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] offset : offsets) {
                int newX = node.getX() + offset[0];
                int newY = node.getY() + offset[1];

                // Adjust the collision check based on movement direction
                boolean wallCollision = switch (offset[0] + "," + offset[1]) {
                    case "0,1" -> gb.hasWall(newX, newY + ghost.getShape().getFitHeight()); // Down
                    case "0,-1" -> gb.hasWall(newX, newY); // Up
                    case "-1,0" -> gb.hasWall(newX, newY); // Left
                    case "1,0" -> gb.hasWall(newX + ghost.getShape().getFitWidth(), newY); // Right
                    default -> false;
                };


                if (isValidPosition(newX, newY) && !wallCollision) {
                    neighbors.add(new Node(newX, newY));
                }

            }
        }
        return neighbors;
    }


    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < gb.getWidth() && y >= 0 && y < gb.getHeight();
    }



}

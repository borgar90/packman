package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.Node;
import com.gruppe2.packman.PacMan;


import java.util.*;

public class MoveHoming implements Move{
    private Ghost ghost;
    private PacMan pacMan;
    private GameBoard gameBoard;

    public MoveHoming( Ghost ghost, PacMan pacMan, GameBoard gameBoard) {
        this.ghost = ghost;
        this.pacMan = pacMan;
        this.gameBoard = gameBoard;
    }

    @Override
    public void move() {
        List<Node> path = findPath();


        if (!path.isEmpty() && path.size() > 1) { // Ensure there's a next step
            Node nextStep = path.get(1);

            ghost.move(nextStep.getX(), nextStep.getY());
        } else {
            System.out.println("No path or no next step available."); // Debug output
        }
    }

    private List<Node> findPath() {
        // Initialize open and closed sets
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Set<Node> closedSet = new HashSet<>();

        System.out.println(ghost.getX() + "  " + ghost.getY() + " " + pacMan.getX() + " " + pacMan.getY());

        Node startNode = new Node((int) ghost.getShape().getX(), (int) ghost.getShape().getY());
        Node targetNode = new Node((int) pacMan.getX(), (int) pacMan.getY());

        startNode.setG(0);
        startNode.setH(estimateDistance(startNode, targetNode));
        startNode.setF(startNode.getG() + startNode.getH());

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            System.out.println("Current: " + currentNode.getX() + ", " + currentNode.getY()); // Debug

            if (currentNode.equals(targetNode)) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode);

            for (Node neighbor : getNeighbors(currentNode)) {
                if (closedSet.contains(neighbor) || gameBoard.hasWall(neighbor.getX(), neighbor.getY())) continue;
                double tentativeG = currentNode.getG() + estimateDistance(currentNode, neighbor);
                if (!openSet.contains(neighbor) || tentativeG < neighbor.getG()) {
                    neighbor.setParent(currentNode);
                    neighbor.setG(tentativeG);
                    neighbor.setH(estimateDistance(neighbor, targetNode));
                    neighbor.setF(neighbor.getG() + neighbor.getH());
                    if (!openSet.contains(neighbor)) openSet.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // Return an empty path if there's no path to Pac-Man
    }

    private double estimateDistance(Node a, Node b) {
        // Use Manhattan distance as heuristic for a grid
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private List<Node> reconstructPath(Node targetNode) {
        List<Node> path = new ArrayList<>();
        for (Node node = targetNode; node != null; node = node.getParent()) {
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }




    // Helper method to get neighbors of a node (adjacent cells)
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        // Example for directly adjacent (up, down, left, right)
        int[][] offsets = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        for (int[] offset : offsets) {
            int newX = node.getX() + offset[0];
            int newY = node.getY() + offset[1];

            // Adjust the collision check based on movement direction
            boolean wallCollision = switch (offset[0] + "," + offset[1]) {
                case "0,1" -> gameBoard.hasWall(newX, newY + ghost.getShape().getFitHeight()); // Down
                case "0,-1" -> gameBoard.hasWall(newX, newY); // Up
                case "-1,0" -> gameBoard.hasWall(newX, newY); // Left
                case "1,0" -> gameBoard.hasWall(newX + ghost.getShape().getFitWidth(), newY); // Right
                default -> false;
            };


            if (isValidPosition(newX, newY) && !wallCollision) {
                neighbors.add(new Node(newX, newY));
            }

        }
        return neighbors;
    }

    private boolean isValidPosition(int x, int y) {
        // Check if (x, y) is within the game board's bounds
        return x >= 0 && x < gameBoard.getWidth() && y >= 0 && y < gameBoard.getHeight();
    }




}

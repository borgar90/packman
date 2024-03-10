package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.Node;
import com.gruppe2.packman.PacMan;


import java.util.*;

public class MoveHoming implements Move{
    private Ghost ghost;
    private PacMan pacMan;
    private GameBoard gameBoard;
    private GhostAI ghostAI;

    private Node currentPath;

    public MoveHoming( Ghost ghost, PacMan pacMan, GameBoard gameBoard) {
        this.ghost = ghost;
        this.pacMan = pacMan;
        this.gameBoard = gameBoard;
        this.ghostAI = new GhostAI();
    }

    @Override
    public void move() {
        homing();
    }

    private void homing(){
        List<Node> path = findPath();
        if (!path.isEmpty() && path.size() > 1) { // Ensure there's a next step
            Node nextStep = path.get(1);
            currentPath = nextStep;
            double currentX = ghost.getX();
            double currentY = ghost.getY();
            double targetX = nextStep.getX();
            double targetY = nextStep.getY();
            double deltaX = targetX - currentX;
            double moveX = currentX + (deltaX > 0 ? ghost.getSpeed() : (deltaX < 0 ? - ghost.getSpeed() : 0));
            double deltaY = targetY - currentY;
            double moveY = currentY + (deltaY > 0 ? ghost.getSpeed() : (deltaY < 0 ? - ghost.getSpeed() : 0));
            ghost.move(targetX,  targetY);

        } else {
           switch (currentPath.getX() + "," + currentPath.getY()) {
                case "0,1" -> ghost.move(0, -1); // Down
                case "0,-1" -> ghost.move(0, 1); // Up
                case "-1,0" -> ghost.move(1, 0); // Left
                case "1,0" -> ghost.move(-1, 0); // Right
                default -> {
                   return;
               }
            };

        }
    }

    private List<Node> findPath() {
        // Initialize open and closed sets
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node((int) ghost.getShape().getX(), (int) ghost.getShape().getY());
        Node targetNode = new Node((int) pacMan.getX(), (int) pacMan.getY());

        startNode.setG(0);
        startNode.setH(estimateDistance(startNode, targetNode));
        startNode.setF(startNode.getG() + startNode.getH());
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

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

    private int[] lineOfSightDirection() {
        // Horizontal line of sight
        if (isAlignedHorizontally(ghost.getX(), pacMan.getX(), pacMan.getRadius())) {
            if (isClearPathVertical((int)ghost.getX(), (int)ghost.getY(), (int)pacMan.getY(), gameBoard)) {
                return new int[]{0, pacMan.getY() > ghost.getY() ? 1 : -1}; // Move vertically towards Pac-Man
            }
        }
        // Vertical line of sight
        else if (isAlignedVertically(ghost.getY(), pacMan.getY(), pacMan.getRadius())) {
            if (isClearPathHorizontal((int)ghost.getY(), (int)ghost.getX(), (int)pacMan.getX(), gameBoard)) {
                return new int[]{pacMan.getX() > ghost.getX() ? 1 : -1, 0}; // Move horizontally towards Pac-Man
            }
        }
        return null; // Not in direct line of sight
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

    private boolean isClearPathHorizontal(int y, int startX, int endX, GameBoard gameBoard) {
        for (int x = startX; x <= endX; x++) {
            if (gameBoard.hasWall(x, y)) return false;
        }
        return true;
    }


    private double estimateDistance(Node a, Node b) {
        double dx = Math.abs(a.getX() - b.getX());
        double dy = Math.abs(a.getY() - b.getY());

        // Optionally add a small bias for direct paths
        double directionBias = 0.95; // Less than 1 to slightly favor direct paths
        if (dx > dy) {
            return dx + dy * directionBias;
        } else {
            return dx * directionBias + dy;
        }
    }


    private List<Node> reconstructPath(Node targetNode) {
        List<Node> path = new ArrayList<>();
        for (Node node = targetNode; node != null; node = node.getParent()) {
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }

    private int[][] getPreferredOrderBasedOnDirection(Node ghostNode, Node targetNode) {
        int dx = targetNode.getX() - ghostNode.getX();
        int dy = targetNode.getY() - ghostNode.getY();
        boolean isHorizontalPriority = Math.abs(dx) > Math.abs(dy);

        // Define directions: Up, Right, Down, Left
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        int[][] preferredOrder = new int[4][2];

        if (isHorizontalPriority) {
            // If horizontal distance is greater, prioritize Right or Left first
            preferredOrder[0] = dx > 0 ? directions[1] : directions[3]; // Right or Left
            preferredOrder[1] = dy > 0 ? directions[2] : directions[0]; // Down or Up
        } else {
            // If vertical distance is greater, prioritize Down or Up first
            preferredOrder[0] = dy > 0 ? directions[2] : directions[0]; // Down or Up
            preferredOrder[1] = dx > 0 ? directions[1] : directions[3]; // Right or Left
        }
        // Fill in the remaining directions
        preferredOrder[2] = isHorizontalPriority ? (dy > 0 ? directions[0] : directions[2]) : (dx > 0 ? directions[3] : directions[1]);
        preferredOrder[3] = isHorizontalPriority ? (dx > 0 ? directions[3] : directions[1]) : (dy > 0 ? directions[0] : directions[2]);

        return preferredOrder;
    }


    // Helper method to get neighbors of a node (adjacent cells)
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[] directLineOfSight = lineOfSightDirection();

        if (directLineOfSight != null) {
            int newX = node.getX() + directLineOfSight[0];
            int newY = node.getY() + directLineOfSight[1];
            // Check if the new position is valid and there are no walls directly in the path.
            if (isValidPosition(newX, newY) && !gameBoard.hasWall(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }

        } else {
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
        }
        return neighbors;
    }


    private void addNeighborIfValid(Node node, int dx, int dy, List<Node> neighbors) {
        int newX = node.getX() + dx;
        int newY = node.getY() + dy;


        if (isValidPosition(newX, newY) && !gameBoard.hasWall(newX, newY)) {
            neighbors.add(new Node(newX, newY));
        }
    }


    private boolean isInLineOfSight() {
        // Check horizontal line of sight
        if (ghost.getX() == pacMan.getX()) {
            int minY = Math.min((int)ghost.getY(), (int)pacMan.getY());
            int maxY = Math.max((int)ghost.getY(), (int)pacMan.getY());
            for (int y = minY; y <= maxY; y++) {
                if (gameBoard.hasWall(ghost.getX(), y)) {
                    return false; // Wall is blocking the view
                }
            }
            return true; // No wall in between
        }
        // Check vertical line of sight
        else if (ghost.getY() == pacMan.getY()) {
            int minX = Math.min((int)ghost.getX(), (int)pacMan.getX());
            int maxX = Math.max((int)ghost.getX(), (int)pacMan.getX());
            for (int x = minX; x <= maxX; x++) {
                if (gameBoard.hasWall(x, ghost.getY())) {
                    return false; // Wall is blocking the view
                }
            }
            return true; // No wall in between
        }
        return false; // Not in direct line of sight
    }



    private boolean isValidPosition(int x, int y) {
        // Check if (x, y) is within the game board's bounds
        return x >= 0 && x < gameBoard.getWidth() && y >= 0 && y < gameBoard.getHeight();
    }




}

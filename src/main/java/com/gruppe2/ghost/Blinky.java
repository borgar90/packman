package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.GameManager;
import com.gruppe2.packman.PacMan;
import javafx.scene.image.Image;
import java.util.Random;

public class Blinky extends Ghost {
    private PacMan pacMan;
    private double wanderTargetX = -1;
    private double wanderTargetY = -1;
    private Random rand = new Random();
    private MoveAgressive chaseAgressive;

    public Blinky( double x, double y, Image ghost, PacMan pacMan, GameBoard gameBoard, GameManager gm ) {
        super(x, y, ghost, gameBoard, 3, gm);
        this.pacMan = pacMan;
        this.chaseAgressive = new MoveAgressive(this, pacMan, gameBoard);
    }


    @Override
    public void chase() {
        if(lineOfSightToPacMan()){
            moveToPacManDirectly();
        }else {
            chaseAgressive.move();
        }
    }

    @Override
    public void stopFleeing() {
        chaseAgressive.move();
    }

    private boolean lineOfSightToPacMan() {
        double pacmanX = pacMan.getShape().getCenterX() - pacMan.getRadius();
        double pacmanY = pacMan.getShape().getCenterY() - pacMan.getRadius();

        double tolerance = 5; // Adjust this tolerance as needed based on the movement increment

        // Check if Pac-Man is aligned horizontally or vertically within the tolerance range
        if (Math.abs(this.x - pacmanX) <= tolerance || Math.abs(this.y - pacmanY) <= tolerance) {

            if (Math.abs(this.x - pacmanX) <= tolerance) {
                int minY = (int) Math.min(this.y, pacmanY);
                int maxY = (int) Math.max(this.y, pacmanY);
                int direction = (int) Math.signum(maxY - minY);

                for (int checkY = minY; direction > 0 ? checkY <= maxY : checkY >= minY; checkY += direction) {
                    if (gameBoard.hasWall(this.x, checkY)) {
                        System.out.println("WALL");
                        return false; // Wall found between the ghost and Pac-Man, LOS blocked
                    }
                }
                return true; // No wall found, LOS clear

            }

            if (Math.abs(this.y - pacmanY) <= tolerance) {

                int minX = (int) Math.min(this.x, pacmanX);
                int maxX = (int) Math.max(this.x, pacmanX);
                int direction = (int) Math.signum(maxX - minX);

                for (int checkY = minX; direction > 0 ? checkY <= maxX : checkY >= minX; checkY += direction) {
                    if (checkY != this.x && gameBoard.hasWall(checkY, this.y)) {
                        return false;
                    }
                }
                return true; // No wall found, LOS clear
            }

        } else {
            // Pac-Man is not aligned either horizontally or vertically within the tolerance range
            // Check for diagonal line of sight

            // Calculate the difference in coordinates between ghost and Pac-Man
            double deltaX = pacmanX - this.x;
            double deltaY = pacmanY - this.y;

            // Calculate the absolute values of deltaX and deltaY
            double absDeltaX = Math.abs(deltaX);
            double absDeltaY = Math.abs(deltaY);

            // Check if the absolute difference in coordinates is equal
            // This indicates that there is a diagonal line of sight
            if (absDeltaX == absDeltaY) {
                // Diagonal line of sight, check for walls along the diagonal path
                int steps = (int) Math.max(absDeltaX, absDeltaY);
                double stepX = deltaX / steps;
                double stepY = deltaY / steps;
                double checkX = this.x;
                double checkY = this.y;
                for (int i = 0; i <= steps; i++) {
                    if (gameBoard.hasWall((int) checkX, (int) checkY)) {
                        return false; // Wall found, LOS blocked
                    }
                    checkX += stepX;
                    checkY += stepY;
                }
                return true; // No wall found, LOS clear
            }
        }

        return false; // No line of sight
    }

    private void moveToPacManDirectly() {
        double pacManX = pacMan.getX();
        double pacManY = pacMan.getY();
        double deltaX = pacManX - this.x;
        double deltaY = pacManY - this.y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        System.out.println("MOVING TO PAC");
        if (distance > 0) { // Avoid division by zero

            double moveX = (deltaX / distance) * getSpeed();
            double moveY = (deltaX / distance) * getSpeed();

            // Attempt diagonal movement only if both directions are clear
            if (!gameBoard.ghostWillCollide(this.x + moveX, this.y + moveY, this)) {
                this.x += moveX;
                this.y += moveY;
            } else {
                // Try horizontal movement if direct path is blocked
                if (!gameBoard.ghostWillCollide(this.x + moveX, this.y, this)) {
                    this.x += moveX;
                } else if (!gameBoard.ghostWillCollide(this.x, this.y + moveY, this)) {
                    // Try vertical movement if horizontal is blocked
                    this.y += moveY;
                } else {
                    // If both horizontal and vertical movements are blocked,
                    // it might be at a corner or close to Pac-Man but blocked by walls
                    handleBeingStuck();
                }
            }
            move(this.x , this.y);
        }
    }


    private void followPathOrWander() {
        int tileSize = (int) gameBoard.getTilesize();
        int width = (int) gameBoard.getWidth();
        int height = (int) gameBoard.getHeight(); // Use height for Y-coordinate calculations

        // Choose a new target if necessary
        chooseNewTargetIfNeeded(width, height);

        // Calculate movement towards the target
        double deltaX = wanderTargetX - this.x;
        double deltaY = wanderTargetY - this.y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            double moveX = (deltaX / distance) * getSpeed();
            double moveY = (deltaY / distance) * getSpeed();

            // Attempt to move in the calculated direction
            attemptMove(moveX, moveY);
        }
    }
    private void chooseNewTargetIfNeeded(int width, int height) {
        int tileSize = (int) gameBoard.getTilesize();

        if (wanderTargetX == -1 || (Math.abs(this.x - wanderTargetX) < getSpeed() && Math.abs(this.y - wanderTargetY) < getSpeed()) || gameBoard.willCollide(wanderTargetX, wanderTargetY)) {
            wanderTargetX = rand.nextInt(width / tileSize) * tileSize + tileSize / 2.0;
            wanderTargetY = rand.nextInt(height / tileSize) * tileSize + tileSize / 2.0;
        }
    }

    private void attemptMove(double moveX, double moveY) {
        double nextX = this.x + moveX;
        double nextY = this.y + moveY;

        boolean canMoveX = !gameBoard.willCollide(nextX + (moveX > 0 ? width : -width), this.y);
        boolean canMoveY = !gameBoard.willCollide(this.x, nextY + (moveY > 0 ? height : -height));

        // Prioritize movement based on which axis has a greater delta, or implement other logic
        if (Math.abs(moveX) > Math.abs(moveY)) {
            if (canMoveX) {
                // Move horizontally if possible
                this.x = nextX;
            } else if (canMoveY) {
                // Fallback to moving vertically if horizontal movement isn't possible
                this.y = nextY;
            } else {
                // Stuck, choose a new direction
                handleBeingStuck();
            }
        } else {
            if (canMoveY) {
                // Move vertically if possible
                this.y = nextY;
            } else if (canMoveX) {
                // Fallback to moving horizontally if vertical movement isn't possible
                this.x = nextX;
            } else {
                // Stuck, choose a new direction
                handleBeingStuck();
            }
        }

        move(this.x - ghostShape.getX(), this.y - ghostShape.getY());
    }

    private void handleBeingStuck() {
        // Simple strategy: turn around by inverting direction
        double newTargetX = this.x - (wanderTargetX - this.x);
        double newTargetY = this.y - (wanderTargetY - this.y);

        // Ensure the new target is within bounds
        wanderTargetX = Math.max(0, Math.min(newTargetX, gameBoard.getWidth() - width * 2));
        wanderTargetY = Math.max(0, Math.min(newTargetY, gameBoard.getHeight() - height * 2));
    }





}

package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.PacMan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoveAgressive implements Move {
    private Ghost ghost;
    private PacMan pacMan;
    private GameBoard gameBoard;
    private Direction currentDirection;
    private Random random = new Random();
    public MoveAgressive( Ghost ghost, PacMan pacMan, GameBoard gameBoard) {
        this.ghost = ghost;
        this.pacMan = pacMan;
        this.gameBoard = gameBoard;
        chooseInitialDirection();
    }

    private void chooseInitialDirection() {
        currentDirection = Direction.RIGHT;
    }

    @Override
    public void move() {
        // Check for openings in three possible directions from where Pac-Man is

        if (canMoveInDirection(currentDirection)) {
            List<Direction> optionalDirection = getPossibleDirections();
            if(!optionalDirection.isEmpty()){
                Direction chosenDirection = chooseBestDirection(optionalDirection);
                moveInDirection(chosenDirection);
            }else {
                moveInDirection(currentDirection);
            }
        } else{
            List<Direction> possibleDirections = getPossibleDirections();
            if(!possibleDirections.isEmpty()) {
                Direction chosenDirection = chooseBestDirection(possibleDirections);
                moveInDirection(chosenDirection);
            }else{
                currentDirection = currentDirection.getOpposite();
                moveInDirection(currentDirection);
            }
        }
    }
    private List<Direction> getPossibleDirections() {
        List<Direction> directions = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir != currentDirection.getOpposite() && canMoveInDirection(dir)) {
                System.out.println("canMove");
                directions.add(dir);
            }
        }
        return directions;
    }

    private List<Direction> getPossibleDirectionsNotCurrent() {
        List<Direction> directions = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            System.out.println(dir);
            if (dir != currentDirection.getOpposite() && canMoveInDirection(dir) && dir != currentDirection) {
                System.out.println("canMove");
                directions.add(dir);
            }
        }
        return directions;
    }


    private boolean canMoveInDirection(Direction direction) {
        double nextX = ghost.ghostShape.getX() + direction.getDeltaX() * ghost.getSpeed();
        double nextY = ghost.ghostShape.getY() + direction.getDeltaY() * ghost.getSpeed();
        return !gameBoard.ghostWillCollide(nextX, nextY, ghost);
    }




    private void moveInDirection(Direction direction) {
        double moveX = direction.getDeltaX() * ghost.getSpeed();
        double moveY = direction.getDeltaY() * ghost.getSpeed();
        double nextX = ghost.getX() + moveX;
        double nextY = ghost.getY() + moveY;

        if (canMoveInDirection(direction)) {
            System.out.println("Moving");
            ghost.move(nextX, nextY);
            currentDirection = direction;
        } else {
            Direction alternativeDirection = findAlternativeDirection(direction);
            if (alternativeDirection != null) {
                moveX = alternativeDirection.getDeltaX() * ghost.getSpeed();
                moveY = alternativeDirection.getDeltaY() * ghost.getSpeed();
                ghost.move(ghost.getX() + moveX, ghost.getY() + moveY);
                currentDirection = alternativeDirection;
            } else {
                System.out.println("No movement possible");
            }
        }
    }

    private Direction findAlternativeDirection(Direction blockedDirection) {
        // Check all other directions except the blocked one and the opposite (to avoid going back immediately)
        Direction[] allDirections = Direction.values();
        for (Direction dir : allDirections) {
            if (dir != blockedDirection && dir != blockedDirection.getOpposite() && canMoveInDirection(dir)) {
                return dir; // Return the first alternative direction where the ghost can move
            }
        }
        return null; // No alternative direction found
    }


    private Direction chooseBestDirection(List<Direction> directions) {
        Direction bestDirection = directions.get(0); // Default to the first direction as a fallback
        double minDistance = Double.MAX_VALUE;

        for (Direction dir : directions) {
            double nextX = ghost.getX() + dir.getDeltaX() * ghost.getSpeed();
            double nextY = ghost.getY() + dir.getDeltaY() * ghost.getSpeed();
            double distanceToPacMan = calculateDistance(nextX, nextY, pacMan.getX(), pacMan.getY());

            if (distanceToPacMan < minDistance) {
                minDistance = distanceToPacMan;
                bestDirection = dir;
            }

        }

        return bestDirection;
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }





}

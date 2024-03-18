package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.map.GameBoard;
import com.gruppe2.gameCharacters.pacman.PacMan;

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
        System.out.println("Resetting ghost initial direction");

        currentDirection = Direction.RIGHT;
    }
    @Override
    public void move() {
        // Sjekker om det er mulig å bevege seg i gjeldende retning
        if (canMoveInDirection(currentDirection)) {
            // Henter mulige retninger som ikke er motsatt av gjeldende retning
            List<Direction> optionalDirection = getPossibleDirections();
            // Hvis det finnes andre mulige retninger, velger en og beveger seg i den retningen
            if(!optionalDirection.isEmpty()){
                Direction chosenDirection = chooseBestDirection(optionalDirection);
                moveInDirection(chosenDirection);
            }else {
                // Hvis ingen andre retninger er mulige, fortsetter i gjeldende retning
                moveInDirection(currentDirection);
            }
        } else{
            // Hvis det ikke er mulig å bevege seg i gjeldende retning, finn nye mulige retninger
            List<Direction> possibleDirections = getPossibleDirections();
            if(!possibleDirections.isEmpty()) {
                // Velger den beste retningen og beveger seg i den retningen
                Direction chosenDirection = chooseBestDirection(possibleDirections);
                moveInDirection(chosenDirection);
            }else{
                // Hvis ingen retninger er mulige, bytt til motsatt retning og prøv å bevege seg
                currentDirection = currentDirection.getOpposite();
                moveInDirection(currentDirection);
            }
        }
    }

    private List<Direction> getPossibleDirections() {
        // Lager en liste over mulige retninger som ikke er motsatt av gjeldende retning og hvor bevegelse er mulig
        List<Direction> directions = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir != currentDirection.getOpposite() && canMoveInDirection(dir)) {
                directions.add(dir);
            }
        }
        return directions;
    }

    private boolean canMoveInDirection(Direction direction) {
        // Beregner neste X og Y basert på retning og hastighet, og sjekker om bevegelse vil forårsake kollisjon
        double nextX = ghost.ghostShape.getX() + direction.getDeltaX() * ghost.getSpeed();
        double nextY = ghost.ghostShape.getY() + direction.getDeltaY() * ghost.getSpeed();
        return !ghost.gm.ghostWillCollide(nextX, nextY, ghost);
    }

    private void moveInDirection(Direction direction) {
        // Beregner bevegelse i gitt retning og utfører bevegelsen hvis det er mulig
        double moveX = direction.getDeltaX() * ghost.getSpeed();
        double moveY = direction.getDeltaY() * ghost.getSpeed();
        double nextX = ghost.getX() + moveX;
        double nextY = ghost.getY() + moveY;

        if (canMoveInDirection(direction)) {
            ghost.move(nextX, nextY);
            currentDirection = direction;
        } else {
            // Finn et alternativ hvis den valgte retningen er blokkert
            Direction alternativeDirection = findAlternativeDirection(direction);
            if (alternativeDirection != null) {
                moveInDirection(alternativeDirection);
            } else {
                System.out.println("Ingen bevegelse mulig");
            }
        }
    }

    private Direction findAlternativeDirection(Direction blockedDirection) {
        // Søker etter en alternativ retning som ikke er blokkert eller motsatt av gjeldende retning
        Direction[] allDirections = Direction.values();
        for (Direction dir : allDirections) {
            if (dir != blockedDirection && dir != blockedDirection.getOpposite() && canMoveInDirection(dir)) {
                return dir; // Returnerer første mulige alternative retning
            }
        }
        return null; // Ingen alternativ retning funnet
    }

    private Direction chooseBestDirection(List<Direction> directions) {
        // Velger den beste retningen basert på den korteste avstanden til Pac-Man
        Direction bestDirection = directions.get(0); // Standard retning som fallback
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
        // Beregner avstanden mellom to punkter
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }





}

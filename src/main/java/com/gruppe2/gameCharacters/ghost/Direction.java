package com.gruppe2.gameCharacters.ghost;

/**
 * Enum for retninger.
 * @Author Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @method getDeltaX() returnerer x-koordinaten for retningen.
 * @method getDeltaY() returnerer y-koordinaten for retningen.
 * @method getOpposite() returnerer motsatt retning.
 */

public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    private final int deltaX;
    private final int deltaY;

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
    public Direction getOpposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: throw new AssertionError("Unknown direction: " + this);
        }
    }

}

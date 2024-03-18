package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.gameCharacters.GameCharacter;
import com.gruppe2.gameCharacters.pacman.PacMan;
import com.gruppe2.map.GameBoard;
import com.gruppe2.utils.GameManager;
import com.gruppe2.utils.Node;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstrakt klasse som representerer et spøkelse i spillet.
 * Spøkelset har funksjonalitet for bevegelse, kollisjonssjekk, flukt fra Pac-Man, og mer.
 */
public class Ghost extends GameCharacter {
    public ImageView ghostShape;
    protected double x, y;
    protected final double width = 16;
    protected final double height = 16;

    protected GameBoard gameBoard;

    private double speed;
    private double initialX;
    private double initialY;
    protected GameManager gm;
    private Flee flee;

    private int points;
    private Move moveType;
    private PacMan pacMan;

    private double wanderTargetX = -1;
    private double wanderTargetY = -1;


    /**
     * Konstruerer et spøkelse med angitte koordinater, bilde, spillbrett, hastighet og spilladministrasjon.
     *
     * @param x          x-koordinaten til spøkelset.
     * @param y          y-koordinaten til spøkelset.
     * @param speed      Hastigheten til spøkelset.
     * @param gm         Spilladministrasjonen som kontrollerer spillet.
     * @param points     Poengene spøkelset gir når det blir spist.
     */

    public Ghost( double x, double y,  double speed, GameManager gm, int points ) {
        this.gm = gm;
        this.points = points;

        this.x = x - width /2 ;
        this.y = y - height /2;
        this.initialX = this.x;
        this.initialY = this.y;

        this.gameBoard = gm.getGameBoard();

        ghostShape = new ImageView();
        ghostShape.setFitWidth(width);
        ghostShape.setFitHeight(height);
        ghostShape.setX(this.x);
        ghostShape.setY(this.y);
        ghostShape.setPreserveRatio(false);

        this.speed = speed;
        this.flee = new Flee(this, gameBoard);
        this.pacMan = gameBoard.getPacMan();
    }


    /*** Flytter spøkelset til en ny posisjon ***/

    public void move(double dx, double dy) {
        if(!this.gm.ghostWillCollide(dx, dy, this) ){
                setPosistion(dx, dy);
        }
    }
    public void chase(){
        moveType.move();
    };
    public void flee(){
        flee.fleeFromPacman();
    }
    public  void stopFleeing() {
        moveType.move();
    }

    protected void moveToPacManDirectly() {
        double pacManX = pacMan.getX();
        double pacManY = pacMan.getY();
        double deltaX = pacManX - this.x;
        double deltaY = pacManY - this.y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if ( distance > 0 ) { // unngå deling på 0

            double moveX = (deltaX / distance) * speed;
            double moveY = (deltaX / distance) * speed;

            if ( !this.gm.ghostWillCollide(this.x + moveX, this.y, this) ) {
                this.x += moveX;
            } else if ( !this.gm.ghostWillCollide(this.x, this.y + moveY, this) ) {
                this.y += moveY;
            } else {
                handleBeingStuck();
            }

            move(this.x, this.y);
        }
    }
    private void handleBeingStuck() {
        // Simple strategy: snu ved å invertere retning
        double newTargetX = this.x - (wanderTargetX - this.x);
        double newTargetY = this.y - (wanderTargetY - this.y);
        // Være sikker på at det nye målet er innenfor spillbrettet
        wanderTargetX = Math.max(0, Math.min(newTargetX, gameBoard.getWidth() - width * 2));
        wanderTargetY = Math.max(0, Math.min(newTargetY, gameBoard.getHeight() - height * 2));
    }

    /*** setter posisjon til start, start + 20 på x akse om start berører ved pac ***/
    public void resetPosition() {
        PacMan pacMan = gm.getGameBoard().getPacMan();
        Bounds ghostBounds = new BoundingBox(this.startLocation.getX(), this.startLocation.getX(), ghostShape.getFitWidth(), ghostShape.getFitHeight());
        if(pacMan.getShape().getBoundsInParent().intersects(ghostBounds)){
            setPosistion(initialX + 20, initialY);
        }else {
            setPosistion(initialX,initialY);
        }
    }

    /*** sjekker av kollisjon og line of sight ***/
    public boolean checkCollide(double dx, double dy, boolean isPackMan){
        PacMan pacMan = gm.getGameBoard().getPacMan();
        double centerX = dx;
        double centerY = dy;
        double radius = pacMan.getRadius();

        // Stiller på omriss for å stramme in hitbox ved å trekke fra 1 på radius for kalkulasjon
        double adjustedRadius = radius - 1;
        double minX = centerX - adjustedRadius;
        double minY = centerY - adjustedRadius;
        double width = 2 * adjustedRadius; // tilpasset diameter
        double height = 2 * adjustedRadius; // tilpasset diameter

        Bounds pacManBounds = new BoundingBox(minX, minY, width, height);

        ImageView ghostShapeView = this.ghostShape;

        double ghostMinX = ghostShapeView.getX() + 3; // tilpasset for å stramme inn hitbox
        double ghostMinY = ghostShapeView.getY() + 3; // tilpasset for å stramme inn hitbox
        double ghostWidth = ghostShapeView.getFitWidth() - 6; // tilpasset med 3 på hver side
        double ghostHeight = ghostShapeView.getFitHeight() - 6; // tilpasset med 3 på hver side
        Bounds ghostBounds = new BoundingBox(ghostMinX, ghostMinY, ghostWidth, ghostHeight);

        if (ghostBounds.intersects(pacManBounds)){
            return true;
        }

        return false;
    }
    // Sjekker om spøkelset og pac er på samme linje horisontalt
    private boolean isAlignedHorizontally(double ghostX, double pacManX, double radius) {
        return Math.abs(ghostX - pacManX) <= radius;
    }
    // Sjekker om spøkelset og pac er på samme linje vertikalt
    private boolean isAlignedVertically(double ghostY, double pacManY, double radius) {
        return Math.abs(ghostY - pacManY) <= radius;
    }


    // Sjekker om det er en klar bane vertikalt
    private boolean isClearPathVertical(int x, int startY, int endY, GameBoard gameBoard) {
        for (int y = startY; y <= endY; y++) {
            if (this.gm.hasWall(x, y)) return false;
        }
        return true;
    }

    // Sjekker om det er en klar bane horisontalt
    private boolean isClearPathHorizontal(int y, int startX, int endX, GameBoard gameBoard) {
        for (int x = startX; x <= endX; x++) {
            if (this.gm.hasWall(x, y)) return false;
        }
        return true;
    }
    private boolean isValidPosition(int x, int y) {
        // Sjekk om (x, y) er innenfor game board
        return x >= 0 && x < gameBoard.getWidth() && y >= 0 && y < gameBoard.getHeight();
    }
    private int[] lineOfSightDirection() {
        // horisontal line of sight
        if (isAlignedHorizontally(this.getX(), pacMan.getX(), pacMan.getRadius())) {
            if (isClearPathVertical((int)this.getX(), (int)this.getY(), (int)pacMan.getY(), gameBoard)) {
                return new int[]{0, pacMan.getY() > this.getY() ? 1 : -1}; // flytte vertikalt mot Pac-Man
            }
        }
        // Vertikal line of sight
        else if (isAlignedVertically(this.getY(), pacMan.getY(), pacMan.getRadius())) {
            if (isClearPathHorizontal((int)this.getY(), (int)this.getX(), (int)pacMan.getX(), gameBoard)) {
                return new int[]{pacMan.getX() > this.getX() ? 1 : -1, 0}; // Flytte horisontalt mot Pac-Man
            }
        }
        return null; // ikke i direkte line of sight
    }
    protected boolean lineOfSightToPacMan() {
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
                    if (this.gm.hasWall(this.x, checkY)) {
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
                    if (checkY != this.x && this.gm.hasWall(checkY, this.y)) {
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
                    if (this.gm.hasWall((int) checkX, (int) checkY)) {
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


    /*** SETTERS ***/
    public void setMoveType(Move moveType){
        this.moveType = moveType;
    }

    protected void setImage(Image image){
        ghostShape.setImage(image);
    }
    private void setPosistion(double dx, double dy){
        x = dx;
        y = dy;
        ghostShape.setX(x);
        ghostShape.setY(y);
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }

    @Override
    public void setStartLocation( Point2D startLocation) {
        this.startLocation = startLocation;
    }


    /*** GETTERS ***/

    public int getPoints(){
        return points;
    }

    public double getWidth(){
        return width;
    }

    public ImageView getShape() {
        return ghostShape;
    }
    public Move getMoveType(){
        return moveType;
    }
    protected double getX(){
        return x;
    }

    protected double getY(){
        return y;
    }

    protected double getSpeed(){
        return speed;
    }
    public List<Node> getNeighbors( Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[] directLineOfSight = lineOfSightDirection();

        if (directLineOfSight != null) {
            int newX = node.getX() + directLineOfSight[0];
            int newY = node.getY() + directLineOfSight[1];
            // Check if the new position is valid and there are no walls directly in the path.
            if (isValidPosition(newX, newY) && !this.gm.hasWall(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }

        } else {
            int[][] offsets = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] offset : offsets) {
                int newX = node.getX() + offset[0];
                int newY = node.getY() + offset[1];

                // Adjust the collision check based on movement direction
                boolean wallCollision = switch (offset[0] + "," + offset[1]) {
                    case "0,1" -> this.gm.hasWall(newX, newY + this.getShape().getFitHeight()); // Down
                    case "0,-1" -> this.gm.hasWall(newX, newY); // Up
                    case "-1,0" -> this.gm.hasWall(newX, newY); // Left
                    case "1,0" -> this.gm.hasWall(newX + this.getShape().getFitWidth(), newY); // Right
                    default -> false;
                };


                if (isValidPosition(newX, newY) && !wallCollision) {
                    neighbors.add(new Node(newX, newY));
                }

            }
        }
        return neighbors;
    }


}

package com.gruppe2.packman;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/*
Author: Borgar Flaen Stensrud
Usage: Lager gameboard til skjermen. Klasse for å tegne det grafiske på spillet. Tegner pacman, vegger og dotter. samt energizers.
 */

public class GameBoard extends Pane {
    private final int tileSize = 20;
    private PacMan pacMan;
    private List<Rectangle> walls = new ArrayList<>();

    private List<Circle> dots = new ArrayList<>();


    public GameBoard(String levelPath){
        System.out.println(levelPath);
        loadLevel(levelPath);
    }

    // Laster level basert på tekstfil
    private void loadLevel(String levelPath){

        try ( InputStream is = getClass().getResourceAsStream(levelPath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int y = 0;
            String line;
            while((line =  br.readLine()) != null){
                for(int x = 0; x < line.length(); x++){
                    char c = line.charAt(x);
                    switch(c){
                        case '#':
                            Rectangle wall = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
                            wall.setFill(Color.BLUE);
                            this.getChildren().add(wall);
                            walls.add(wall);
                            break;
                        case '.':
                            Circle dot = new Circle(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, tileSize / 4.0);
                            dot.setFill(Color.BLACK);
                            dots.add(dot);
                            this.getChildren().add(dot);
                            break;
                        case 'o':
                            Circle energizer = new Circle(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, tileSize / 2.0);
                            energizer.setFill(Color.YELLOW);
                            System.out.println(x * tileSize + tileSize / 2.0);
                            this.getChildren().add(energizer);
                            break;
                        case 'P':
                            pacMan = new PacMan(x * tileSize + tileSize  / 2.0, y * tileSize + tileSize / 2.0);
                            this.getChildren().add(pacMan.getShape());
                            break;
                        case 'G':
                            Ghost blinky;
                            blinky = new Blinky(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, Color.RED);
                            this.getChildren().add(blinky.ghostShape);
                            break;
                    }
                }
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Kolisjons test, tester om pacman vill kolidere på gitte kordinater.
    public boolean willCollide(double nextX, double nextY) {
        Bounds pacManBounds = new BoundingBox(nextX - pacMan.getRadius(), nextY - pacMan.getRadius(),
                2 * pacMan.getRadius(), 2 * pacMan.getRadius());
        for (Rectangle wall : walls) {
            if (wall.getBoundsInParent().intersects(pacManBounds)) {
                return true; // kollisjon
            }
        }
        return false; // ingen kollisjon
    }

    //Kolisjonstest sjekker om pacman vill spise en dot på gitte kordinater.
    public Circle willEatDot(double nextX, double nextY){
        Bounds pacManBounds = new BoundingBox(nextX - pacMan.getRadius(), nextY - pacMan.getRadius(),
                2 * pacMan.getRadius(), 2 * pacMan.getRadius());
        for (Circle dot : dots) {
            if (dot.getBoundsInParent().intersects(pacManBounds)) {
                return dot;
            }
        }
        return null; // No dot eaten
    }

    //fjerner dot fra GameBoard
    public void removeDot(Circle dot){
        this.getChildren().remove(dot);
        dots.remove(dot); // Assuming 'dots' is a List<Circle> of all dot objects
    }
    //returnerer pacman
    public PacMan getPacMan(){
        return pacMan;
    }

}

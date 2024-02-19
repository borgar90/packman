package com.gruppe2.packman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.*;

public class GameBoard extends Pane {
    private final int tileSize = 20;

    public GameBoard(String levelPath){
        System.out.println(levelPath);
        loadLevel(levelPath);
    }

    private void loadLevel(String levelPath){
        InputStream is;
        try{
            is = getClass().getResourceAsStream(levelPath);
        }catch (Exception ex){
            System.out.println(ex);
            return;
        }
        System.out.println("fil lastet");
        try (
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
                            this.getChildren().add(wall);break;
                        case '.':
                            Circle dot = new Circle(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, tileSize / 4.0);
                            dot.setFill(Color.BLACK);
                            this.getChildren().add(dot);
                            break;
                        case 'o':
                            Circle energizer = new Circle(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, tileSize / 2.0);
                            energizer.setFill(Color.YELLOW);
                            this.getChildren().add(energizer);

                            break;
                        case 'P':
                            PacMan pacMan;
                            pacMan = new PacMan(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0);
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
}

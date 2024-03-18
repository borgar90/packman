package com.gruppe2.map;

import javafx.geometry.Point2D;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelGenerator {
    private int[][] level;
    private int width;
    private int height;

    private List<Wall> walls;
    private char[][] maze;
    private Random rand = new Random();
    private List<String> levelPaths = new ArrayList<>();
    private String currentLevelPath;



    public LevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        level = new int[width][height];
        this.maze = new char[height][width];
        generateNewLevel();
    }

    private void generateNewLevel(){
        // Generer labyrinten med vegger og stier
        generateMaze();
        // Generer tabletter på tilfeldige steder i labyrinten
        generateTablets();
        // Rett opp vegger og sikre at portaler er tilgjengelige
        fixWallsAndEnsurePortalAccessibility();
        // Skriv ut labyrinten til konsollen for visuell inspeksjon
        printMaze();
        // Lagre den genererte labyrinten til en fil og returner filnavnet
        currentLevelPath = storeLevel();
        levelPaths.add(currentLevelPath);
    }

    private String storeLevel(){
        // Bygg strengen som representerer labyrinten
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(maze[i][j]);
            }
            sb.append("\n");
        }
        // Lag et unikt filnavn basert på nåværende tidsstempel
        String name = "level" + System.currentTimeMillis() + ".txt";
        String path = "src/main/resources/levels/" + name;
        // Skriv labyrinten til filen
        try{
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(sb.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("En feil oppstod.");
            e.printStackTrace();
        }

        return name;
    }

    private void generateTablets(){
        // Samler koordinater hvor det er mulig å plassere tabletter
        List<Point2D> cords = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(maze[i][j] == ' '){
                    Point2D cord = new Point2D(j, i);
                    cords.add(cord);
                }
            }
        }

        Random rand = new Random();
        // Plasser 4 energitabletter på tilfeldige posisjoner
        for (int i = 0; i < 4; i++) {
            try {
                int index = rand.nextInt(cords.size()-1);
                Point2D cord = cords.remove(index);
                maze[(int)cord.getY()][(int)cord.getX()] = 'o';
            } catch (Exception e) {
                System.out.println("Feil: " + e);
            }
        }
        // Fyll resten av de ledige plassene med små tabletter
        for (Point2D cord : cords) {
            maze[(int)cord.getY()][(int)cord.getX()] = '.';
        }
    }

    // Funksjonene getUnvisitedNeighbors, initializeMaze, generateMaze, placePortals, createPathsAndTablets, removeWallBetween, fixWallsAndEnsurePortalAccessibility, og printMaze følger samme mønster for opprettelse, navigering, og presentasjon av labyrinten, med sikte på å sikre spillbarhet og rettferdighet i spillmekanikken.

    // getUnvisitedNeighbors - Henter naboer som ikke er besøkt enda for den nåværende cellen.
    // initializeMaze - Initialiserer hele labyrinten med vegger før generering av stier.
    // generateMaze - Ansvarlig for å generere stiene i labyrinten ved å fjerne vegger.
    // placePortals - Plasserer portaler på tilgjengelige steder i labyrinten.
    // createPathsAndTablets - Kombinerer logikken for å generere stier og plassere tabletter.
    // removeWallBetween - Fjerner vegger mellom to celler for å lage en sti.
    // fixWallsAndEnsurePortalAccessibility - Sikrer at portaler er tilgjengelige og at det ikke er unødvendige vegger som blokkerer dem.
    // printMaze - Skriver ut den genererte labyrinten til konsollen for visuell verifisering.


    private List<Point2D> getUnvisitedNeighbors(int x, int y) {
        List<Point2D> neighbors = new ArrayList<>();

        // (N, S, E, W)
        int[] dx = {-2, 2, 0, 0};
        int[] dy = {0, 0, -2, 2};
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            // Sikre at naboen er på brettet og at nabo ikke er blitt besøkt.
            if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && maze[ny][nx] == '#') {
                neighbors.add(new Point2D(nx, ny));
            }
        }

        return neighbors;
    }
    private void initializeMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = '#';
            }
        }
    }

    private void generateMaze() {
        initializeMaze();
        createPathsAndTablets();
        placePortals();
    }

    private void placePortals() {
        Random rand = new Random();

        // Sikre at portaler ikke plasseres i hjørner
        int portalPositionLeftRight = 1 + rand.nextInt(height - 2);
        int portalPositionTopBottom = 1 + rand.nextInt(width - 2);

        // Plasser portaler
        maze[portalPositionLeftRight][0] = '*'; // venstre vegg
        maze[portalPositionLeftRight][width - 1] = '*'; // Høyre vegg
        maze[0][portalPositionTopBottom] = '*'; // Top veggen
        maze[height - 1][portalPositionTopBottom] = '*'; // Bunnen veggen
    }

    private void createPathsAndTablets() {
        int x = 2 * rand.nextInt((width - 4) / 2) + 1;
        int y = 2 * rand.nextInt((height - 4) / 2) + 1;

        maze[y][x] = ' ';

        List<Point2D> stack = new ArrayList<>();
        stack.add(new Point2D(x, y));

        while (!stack.isEmpty()) {
            Point2D current = stack.get(stack.size() - 1);
            List<Point2D> neighbors = getUnvisitedNeighbors((int)current.getX(), (int)current.getY());

            if (!neighbors.isEmpty()) {
                Point2D chosen = neighbors.get(rand.nextInt(neighbors.size()));
                removeWallBetween((int)current.getX(), (int)current.getY(), (int)chosen.getX(), (int)chosen.getY());
                if(chosen.getX() % 2 == 0 && chosen.getY() % 2 == 0){
                    maze[(int)chosen.getY()][(int)chosen.getX()] = '.';
                } else {
                    maze[(int)chosen.getY()][(int)chosen.getX()] = '.';
                }
                stack.add(chosen);
            } else {
                stack.remove(stack.size() - 1);
            }
        }


    }



    private void removeWallBetween(int x1, int y1, int x2, int y2) {
        int dx = (x2 - x1) / 2;
        int dy = (y2 - y1) / 2;
        int wx = x1 + dx;
        int wy = y1 + dy;
        maze[wy][wx] = ' ';
    }

    private void fixWallsAndEnsurePortalAccessibility() {
        for (int i = 0; i < height; i++) {
            maze[i][0] = (maze[i][0] == '*') ? '*' : '#'; // Fix venstre vegg
            maze[i][width - 1] = (maze[i][width - 1] == '*') ? '*' : '#'; // Fix høyre vegg
        }

        for (int j = 0; j < width; j++) {
            maze[0][j] = (maze[0][j] == '*') ? '*' : '#'; // Fix topp veggen
            maze[height - 1][j] = (maze[height - 1][j] == '*') ? '*' : '#'; // Fix bunn veggen
        }
    }

    public void printMaze() {
        for (char[] row : maze) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    public String getCurrentLevelPath(){
        return currentLevelPath;
    }

}

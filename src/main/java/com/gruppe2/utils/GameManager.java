package com.gruppe2.utils;

import com.gruppe2.DB.MongoUtil;
import com.gruppe2.GUI.GameUI;
import com.gruppe2.GUI.MainMenu;
import com.gruppe2.GUI.PopUp;
import com.gruppe2.GUI.TopMenu;
import com.gruppe2.gameCharacters.ghost.Ghost;
import com.gruppe2.gameCharacters.pacman.PacMan;
import com.gruppe2.map.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

import static com.gruppe2.packman.PacManGame.*;

/**
 @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 @Usage: Denne klassen er en manager for hele spillet. den starter game loop og den setter GUI.
 Denne klassen håndterer også bevegelse av Pacman, og kollisjon med vegger og spøkelser.
    den håndterer også spising av dots og spøkelser.
    den håndterer også teleportering av pacman.
    den håndterer også spøkelser som blir redde.
    den håndterer også når spillet er tapt.
    den håndterer også når spillet er restartet.
    den håndterer også når spillet er lastet.
    den håndterer også poeng, og lagring av poeng til database.
 */

public class GameManager {


    /* MAIN MENU */
    private boolean mainMenuFinished = false;
    private MainMenu mainMenu;



    /* LEVEL OG LEVEL GENERATOR */
    private String currentLevel = "/level1.txt";
    private LevelGenerator levelGenerator;
    private List<String> levelList;
    private int levelNum = 0;



    /* GAME BOARD: VEGGER; SPØKELSER; TABLETTER; PAC; PORTALER */
    private GameBoard gameBoard;
    private PacMan pacMan;
    private List<Wall> walls;
    private List<Tablet> tablets;
    private List<Portal> portals;
    private List<Ghost> ghosts;
    private boolean ghostOnFlee = false;


    /* SPILL DATA */
    private int points = 0;
    private int lives = 3;
    private int[] completedLevelsPoints;
    private String nickName;


    /* GUI OG ANIMASJON */
    private GameUI gameUI;
    private AnimationTimer gameLoop;
    private long lastUpdate = 0;
    private Button restartButton;
    private boolean resetIsActive = false;
    private boolean isGameLoopRunning = false;
    private boolean isHandlingCollision = false;



    public GameManager(){
        levelList = Arrays.asList("/level1.txt", "/level3.txt",  "/level2.txt", "/level4.txt");
        currentLevel = levelList.get(levelNum);
        this.restartButton = new Button("Restart");
        newGame();
    }

    //Starter nytt spill
    public void newGame(){
        lives = 3;
        points = 0;
        this.currentLevel = "level1.txt";
        completedLevelsPoints = new int[1];
        setupGameLoop();
        initNewLevel(currentLevel);
    }


    //Laster inn spillobjekter fra gameBoard
    public void loadGameBoardObjects(){
        initCompletedLevelPoints();
        this.ghosts = gameBoard.getGhostList();
        this.pacMan = gameBoard.getPacMan();
        this.pacMan.setLives(lives);
        this.portals = gameBoard.getPortals();
        this.walls = gameBoard.getWalls();
        this.tablets = gameBoard.getTablets();
    }



    /*** LEVEL METHODS ***/
    //Initialiserer nytt level, og setter opp gameBoard og gameUI
    // Setter også opp menyen på toppen av spillet.
    // Setter også opp gameLoop for å oppdatere skjerm bassert på kalkulasjoner og algoritmer.
    // Setter også opp gameUI for å vise poeng og liv.
    // Setter også opp gameBoard for å vise vegger, spøkelser, tabletter og pacman.
    // Basert på level-strenget som blir sendt inn, vil gameBoard bli satt opp.
    private void initNewLevel(String level){
        currentLevel = level;
        GameBoard nextLevel = new GameBoard(level, this);
        this.gameBoard = nextLevel;
        loadGameBoardObjects();
        this.gameUI = new GameUI(this);
        setRootWithMenu();
        this.gameUI.setGameBoard(nextLevel);

    }

    //Neste level
    private void nextLevel(String level){
        stopGame();
        gameUI.showLevelPreparePopup();
        saveLevelScore();
        completedLevelsPoints[completedLevelsPoints.length-1] = points;
        // These actions will happen after the pause
        initNewLevel(level);
        currentLevel = level;
        // Use PauseTransition to wait before starting the next level
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            startGameLoop();
        });
        delay.play();
    }


    // Starte samme nivå på nytt, med samme poengsum som lagret fra forrige level.
    public void resetLevel() {
        stopGame();
        addAllPointsCombinded(); // legg til poeng fra forrige level
        gameUI.showResetLevelPreparePopup(); // vis popup
        initNewLevel(currentLevel); // start level
        pacMan.setLives(lives); // sett liv
        gameUI.refreshPoints(Integer.toString(points) + " poeng"); // oppdater poeng
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            startGameLoop();
        });
        delay.play();
    }
    //Genererer level
    public void generateLevel(){
        levelGenerator = new LevelGenerator(40, 30);
        String levelName = levelGenerator.getCurrentLevelPath();
        String levelPath = "levels/" + levelName ;
        this.currentLevel = levelPath;
        initNewLevel(currentLevel);
    }



    /*** GAME OVER AND VICTORY METHODS ***/
    private void victory() {
        PopUp victoryPopup = new PopUp("Victory - " + nickName + " fikk " + points + " poeng" + " \n Trykk Enter for restart", restartButton, this);
        gameUI.setResetPopup(victoryPopup);
        resetIsActive = true;
        stopGame();
    }
    private void gameOver() {
        PopUp resetPopup = new PopUp("Game Over - du fikk " + points + " poeng" + " \n Trykk Enter for restart", restartButton, this);
        gameUI.setResetPopup(resetPopup);
        resetIsActive = true;
        lives = 3;
        points = 0;
        stopGame();
    }


    /*** PACMAN MOVEMENT METHODS ***/
    public void movePacMan(KeyCode keyCode) {

        double nextX = pacMan.getX();
        double nextY = pacMan.getY();

        switch (keyCode) {
            case W: nextY = nextY - pacMan.getSpeed(); break;
            case S: nextY = nextY + pacMan.getSpeed(); break;
            case A: nextX = nextX - pacMan.getSpeed(); break;
            case D: nextX = nextX + pacMan.getSpeed(); break;
        }


        // wall detection
        if (!willCollide(nextX, nextY)) {

                pacMan.move(nextX, nextY);
                consumeGhostOrLoseLife(true);
                teleportPacMan(nextX, nextY);
                consumeTablet(nextX, nextY);


        }

    }


    /*** KEYBOARD HANDLING ***/

    // håndterer keyPress
    public void handleKeyPress( KeyCode keyCode) {
        if(mainMenuFinished){ // hvis main menu er ferdig
            if (keyCode == KeyCode.ENTER && resetIsActive) { // restart level, hvis enter er trykket og man er i game over mode.
                resetLevel(); // restart level
                resetIsActive = false;
            }else if(!resetIsActive){
                movePacMan(keyCode); // beveg pacman
            }
        }
    }

    //Håndterer keyRelease
    public void handleKeyRelease(KeyCode keyCode) {
        if(mainMenuFinished) {
            switch (keyCode) {
                case W:
                case S:
                case A:
                case D:
                    break;
                default:
                    break;
            }
        }
    }


    /*** GAME LOOP METHODS ***/
    public void stopGame() {
        if (isGameLoopRunning) {
            gameLoop.stop();
            isGameLoopRunning = false;
        }
    }
    public void startGameLoop() {
        if (!isGameLoopRunning) {
            gameLoop.start();
            isGameLoopRunning = true;
        }
    }
    // setter opp game loop for å oppdatere skjerm bassert på kalkulasjoner og algoritmer.
    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Spill logikk og oppdatering av skjerm
                updateGame(now);
            }
        };
    }


    //Game Board Methods...


    /*** PORTAL METHODS ***/

    //Teleporter pacman til ny portal
    public void teleportPacMan(double x, double y) {
        Portal foundPortal = findPortal(x , y ); // finner portal på x,y
        if (foundPortal != null) { // hvis portalen er funnet
            Rectangle newPortal = null;
            for(Portal aPortal: portals){ // loop gjennom portaler
                if(!foundPortal.equals(aPortal)){ // hvis portalen ikke er den samme
                    newPortal = aPortal.getPortalShape(); // ny portal
                }
            }
            if (newPortal != null) { // teleporter pacman til ny portal
                double newX = newPortal.getX(); // x kordinat til portalen
                if(newX == 0){
                    newX+= 21; // 21 er bredden fra portalen, måtte ha litt å gå på.
                }else{
                    newX-=10; // 10 er bredden fra portalen, måtte ha litt å gå på.
                }
                double newY = newPortal.getY() + newPortal.getHeight() / 2; // midten av portalen
                pacMan.move(newX, newY);
            }
        }
    }
    //Finner portal på gitte kordinater
    public Portal findPortal( double x, double y) {
        for ( Portal portal : portals){
            if(intersects(portal.getPortalShape(), x, y, pacMan.getRadius())){
                return portal;
            }
        }
        return null;
    }
    //Sjekker om pacman kolliderer med portal
    private boolean intersects( Rectangle portal, double centerX, double centerY, double radius) {
        double leftX = centerX - radius;
        double topY = centerY - radius;
        return portal.getBoundsInParent().intersects(leftX, topY, 2 * radius, 2 * radius);
    }



    /*** Kollisjons metoder ***/

    //Kollisjonstest, sjekker om GameCharacter vil kollidere med vegg på gitte kordinater.
    public boolean hasWall(double x, double y) {
        return walls.stream().anyMatch(wall -> {
            return wall.getWallShape().getBoundsInParent().contains(x, y); // kollisjon
        });
    }


    //Kolisjons test, tester om pacman vill kolidere på gitte kordinater.
    public boolean willCollide(double nextX, double nextY) {
        Bounds pacManBounds = new BoundingBox(nextX - pacMan.getRadius(), nextY - pacMan.getRadius(),
                2 * pacMan.getRadius(), 2 * pacMan.getRadius());

        for ( Wall wall : walls) {
            if (wall.getWallShape().getBoundsInParent().intersects(pacManBounds)) {
                return true; // kollisjon
            }
        }
        return false; // ingen kollisjon
    }



    /*** DATABASE METHODS ***/
    //Lagrer poeng til database
    public void saveLevelScore() {
        MongoDatabase db = MongoUtil.getDatabase("pacman");
        MongoCollection<Document> collection = db.getCollection("levelScores");

        Document doc = new Document("nickname", nickName)
                .append("levelName", currentLevel)
                .append("score", points);
        collection.insertOne(doc);
    }


    /*** GHOST METHODS **/
    public void setGhostFlee(){
        ghostOnFlee = !ghostOnFlee; // setter spøkelser til å flykte/ikke flykte
        if(ghostOnFlee){
            for ( Ghost ghost : ghosts) {
                ghost.flee(); // spøkelser flykter
            }
        }else{ // hvis spøkelser ikke skal flykte
            for (Ghost ghost : ghosts) {
                ghost.stopFleeing(); // spøkelser slutter å flykte
            }}
    }


    //Kollisjonstest for spøkelser, sjekker om spøkelser vil kollidere på gitte kordinater.
    public void consumeGhostOrLoseLife(boolean isPacMove){
        if(isHandlingCollision) return;
        isHandlingCollision = true;
        for (Ghost ghost: ghosts){
            if(ghost.checkCollide(pacMan.getX(), pacMan.getY(), true)) {
                if(pacMan.getActive()){

                    deadGhost(ghost); // spis spøkelset

                    if(isPacMove) consumeTablet(pacMan.getX(), pacMan.getY()); // spis dot
                }else{
                    if(pacMan.getLives() == 0){
                        gameOver();
                    }
                    removeLife(); // mist liv
                    ghost.resetPosition(); // reset spøkelse
                }
            }
        }
        isHandlingCollision = false;
    }

    //oppdaterer posisjonen til spøkelse
    private void updateGame(long now) {
        if(pacMan.getLives() == 0){ // Game over
            gameOver();
            return;
        }

        // konverterer nå fra nanosekunder til millisekunder for lettere samenligning
        if (lastUpdate == 0 || (now - lastUpdate) / 1_000_000 > 30) { // mer en 30 ms siden sist
            if (this.ghosts != null) { // finnes spøkelser
                for ( Ghost ghost : ghosts ) { // loop gjennom spøkelser
                    if ( !pacMan.getActive() ) { // hvis pacman ikke er aktiv, ikke har spist energizer
                        if ( !ghostCollides(pacMan.getX(), pacMan.getY(), false) ){ // hvis spøkelset ikke kolliderer med pacman
                            ghost.setSpeed(3); // sett speed til 3
                            ghost.chase(); // jager pacman
                        }else{
                            consumeGhostOrLoseLife(false); // spis spøkelset eller mist liv
                        }
                    } else {
                        ghost.flee(); // spøkelset flykter
                    }
                }
            }
            lastUpdate = now; // oppdaterer siste oppdatering
        }
    }


    //Kollisjonstest for spøkelser, sjekker om spøkelser vil kollidere på gitte kordinater.
    public boolean ghostWillCollide(double nextX, double nextY, Ghost ghost) {
        // Nåværende omriss for spøkelset
        Bounds ghostBounds = ghost.getShape().getBoundsInParent();

        // Kalkuler ghost's width og height
        double ghostWidth = ghostBounds.getWidth();
        double ghostHeight = ghostBounds.getHeight();


        // kalkulerer nytt omriss vedrørende ghost's width og height
        double newMinX = nextX;
        double newMinY = nextY;

        // Kollisjonstest for vegger med spløkelser
        for (Wall wall : walls) {
            if (wall.getWallShape().getBoundsInParent().intersects(newMinX, newMinY, ghostWidth, ghostHeight)) {
                return true; // spøkelset kolliderer med en vegg
            }
        }
        // Kollisjonstest for portaler med spløkelser
        for (Portal portal: portals) {
            if (portal.getPortalShape().getBoundsInParent().intersects(newMinX, newMinY, ghostWidth, ghostHeight)) {
                return true; // spøkelset kolliderer med en portal
            }
        }
        return false; // ingen kollisjon
    }

    //Kollisjonstest for spøkelser, loop gjennom alle spøkelser og sjekk om de kolliderer.
    public boolean ghostCollides(double dx, double dy, boolean isPackMan){
        if (this.ghosts != null) {
            for (Ghost ghost : ghosts) {
                if (ghost.checkCollide(dx, dy, isPackMan)) {
                    return true; // kollisjon
                }
            }
        }
        return false; // ingen kollisjon
    }

    // Død spøkelse, legger til poeng og resetter spøkelset.
    public void deadGhost(Ghost ghost){
        points += ghost.getPoints();
        ghost.resetPosition();
        String score = Integer.toString(points);
        score += " poeng";
        gameUI.refreshPoints(score);
    }


    /*** TABLET METHODS ***/

    //Kolisjonstest sjekker om pacman vill spise en dot på gitte kordinater.
    public Tablet willEatTablet(double nextX, double nextY){
        Bounds pacBounds = new BoundingBox(pacMan.getShape().getCenterX(), pacMan.getShape().getCenterY(), 1, 1);

        for (Tablet tablet: tablets) {
            Bounds tabletBounds = new BoundingBox(tablet.getShape().getCenterX(), tablet.getShape().getCenterY(), 1, 1);
            if (tabletBounds.intersects(pacBounds)) { // kollisjon mellom pacman og dot
                return tablet;
            }
        }
        return null; // No dot eaten
    }
    private void consumeTablet(double nextX, double nextY) {
        Tablet consumedTablet = willEatTablet(pacMan.getX(), pacMan.getX());
        // sjekker om pacman spiser en dot

        if (consumedTablet != null) {
            if(consumedTablet instanceof EnergyTablet ){ // hvis pacman spiser energizer
                pacMan.setEnergizerActive();
            }

            gameBoard.removeTablet(consumedTablet); // fjerner dot
            addPoint(consumedTablet.getPoints()); // leger til poeng for spist dot
            if(gameBoard.getTablets().size() < 1){ // hvis ingen dots igjen
                if(levelNum < levelList.size()-1) { // hvis det er flere levels
                    String nextLevel = levelList.get(++levelNum); // neste level
                    nextLevel(nextLevel); //ikke generisk levelLoading
                }else{
                    victory(); // game over
                }
            }
        }
    }


    /*** POINTS METHODS ***/
    // Poeng for ferdig nivå lagres i en array, og legger til et nytt element i arrayet.
    // Dette gjør at vi kan lagre poeng for hvert nivå, og legge til poeng for hvert nivå.
    public void initCompletedLevelPoints(){
        int[] temp;
        if(completedLevelsPoints != null) {
            temp = new int[completedLevelsPoints.length+1];
            for ( int i = 0; i < completedLevelsPoints.length; i++ ) {
                temp[i] = completedLevelsPoints[i];
            }
            temp[temp.length-1] = 0;
            completedLevelsPoints = temp;
        }else{
            completedLevelsPoints = new int[1];
            completedLevelsPoints[0] = 0;
        }
    }
    public void addAllPointsCombinded(){
        int currentPoints = 0;
        currentPoints = Arrays.stream(completedLevelsPoints).sum();
        String score = Integer.toString(currentPoints);
        points = currentPoints;
        score += " points";
        gameUI.refreshPoints(score);
    }


   /*** POPUP AND MENU METHODS ***/
    public void hidePopup(){
        gameUI.hidePopup();
    }

    // Legger til en meny på toppen av spillet, tar gameUI og legger til menyen på en scene
    public void showMainMenuAddToScene(){
        stopGame();
        gameUI.setMenuOverlay(mainMenu.getMainMenu());
    }


    // SETTERS
    public void setNickname(String nickName){
        this.nickName = nickName;
    }

    // Legger til en meny på toppen av spillet, tar gameUI og legger til menyen på en scene
    // Setter også en scene på en stage, i static metode i main class.
    public void setRootWithMenu(){
        BorderPane bp = new BorderPane();
        TopMenu topMenu = new TopMenu(this);
        bp.setTop(topMenu);
        bp.setCenter(getGameUI());
        Scene scene = new Scene(bp, 1000, 800);
        setScene(scene, this);
    }

    // Ferdig med hovedmenyen
    public void setMainMenuFinished(boolean mainMenuFinished){
        this.mainMenuFinished = mainMenuFinished;
        setRootWithMenu();
    }

    // Legger til poeng for spist dot eller spøkelse
    public void addPoint(int point){
        points += point;
        String score = Integer.toString(points); // ny score til string.
        score += " poeng";
        gameUI.refreshPoints(score); // oppdaterer score
    }

    // Fjerner et liv fra pacman
    public void removeLife(){
        pacMan.loseLife();
        lives = pacMan.getLives();
        gameUI.removePacMan();
    }




    // GETTERS
    public String getNickname(){
        return nickName;
    }
    public GameBoard getGameBoard(){
        return gameBoard;
    }

    // Returnerer gameUI, hvis mainMenuFinished er true, fjerner vi menyen.
    // Hvis ikke, returnerer vi mainMenu.
    public Pane getGameUI() {
        if(mainMenuFinished){
            gameUI.removeMenuOverlay();
            return gameUI;
        }else{
            mainMenu = new MainMenu(this);
            return mainMenu.getMainMenu();
        }

    }

    public int getLives(){
        return lives;
    }

    public String getCurrentLevel(){
        return currentLevel.substring(0, currentLevel.length()-4);
    }

}
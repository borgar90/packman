package com.gruppe2.GUI;

import com.gruppe2.DB.MongoUtil;
import com.gruppe2.gameCharacters.ghost.*;
import com.gruppe2.utils.GameManager;
import com.gruppe2.gameCharacters.pacman.PacMan;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Dette er hovedmenyen til spillet.
 * Den inneholder en velkomsttekst, en inputboks for å skrive inn kallenavn,
 * og knapper for å starte spillet, generere et nytt nivå,
 * og vise highscore hentet fra database.
 */
public class MainMenu extends VBox {
    private GameManager gm;
    private VBox titleAndSpacer;

    public MainMenu(GameManager gm) {
        this.gm = gm;
        initScreen();
    }

    public VBox getMainMenu() {
        return this;
    }

    /**
     * Initialiserer hovedmenyen.
     */
    private void initScreen() {

        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: black; -fx-padding: 30px;");

        titleAndSpacer = new VBox();
        titleAndSpacer.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Erik-Tobias og Borgar sitt Pacman spill");
        title.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center;");
        titleAndSpacer.getChildren().add(title);



        Region spacer = new Region();
        spacer.setMinHeight(75);
        titleAndSpacer.getChildren().add(spacer);

        this.getChildren().add(titleAndSpacer);


        Label message = new Label("Enter nickname");
        message.setStyle("-fx-font-size: 20px; -fx-start-margin: 50px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center;");
        this.getChildren().add(message);

        TextField nickname = new TextField();
        nickname.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black; -fx-text-alignment: center;");
        nickname.setMaxWidth(200);
        this.getChildren().add(nickname);

        Button start = new Button("Start");
        start.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center; -fx-background-color: white; -fx-padding: 10px;");
        start.setMinWidth(200);
        start.setOnMouseClicked(event -> {
            gm.setNickname(nickname.getText());
            this.getChildren().clear();
            titleAndSpacerAndMenu2();
        });
        this.getChildren().add(start);
    }

    /**
     * Lager en ny menyside med tittel og det som er i showMenu2().
     * @return VBox
     */
    public VBox titleAndSpacerAndMenu2(){

        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: black; -fx-padding: 30px;");
        this.getChildren().add(titleAndSpacer);
        this.getChildren().add(showMenu2());
        return this;
    }

    /**
     * Legger til knapper for spill.
     * bruker getPacShape() og getGhosts() for å vise Pacman og spøkelsene.
     * legger til knappene for high score, spill og generer nivå.
     * @return VBox
     */

    private VBox showMenu2(){

        VBox menu = new VBox();
        menu.setSpacing(20);
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setMinWidth(400);

        menu.getChildren().add(getPacShape());

        Region spacer = new Region();
        spacer.setMinHeight(20);

        menu.getChildren().add(spacer);


        Label welcome = new Label("Velkommen til Pacman, " + gm.getNickname());
        welcome.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center;");
        menu.getChildren().add(welcome);

        Label play = new Label("Spill");
        play.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center; -fx-background-color: white; -fx-padding: 20px;");
        play.setOnMouseClicked(event -> {
            gm.setMainMenuFinished(true);
            gm.newGame();
        });
        play.setMaxWidth(200);
        play.setAlignment(Pos.CENTER);
        menu.getChildren().add(play);

        Label generateLevel = new Label("Generer Nivå");
        generateLevel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center; -fx-background-color: white; -fx-padding: 20px;");
        generateLevel.setOnMouseClicked(event -> {
            String advarsel = new String("Generering av level er ikke implementert, men vi har en demo av hvordan det kan se ut, Vill du fortsette?");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, advarsel, ButtonType.YES, ButtonType.NO);
            alert.setTitle("Generering av level");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) {
                return;
            }
            gm.setMainMenuFinished(true);
            gm.generateLevel();
        });

        generateLevel.setMaxWidth(200);
        generateLevel.setAlignment(Pos.CENTER);
        menu.getChildren().add(generateLevel);

        Label highScore = new Label("High Score");
        highScore.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center; -fx-background-color: white; -fx-padding: 20px;");
        highScore.setOnMouseClicked(event -> {
            this.getChildren().clear();
            this.getChildren().add(displayScores());
        });
        highScore.setMaxWidth(200);
        highScore.setAlignment(Pos.CENTER);
        menu.getChildren().add(highScore);


        Region spacer2 = new Region();
        spacer2.setMinHeight(30);
        menu.getChildren().add(spacer2);

        HBox ghosts = new HBox();
        ghosts.setSpacing(10);
        ghosts.setAlignment(Pos.CENTER);
        for(ImageView ghost : getGhosts()){
            ghosts.getChildren().add(ghost);
        }
        menu.getChildren().add(ghosts);
        return menu;
    }


    /**
     * Henter formen til Pacman og skalerer den.
     * @return Node
     */
    public Node getPacShape(){
        PacMan pacMan = new PacMan(0, 0, gm);
        Node pacManShape = pacMan.getShape();
        // Apply a scale transformation
        double scaleFactor = 5.0; // Adjust scale factor as needed
        pacManShape.setScaleX(scaleFactor);
        pacManShape.setScaleY(scaleFactor);
        return pacManShape;
    }

    /**
     * Henter formen til spøkelsene og skalerer dem.
     * @return List<ImageView>
     */
    public List<ImageView> getGhosts(){
        Clyde clyde = new Clyde(0, 0, gm);
        Pinky pinky = new Pinky(0, 0, gm);
        Blinky blinky = new Blinky(0, 0, gm);
        Inky inky = new Inky(0, 0, gm);
        List<ImageView> ghosts = new ArrayList<>();
        ghosts.add(clyde.getShape());
        ghosts.add(pinky.getShape());
        ghosts.add(blinky.getShape());
        ghosts.add(inky.getShape());

        for(ImageView ghostShape : ghosts){

            // Apply a scale transformation
            double scaleFactor = 5.0; // Adjust scale factor as needed
            ghostShape.setScaleX(scaleFactor);
            ghostShape.setScaleY(scaleFactor);
        }

        return ghosts;
    }

    /**
     * Henter high score fra database og viser det i en tabell.
     * @return VBox
     */
    public VBox displayScores() {
        VBox table = new VBox();
        table.setSpacing(5); // Space between rows
        table.setAlignment(Pos.TOP_CENTER);

        titleAndSpacer = new VBox();
        titleAndSpacer.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("High score på vår pacman");
        title.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center;");
        titleAndSpacer.getChildren().add(title);

        Region spacer = new Region();
        spacer.setMinHeight(75);
        titleAndSpacer.getChildren().add(spacer);

        table.getChildren().add(titleAndSpacer);

        // Title Row
        HBox titleRow = new HBox();
        Label nickNameLabel = new Label("Kallenavn");
        nickNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-border-color: blue; -fx-border-width: 1px;");
        nickNameLabel.setMinWidth(200);
        nickNameLabel.setPadding(new Insets(10, 20, 10, 20));

        Label levelNameLabel = new Label("Nivånavn");
        levelNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-border-color: blue; -fx-border-width: 1px;");
        levelNameLabel.setMinWidth(200);
        levelNameLabel.setPadding(new Insets(10, 20, 10, 20));

        Label scoreLabel = new Label("Poeng");
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-border-color: blue; -fx-border-width: 1px;");
        scoreLabel.setMinWidth(200);
        scoreLabel.setPadding(new Insets(10, 20, 10, 20));

        titleRow.getChildren().addAll(
                nickNameLabel, levelNameLabel, scoreLabel
        );
        titleRow.setAlignment(Pos.CENTER);


        table.getChildren().add(titleRow);

        // Fetch scores from MongoDB
        MongoDatabase db = MongoUtil.getDatabase("pacman");
        MongoCollection<Document> collection = db.getCollection("levelScores");

        for (Document doc : collection.find().sort(Filters.eq("score", -1))) { // Assuming you want to sort by score
            HBox row = new HBox();
            Label nickname = new Label(doc.getString("nickname"));
            nickname.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-border-color: green; -fx-border-width: 1px;");
            nickname.setMinWidth(200);
            nickname.setPadding(new Insets(10, 20, 10, 20));

            Label levelName = new Label(doc.getString("levelName"));
            levelName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-border-color: green; -fx-border-width: 1px;" );
            levelName.setMinWidth(200);
            levelName.setPadding(new Insets(10, 20, 10, 20));

            Label score = new Label(String.valueOf(doc.getInteger("score")));
            score.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-border-color: green; -fx-border-width: 1px;");
            score.setMinWidth(200);
            score.setPadding(new Insets(10, 20, 10, 20));

            row.getChildren().addAll(
                    nickname, levelName, score
            );

            row.setAlignment(Pos.CENTER);
            table.getChildren().add(row);
        }

        // Back Button at the bottom
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            this.getChildren().clear();
            titleAndSpacerAndMenu2();
        });
        backButton.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green; -fx-text-alignment: center; -fx-background-color: white; -fx-padding: 10px; -fx-start-margin: 20px");
        backButton.setMinWidth(200);

        table.getChildren().add(backButton);

        return table;
    }


}

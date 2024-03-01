package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.GameManager;
import com.gruppe2.packman.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Inky extends Ghost {

    private PatrolMove patrolMove;

    public Inky( double x, double y, Image ghost, GameBoard gameBoard, GameManager gm ) {
        super(x, y, ghost, gameBoard, 6, gm);
        List<Node> patrolPath = new ArrayList<>();
        patrolPath = setPatrolPath();
        patrolMove = new PatrolMove(this, gameBoard.getPacMan(), gameBoard, patrolPath);
    }

    private List<Node> setPatrolPath(){
        List<Node> patrolPath = new ArrayList<>();

        // Define patrol path coordinates
        // Example path coordinates assuming a grid where each step is 1 unit
        patrolPath.add(new Node(1, 1)); // Starting point
        patrolPath.add(new Node(5, 1)); // Move to the right
        patrolPath.add(new Node(5, 5)); // Move down
        patrolPath.add(new Node(1, 5)); // Move to the left
        patrolPath.add(new Node(1, 1)); // Move up to complete the loop

        // Return the constructed path
        return patrolPath;
    }
    @Override
    public void chase() {
        patrolMove.move();
    }
}

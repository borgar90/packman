package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.map.GameBoard;
import com.gruppe2.utils.Node;
import com.gruppe2.gameCharacters.pacman.PacMan;

import java.util.List;

/**
 * @Forfattere: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Bruk: denne klassen representerer flukten til spøkelsene. Den inneholder metoder for å flykte fra pacman.
 */
public class Flee {
    private Ghost ghost;
    private GameBoard gb;
    public Flee( Ghost ghost, GameBoard gb ){
        this.ghost = ghost;
        this.gb = gb;
    }
    public void fleeFromPacman(){
        flee(ghost);
    }

    private void flee(Ghost ghost){
        ghost.setSpeed(2);

        Node currentNode = new Node((int)ghost.getX(), (int)ghost.getY());
        List<Node> neighbors = ghost.getNeighbors(currentNode);

        PacMan pacMan = gb.getPacMan();
        Node furthestNeighbor = null;

        double maxDistance = -1;

        for (Node neighbor : neighbors) {
            double distance = Math.sqrt(Math.pow(pacMan.getX() - neighbor.getX(), 2) + Math.pow(pacMan.getY() - neighbor.getY(), 2));
            if (distance > maxDistance) {
                maxDistance = distance;
                furthestNeighbor = neighbor;
            }
        }

        if (furthestNeighbor != null) {
            ghost.move(furthestNeighbor.getX(), furthestNeighbor.getY());
        }
    }





}

package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.utils.Node;
import com.gruppe2.gameCharacters.pacman.PacMan;


import java.util.*;
/**
 * @Author Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @usage Denne klassen er en del av spøkelsesfunksjonaliteten i spillet.
 * Den inneholder funksjonalitet for å bevege spøkelset
 * i hennold til homing missile strategi.
 *
 */
public class MoveHoming implements Move{
    private Ghost ghost;
    private PacMan pacMan;
    private Node currentPath;

    public MoveHoming( Ghost ghost, PacMan pacMan) {
        this.ghost = ghost;
        this.pacMan = pacMan;
        this.currentPath = new Node((int) ghost.getX(), (int) ghost.getY());
    }

    @Override
    public void move() {
        homing();
    }

    private void homing() {
        // Finn stien til målet
        List<Node> path = findPath();
        if (!path.isEmpty() && path.size() > 1) { // Sjekker at det finnes et neste steg
            Node nextStep = path.get(1); // Henter neste steg i stien
            currentPath = nextStep; // Oppdaterer nåværende posisjon til neste steg
            double targetX = nextStep.getX(); // Mål X-posisjon
            double targetY = nextStep.getY(); // Mål Y-posisjon
            ghost.move(targetX, targetY); // Flytter spøkelset mot målet

        } else {
            // Hvis det ikke er flere steg, bytt retning basert på nåværende posisjon
            switch (currentPath.getX() + "," + currentPath.getY()) {
                case "0,1" -> ghost.move(0, -1); // Ned
                case "0,-1" -> ghost.move(0, 1); // Opp
                case "-1,0" -> ghost.move(1, 0); // Venstre
                case "1,0" -> ghost.move(-1, 0); // Høyre
                default -> {
                    return; // Avslutter metoden hvis ingen retning er gitt
                }
            };

        }
    }

    private List<Node> findPath() {
        // Initialiserer åpen og lukket sett for å holde styr på noder
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Set<Node> closedSet = new HashSet<>();

        // Oppretter start- og målnoder basert på spøkelses- og Pac-Man-posisjonen
        Node startNode = new Node((int) ghost.getShape().getX(), (int) ghost.getShape().getY());
        Node targetNode = new Node((int) pacMan.getX(), (int) pacMan.getY());

        // Setter startkostnader for startnoden
        startNode.setG(0);
        startNode.setH(estimateDistance(startNode, targetNode));
        startNode.setF(startNode.getG() + startNode.getH());
        openSet.add(startNode);

        // Utforsker noder inntil målet er funnet eller det ikke er flere noder å utforske
        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll(); // Henter og fjerner noden med lavest F-kostnad

            if (currentNode.equals(targetNode)) {
                // Rekonstruerer og returnerer stien hvis målet er funnet
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode); // Legger til den nåværende noden i det lukkede settet

            // Utforsker naboene til den nåværende noden
            for (Node neighbor : ghost.getNeighbors(currentNode)) {
                // Ignorerer naboer som allerede er utforsket eller blokkert av vegger
                if (closedSet.contains(neighbor) || ghost.gm.hasWall(neighbor.getX(), neighbor.getY())) continue;
                double tentativeG = currentNode.getG() + estimateDistance(currentNode, neighbor); // Beregner G-kostnad for nabonoden
                // Oppdaterer nabonoden hvis den er mer effektiv eller ikke er utforsket
                if (!openSet.contains(neighbor) || tentativeG < neighbor.getG()) {
                    neighbor.setParent(currentNode); // Setter forelder for rekonstruksjon av stien
                    neighbor.setG(tentativeG); // Oppdaterer G-kostnad
                    neighbor.setH(estimateDistance(neighbor, targetNode)); // Beregner og oppdaterer H-kostnad
                    neighbor.setF(neighbor.getG() + neighbor.getH()); // Beregner og oppdaterer F-kostnad
                    if (!openSet.contains(neighbor)) openSet.add(neighbor); // Legger til nabonoden i det åpne settet hvis den ikke allerede er der
                }
            }
        }

        // Returnerer en tom liste hvis målet ikke kan nås
        return Collections.emptyList();
    }

    private double estimateDistance(Node a, Node b) {
        // Beregner estimert avstand mellom to noder
        double dx = Math.abs(a.getX() - b.getX()); // Absoluttverdi av differansen i X-posisjon
        double dy = Math.abs(a.getY() - b.getY()); // Absoluttverdi av differansen i Y-posisjon
        double directionBias = 0.95; // Bias for å favorisere en retning litt mer enn den andre
        // Returnerer den estimerte avstanden med retning bias
        if (dx > dy) {
            return dx + dy * directionBias;
        } else {
            return dx * directionBias + dy;
        }
    }

    private List<Node> reconstructPath(Node targetNode) {
        // Rekonstruerer stien fra målnoden ved å følge foreldrenodene
        List<Node> path = new ArrayList<>();
        for (Node node = targetNode; node != null; node = node.getParent()) {
            path.add(node); // Legger til noden i stien
        }
        Collections.reverse(path); // Reverserer stien for å starte fra startnoden
        return path; // Returnerer den rekonstruerte stien
    }

}

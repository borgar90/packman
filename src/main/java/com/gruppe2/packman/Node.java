package com.gruppe2.packman;

import java.util.Objects;

public class Node {
    private int x;
    private int y;
    private double f; // total cost
    private double g; // cost from start to current node
    private double h; // heuristic cost (estimated cost from current node to target node)
    private Node parent; // parent node in the path

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.g = Double.MAX_VALUE;
    }

    public int getX() {
        return x;
    }

    public void setX( int x ) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY( int y ) {
        this.y = y;
    }

    public double getF() {
        return f;
    }

    public void setF( double f ) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG( double g ) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH( double h ) {
        this.h = h;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent( Node parent ) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

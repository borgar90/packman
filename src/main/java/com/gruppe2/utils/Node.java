package com.gruppe2.utils;

import java.util.Objects;
/**
 * Node class for A* algorithm
 *  @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 *  @Usage; Brukes i A* algorytmen for å lage noder for stifinning
 **/


public class Node {
    private int x;
    private int y;
    private double f; // totalt kostnadd
    private double g; // konstnadd fra start til nåværende node
    private double h; // beregnet kostnadd fra nåværende node to mål node)
    private Node parent; // foreldre node i path

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


package com.example.paintio;

public class Position implements Comparable<Position> {
    public double x;
    public double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Position p = (Position) obj;
        return (p.x == this.x && p.y == this.y);
    }

    @Override
    public int compareTo(Position o) {
        return (o.x == this.x && o.y == this.y) ? 1 : -1;
    }
}
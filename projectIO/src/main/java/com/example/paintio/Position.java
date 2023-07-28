
package com.example.paintio;

public class Position implements Comparable<Position>{
    double x;
    double y;
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
    public int compareTo(Position o) {
        return (o.x == this.x && o.y == this.y)?1:-1;
    }
}

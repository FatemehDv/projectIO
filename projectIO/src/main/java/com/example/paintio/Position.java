
package com.example.paintio;

public class Position implements Comparable<Position> {
    public int x;
    public int y;

    public Position(int x, int y) {
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
        if (!(obj instanceof Position p))
            return false;
        return (p.x == this.x && p.y == this.y);
    }

    @Override
    public int compareTo(Position o) {
        return (o.x == this.x && o.y == this.y) ? 1 : -1;
    }
}
package com.example.fpoebatallanaval.models;

import java.io.Serializable;

public class Position implements Serializable {

    private static final long serialVersionUID = 1L;
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position posToCopy) {
        this.x = posToCopy.x;
        this.y = posToCopy.y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }

}

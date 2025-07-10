package com.example.fpoebatallanaval.models;

public class Rectangle {

    protected Position position;
    protected int width;
    protected int height;

    public Rectangle(Position pos, int width, int height) {
        this.position = pos;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int x, int y, int width, int height) {
        this(new Position(x, y), width, height);
    }

    public Position getPosition() { return position;}
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isPositionInside(Position targetPos) {
        return targetPos.x >= position.x && targetPos.y >= position.y && targetPos.x < position.x + width && targetPos.y < position.y + height;
    }

}
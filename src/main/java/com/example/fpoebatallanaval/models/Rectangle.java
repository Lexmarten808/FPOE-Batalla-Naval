package com.example.fpoebatallanaval.models;

/**
 * La clase Rectangle representa un rectángulo definido por una posición (esquina superior izquierda),
 * un ancho y una altura. Se puede usar para representar áreas en el tablero, como barcos o zonas de impacto.
 */
public class Rectangle {

    // Posición de la esquina superior izquierda del rectángulo
    protected Position position;

    // Ancho del rectángulo
    protected int width;

    // Altura del rectángulo
    protected int height;

    /**
     * Constructor que recibe un objeto Position para establecer la esquina superior izquierda.
     * @param pos    Posición inicial (esquina superior izquierda) del rectángulo
     * @param width  Ancho del rectángulo
     * @param height Altura del rectángulo
     */
    public Rectangle(Position pos, int width, int height) {
        this.position = pos;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor alternativo que recibe directamente las coordenadas x e y.
     * @param x      Coordenada horizontal de la posición inicial
     * @param y      Coordenada vertical de la posición inicial
     * @param width  Ancho del rectángulo
     * @param height Altura del rectángulo
     */
    public Rectangle(int x, int y, int width, int height) {
        this(new Position(x, y), width, height);
    }

    /**
     * @return La posición del rectángulo (esquina superior izquierda)
     */
    public Position getPosition() { return position;}

    /**
     * @return El ancho del rectángulo
     */
    public int getWidth() { return width; }

    /**
     * @return La altura del rectángulo
     */
    public int getHeight() { return height; }

    /**
     * Verifica si una posición dada se encuentra dentro de los límites del rectángulo.
     * @param targetPos Posición a evaluar
     * @return true si la posición está dentro del rectángulo, false en caso contrario
     */
    public boolean isPositionInside(Position targetPos) {
        return targetPos.x >= position.x &&
                targetPos.y >= position.y &&
                targetPos.x < position.x + width &&
                targetPos.y < position.y + height;
    }

}
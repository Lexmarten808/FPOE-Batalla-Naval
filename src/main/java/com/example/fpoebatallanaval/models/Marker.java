package com.example.fpoebatallanaval.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.io.Serializable;

/**
 * La clase Marker representa una celda del tablero que puede marcarse durante el juego
 * para indicar un disparo (acierto o fallo) y, si aplica, la presencia de una sección de barco.
 * Hereda de Rectangle para disponer de posición, ancho y alto.
 */
public class Marker extends Rectangle implements Serializable {

    // Identificador de versión para asegurar compatibilidad durante la serialización
    private static final long serialVersionUID = 1L;

    // Colores usados para repersentar el estado visual del marcado
    private final String HIT_COLOR = "#ad480e";        // Barco impactado
    private final String MISS_COLOR = "#040d57";      // Disparo fallido
    private final String DESTROYED_COLOR = "#570404"; // Barco completamente destruido

    private final int PADDING = 3; // Espaciado visual para dibujar el marcador

    // Determina si la celda ha sido marcada por un disparo
    private boolean showMarker;

    // Referencia al barco que ocupa esta celda (si lo hay)
    private Ship shipAtMarker;

    /**
     * Constructor que crea un marcador en la posición y tamaño especificados.
     * @param x      Coordenada horizontal de la celda
     * @param y      Coordenada vertical de la celda
     * @param width  Ancho de la celda
     * @param height Alto de la celda
     */
    public Marker(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    /**
     * Restaura el marcador a su estado original: sin marca y sin barco asociado.
     */
    public void reset() {
        showMarker = false;
        shipAtMarker = null;
    }

    /**
     * Marca esta celda. Si contiene un barco, se destruye una sección del mismo.
     */
    public void mark() {
        if (!showMarker && isShip()) {
            shipAtMarker.destroySection();
        }
        showMarker = true;
    }

    /**
     * @return true si hay un barco en esta celda, false si está vacía
     */
    public boolean isShip() { return shipAtMarker != null; }

    /**
     * @return true si ha sido marcada, false en caso contrario
     */
    public boolean isMarked() { return showMarker; }

    /**
     * Asocia un barco a esta celda.
     * @param ship Barco que ocupa esta celda
     */
    public void setAsShip(Ship ship) { this.shipAtMarker = ship; }

    /**
     * Devuelve el barco asociado a esta celda.
     * @return Referencia al barco, o null si no hay ninguno
     */
    public Ship getAssociatedShip() { return shipAtMarker; }

    /**
     * Dibuja visualmente el marcador, dependiendo del resultado del disparo:
     * - Azul oscuro si fallo
     * - Naranja si acertó
     * - Rojo oscuro si el barco ha sido destruido completamente
     * @param gc Contexto gráfico donde se dibuja el marcador
     */
    public void paint(GraphicsContext gc) {
        if (!showMarker) return;

        if (isShip()) {
            if (shipAtMarker.isDestroyed()) {
                gc.setFill(Paint.valueOf(DESTROYED_COLOR)); // Barco destruido
            } else {
                gc.setFill(Paint.valueOf(HIT_COLOR)); // Acierto
            }
        } else {
            gc.setFill(Paint.valueOf(MISS_COLOR)); // Fallo
        }
        gc.fillRect(
                position.x + PADDING + 1,
                position.y + PADDING + 1,
                width - PADDING * 2,
                height- PADDING * 2
        );
    }

}
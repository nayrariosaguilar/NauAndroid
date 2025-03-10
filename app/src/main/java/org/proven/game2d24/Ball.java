package org.proven.game2d24;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Ball class - Clase que representa una bola en el juego
 * @author Versión mejorada
 * @date 10/03/2025
 */
public class Ball {
    int x, y;                // Posición actual
    int maxX, maxY;          // Límites de la pantalla
    int radius;              // Radio de la bola
    Paint paint;             // Estilo de la bola
    int velocity;            // Velocidad de movimiento
    boolean directionX;      // Dirección horizontal (true = derecha, false = izquierda)
    boolean directionY;      // Dirección vertical (true = abajo, false = arriba)
    boolean isBullet = false; // Indica si es un proyectil disparado por la nave

    /**
     * Constructor por defecto
     */
    public Ball() {
        x = 0;
        y = 0;
        maxX = 600;
        maxY = 800;
        radius = 20;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        velocity = 10;
        directionX = true;
        directionY = true;
    }

    /**
     * Constructor con posición inicial
     * @param x Coordenada X inicial
     * @param y Coordenada Y inicial
     */
    public Ball(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor completo para balas
     * @param x Coordenada X inicial
     * @param y Coordenada Y inicial
     * @param radius Radio
     * @param velocity Velocidad
     * @param isBullet Indica si es una bala
     */
    public Ball(int x, int y, int radius, int velocity, boolean isBullet) {
        this(x, y);
        this.radius = radius;
        this.velocity = velocity;
        this.isBullet = isBullet;
        if (isBullet) {
            // Las balas siempre van hacia arriba
            this.directionY = false;
            // Color blanco para las balas
            this.paint.setColor(Color.WHITE);
        }
    }

    /**
     * Comprueba si hay colisión con otra bola
     * @param b Bola con la que comprobar colisión
     * @return true si hay colisión
     */
    public boolean collision(Ball b) {
        // Calculamos la distancia entre los centros de las bolas
        double distance = Math.sqrt(
                Math.pow(getX() - b.getX(), 2) + Math.pow(getY() - b.getY(), 2)
        );
        // Hay colisión si la distancia es menor o igual a la suma de los radios
        return distance <= (getRadius() + b.getRadius());
    }

    /**
     * Dibuja la bola en el canvas
     * @param canvas Canvas donde dibujar
     */
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(getX(), getY(), getRadius(), getPaint());
    }

    /**
     * Mueve la bola según su dirección y velocidad
     * Si es una bala, solo se mueve verticalmente hacia arriba
     * Si no, se mueve en ambas direcciones y rebota en los límites
     */
    public void move() {
        if (isBullet) {
            // Las balas solo se mueven hacia arriba
            y -= velocity;
        } else {
            // Comprobar límites horizontales
            if (x >= maxX - radius) {
                directionX = false;
                x = maxX - radius;
            } else if (x <= radius) {
                directionX = true;
                x = radius;
            }

            // Comprobar límites verticales
            if (y >= maxY - radius) {
                directionY = false;
                y = maxY - radius;
            } else if (y <= radius) {
                directionY = true;
                y = radius;
            }

            // Actualizar posición según dirección
            x += directionX ? velocity : -velocity;
            y += directionY ? velocity : -velocity;
        }
    }

    /**
     * Invierte la dirección horizontal
     */
    public void reverseDirectionX() {
        directionX = !directionX;
    }

    /**
     * Invierte la dirección vertical
     */
    public void reverseDirectionY() {
        directionY = !directionY;
    }

    // Getters y setters

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isDirectionX() {
        return directionX;
    }

    public void setDirectionX(boolean directionX) {
        this.directionX = directionX;
    }

    public boolean isDirectionY() {
        return directionY;
    }

    public void setDirectionY(boolean directionY) {
        this.directionY = directionY;
    }

    public boolean isBullet() {
        return isBullet;
    }

    public void setBullet(boolean bullet) {
        this.isBullet = bullet;
    }
}
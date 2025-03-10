package org.proven.game2d24;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Ship class - Clase que representa la nave del juego
 * @author Versión mejorada
 * @date 10/03/2025
 */
public class Ship {
    private Drawable drawable;        // Imagen de la nave
    private int x, y;                 // Posición
    private int width, height;        // Dimensiones
    private boolean movingRight;      // Dirección (true = derecha, false = izquierda)
    private int speed;                // Velocidad
    private Paint paint;              // Para dibujar si no hay imagen
    private int maxX, maxY;           // Límites de la pantalla
    private Bitmap shipImage;         // Imagen bitmap alternativa

    /**
     * Constructor con drawable
     * @param drawable Imagen de la nave (puede ser null)
     */
    public Ship(Drawable drawable) {
        this.drawable = drawable;
        this.speed = 15;
        this.movingRight = true;

        // Inicializar paint por si no hay drawable
        paint = new Paint();
        paint.setColor(Color.CYAN);

        // Si hay drawable, usamos sus dimensiones
        if (drawable != null) {
            this.width = drawable.getIntrinsicWidth();
            this.height = drawable.getIntrinsicHeight();
        } else {
            // Dimensiones por defecto
            this.width = 100;
            this.height = 50;
        }
    }

    /**
     * Constructor con bitmap
     * @param x Posición X inicial
     * @param y Posición Y inicial
     * @param width Ancho de la nave
     * @param height Alto de la nave
     * @param speed Velocidad de movimiento
     * @param context Contexto para cargar recursos
     */
    public Ship(int x, int y, int width, int height, int speed, Context context) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.movingRight = true;

        // Cargamos la imagen
        try {
            shipImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.nautransparent);
            shipImage = Bitmap.createScaledBitmap(shipImage, width, height, true);
        } catch (Exception e) {
            // Si no podemos cargar la imagen, usamos un rectángulo
            paint = new Paint();
            paint.setColor(Color.CYAN);
            System.out.println("Error cargando imagen: " + e.getMessage());
        }
    }

    /**
     * Establece los límites de la pantalla y actualiza la posición inicial
     * @param maxX Ancho de la pantalla
     * @param maxY Alto de la pantalla
     */
    public void setBounds(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        // Posición inicial: centrada horizontalmente y en la parte inferior
        x = maxX / 2 - width / 2;
        y = maxY - height - 20; // 20px de margen
    }

    /**
     * Mueve la nave automáticamente
     * Cambia de dirección al llegar a los bordes
     */
    public void move() {
        if (movingRight) {
            x += speed;
            if (x + width >= maxX) {
                x = maxX - width;
                movingRight = false;
            }
        } else {
            x -= speed;
            if (x <= 0) {
                x = 0;
                movingRight = true;
            }
        }
    }

    /**
     * Dibuja la nave en el canvas
     * @param canvas Canvas donde dibujar
     */
    public void draw(Canvas canvas) {
        if (shipImage != null) {
            // Dibujar imagen bitmap
            canvas.drawBitmap(shipImage, x, y, null);
        } else if (drawable != null) {
            // Dibujar drawable
            drawable.setBounds(x, y, x + width, y + height);
            drawable.draw(canvas);
        } else {
            // Dibujar rectángulo
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    /**
     * Comprueba si la nave colisiona con una bola
     * @param ball Bola a comprobar
     * @return true si hay colisión
     */
    public boolean collidesWith(Ball ball) {
        // Calculamos el centro de la nave
        int shipCenterX = x + (width / 2);
        int shipCenterY = y + (height / 2);

        // Calculamos la distancia entre los centros
        double distance = Math.sqrt(
                Math.pow(shipCenterX - ball.getX(), 2) +
                        Math.pow(shipCenterY - ball.getY(), 2)
        );

        // Aproximamos la nave a un círculo para la colisión
        double shipRadius = Math.min(width, height) / 2.0;

        // Hay colisión si la distancia es menor que la suma de radios
        return distance <= (shipRadius + ball.getRadius());
    }

    /**
     * Comprueba si un punto está dentro de la nave
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return true si el punto está dentro
     */
    public boolean contains(float x, float y) {
        return x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height;
    }

    /**
     * Crea una bala que sale desde el centro superior de la nave
     * @return La bala creada
     */
    public Ball shoot() {
        int bulletX = x + width / 2;
        int bulletY = y - 5; // Justo encima de la nave
        Ball bullet = new Ball(bulletX, bulletY, 15, 20, true);

        return bullet;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y;
    }
}
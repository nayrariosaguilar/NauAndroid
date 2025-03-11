package org.proven.game2d24;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    int x, y;
    int maxX, maxY;
    int radius;
    Paint paint;
    int velocity;
    boolean directionX;
    boolean directionY;
    boolean isBullet = false;


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

    public Ball(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Ball(int x, int y, int radius, int velocity, boolean isBullet) {
        this(x, y);
        this.radius = radius;
        this.velocity = velocity;
        this.isBullet = isBullet;
        if (isBullet) {
            //  balas van arriba
            this.directionY = false;
            this.paint.setColor(Color.CYAN);
        }
    }

    public boolean collision(Ball b) {
        double distance = Math.sqrt(
                Math.pow(getX() - b.getX(), 2) + Math.pow(getY() - b.getY(), 2)
        );
        return distance <= (getRadius() + b.getRadius());
    }

    public void onDraw(Canvas canvas) {
        canvas.drawCircle(getX(), getY(), getRadius(), getPaint());
    }

    public void move() {
        if (isBullet) {
            y -= velocity;
        } else {
            //  límites horizontales
            if (x >= maxX - radius) {
                directionX = false;
                x = maxX - radius;
            } else if (x <= radius) {
                directionX = true;
                x = radius;
            }

            //  límites verticales
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

    public void reverseDirectionX() {
        directionX = !directionX;
    }

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
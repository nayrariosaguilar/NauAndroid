package org.proven.game2d24;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    int x,y;  // Position donde se encuentra
    int maxX, maxY; // Límit X Y
    int radius;
    Paint paint;  // Estil objecte

    //velocidad del objeto
    int velocity;
    boolean directionX; // Horizontal
    boolean directionY; // Vertical
    boolean directionW; // left
    boolean directionZ; // right

    //limites de la pantalla????

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

    public boolean collision(Ball  b) {
        boolean ret = false;
        double D =
                Math.sqrt( Math.pow(getX() - b.getX(), 2)
                        + Math.pow(getY() - b.getY(), 2));
        if(D <= (getRadius() + b.getRadius())) {
            ret = true;
        }
        return ret;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawCircle(getX() , getY()
                          , getRadius(), getPaint());
    }
//metodo que hace avanzar la pelota a una nueva posicion
    public void move() {
        // Directions  X
        if(getX() > getMaxX() - getRadius()) {
            setDirectionX(false);
            setX(getMaxX()-getRadius());
        }
        if(getX() < getRadius()) {
            setDirectionX(true);
            setX(getRadius());
        }
        // Directions  Y
        if(getY() > getMaxY() - getRadius()) {
            setDirectionY(false);
            setY(getMaxY()-getRadius());
        }
        if(getY() < getRadius()) {
            setDirectionY(true);
            setY(getRadius());
        }
        if(isDirectionX()) {  // Right
            setX(getX() + getVelocity());
        }else{                // left
            setX(getX() - getVelocity());
        }
        if(isDirectionY()) {  // Down
            setY(getY() + getVelocity());
        }else{                // Up
            setY(getY() - getVelocity());
        }
    }



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
}

package org.proven.game2d24;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;


public class Ship {
    private Drawable drawable;
    private int x, y;
    private int width, height;
    private boolean movingRight;
    private int speed;
    private Paint paint;
    private int maxX, maxY;
    private Bitmap shipImage;

    public Ship(Drawable drawable) {
        this.drawable = drawable;
        this.speed = 15;
        this.movingRight = true;

        if (drawable != null) {
            this.width = drawable.getIntrinsicWidth();
            this.height = drawable.getIntrinsicHeight();
        } else {
            // Dimensiones
            this.width = 100;
            this.height = 50;
        }
    }
    public Ship(int x, int y, int width, int height, int speed, Context context) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.movingRight = true;

        try {
            shipImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.nau);
            shipImage = Bitmap.createScaledBitmap(shipImage, width, height, true);
        } catch (Exception e) {
            shipImage = null;
        }
    }
    public void setBounds(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        x = maxX / 2 - width / 2;
        y = maxY - height - 20;
    }

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

    public void draw(Canvas canvas) {
        if (shipImage != null) {
            canvas.drawBitmap(shipImage, x, y, null);
        } else if (drawable != null) {
            drawable.setBounds(x, y, x + width, y + height);
            drawable.draw(canvas);
        } else {
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    public boolean collidesWith(Ball ball) {
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
    public boolean contains(float x, float y) {
        return x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height;
    }


    public Ball shoot() {
        int bulletX = x + width / 2;
        int bulletY = y - 5;
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
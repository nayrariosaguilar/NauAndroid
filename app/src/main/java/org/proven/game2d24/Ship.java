package org.proven.game2d24;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ship {
    private float x, y;
    private int width = 100;
    private int height = 50;
    private boolean direction = true; // true = right
    private int velocity = 10;
    private Paint paint;
    private PilotesView view;

    //variables

    //Constructor

    public Ship(PilotesView view) {
        this.view = view;
        x = view.getWidth()/2 - width/2;
        y = view.getHeight() - height - 20;
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    public void move() {
        x += direction ? velocity : -velocity;
        if (x < 0) {
            x = 0;
            direction = true;
        } else if (x + width > view.getWidth()) {
            x = view.getWidth() - width;
            direction = false;
        }
    }
    public void onDraw(Canvas canvas) {
        move();
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
    public boolean collision(Ball ball) {
        return ball.getX() + ball.getRadius() > x &&
                ball.getX() - ball.getRadius() < x + width &&
                ball.getY() + ball.getRadius() > y;
    }

}

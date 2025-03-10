package org.proven.game2d24;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class PilotesView extends View {

    ArrayList<Ball> listBalls;
    private ArrayList<Ball> listBullets;
    private Ship ship;

    private boolean gameOver = false;
    private int score = 0;

    private Drawable shipDrawable;
   // GraficNau nau;

    // Dimensiones de la pantalla
    private int screenWidth, screenHeight;

    // Random para generar valores aleatorios
    private Random random = new Random();

    public PilotesView(Context context) {
        super(context);
        initGame(context);
    }

    public PilotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGame(context);
        setOnTouchListener(new OnTouchListener() {
           OnTouchListener listener;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

//                    // Comprovar si s'ha tocat la nau o no
//                    if (x >= nau.getPosX() && x <= nau.getPosX() + drawableNau.getIntrinsicWidth() &&
//                            y >= nau.getPosY() && y <= nau.getPosY() + drawableNau.getIntrinsicHeight()) {
//                        // Disparar bala si se toca la nave
//                        shootBall();
//                        return true;
//                    } else {
//                        // Crear bola si no se toca la nave
//                        randomBall(x, y);
//                        return true;
//                    }
                }
                return false;
            }
        });
    }
    private void initGame(Context context) {
        listBalls = new ArrayList<>();
        listBullets = new ArrayList<>();
        gameOver = false;
        score = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        for(Ball b: listBalls) {
            b.setMaxX(w);
            b.setMaxY(h);
        }
        initBalls();
    }
    int randomColorBackPilota() {
        int red = (int) (Math.random() * 2);
        int green = (int) (Math.random() * 2);
        int blue = (int) (Math.random() * 2);
        return Color.rgb(red, green, blue);
    }

    private void initBalls() {
        int numBalls = random.nextInt(6) + 3;

        for (int i = 0; i < numBalls; i++) {
            int x = random.nextInt(screenWidth);
            int y = random.nextInt(screenHeight);
            createRandomBall(x,y);
        }
    }
    private void createRandomBall(int x, int y) {

        Ball ball = new Ball();
        int radius = random.nextInt(41) + 30;
        ball.setRadius(radius);
        ball.setRadius((int) ((Math.random() + 0.35) * 100));
        ball.setVelocity(15);
        ball.setDirectionX(random.nextBoolean());
        ball.setDirectionY(random.nextBoolean());
        ball.setMaxX(screenWidth);
        ball.setMaxY(screenHeight);

        Paint paint = new Paint();
        paint.setColor(randomColorBackPilota());
        ball.setPaint(paint);
        listBalls.add(ball);
    }

    public void move() {
        for(Ball b: listBalls) {
            b.move();
        }
    }

    public void collisions() {
        for (int i = 0; i < listBalls.size() - 1; i++) {
            Ball b1 = listBalls.get(i);
            for (int j = i + 1; j < listBalls.size(); j++) {
                Ball b2 = listBalls.get(j);
                if (b1.collision(b2)) {
                    if (b1.getPaint().getColor() == b2.getPaint().getColor()) {
                        listBalls.remove(b1);
                        listBalls.remove(b2); // j-1 porque al eliminar b1, el índice de b2 se reduce
                        return; // Salimos para evitar problemas con los índices
                    } else {
                        b1.setDirectionX(!b1.isDirectionX());
                        b1.setDirectionY(!b1.isDirectionY());
                        b2.setDirectionX(!b2.isDirectionX());
                        b2.setDirectionY(!b2.isDirectionY());
                    }
                }
            }
        }
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for(Ball b: listBalls) {
            b.onDraw(canvas);
        }
    }
}

package org.proven.game2d24;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PilotesView extends View {
    private ArrayList<Ball> balls;
    private ArrayList<Ball> bullets;
    private Ship ship;

    private int score = 0;
    private boolean gameOver = false;
    private boolean victory = false;

    private Paint scorePaint;
    private Drawable shipDrawable;

    private int screenWidth, screenHeight;

    private Random random = new Random();

    private static final int[] COLOR_PALETTE = {
            Color.WHITE,
            Color.RED,
            Color.BLUE,
            Color.YELLOW
    };

    public PilotesView(Context context) {
        super(context);
        initGame(context);
    }

    public PilotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGame(context);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gameOver) return false;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();

                    if (ship != null && ship.contains(x, y)) {

                        shootBullet();
                        return true;
                    } else {
                        createRandomBall((int) x, (int) y);
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void initGame(Context context) {
        balls = new ArrayList<>();
        bullets = new ArrayList<>();
        gameOver = false;
        victory = false;
        score = 0;
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(50);

        try {
            shipDrawable = context.getResources().getDrawable(R.drawable.nau, context.getTheme());
            ship = new Ship(shipDrawable);
        } catch (Exception e) {
            System.out.println("Error aqui" + e.getMessage());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;

        for (Ball b : balls) {
            b.setMaxX(w);
            b.setMaxY(h);
        }

        if (ship != null) {
            ship.setBounds(w, h);
        }

        if (balls.isEmpty()) {
            initBalls();
        }
    }

    private void initBalls() {
        int numBalls = random.nextInt(5) + 5;

        for (int i = 0; i < numBalls; i++) {
            int x = random.nextInt(screenWidth - 100) + 50;
            int y = random.nextInt(screenHeight / 3) + 50;
            createRandomBall(x, y);
        }
    }

    private void createRandomBall(int x, int y) {

        Ball ball = new Ball(x, y);
        int radius = random.nextInt(41) + 30;
        ball.setRadius(radius);
        ball.setVelocity(random.nextInt(11) + 5);
        ball.setDirectionX(random.nextBoolean());
        ball.setDirectionY(random.nextBoolean());
        //  límites
        ball.setMaxX(screenWidth);
        ball.setMaxY(screenHeight);
        Paint paint = new Paint();
        paint.setColor(COLOR_PALETTE[random.nextInt(COLOR_PALETTE.length)]);
        ball.setPaint(paint);
        balls.add(ball);
    }
    //dddd

    private void shootBullet() {
        if (ship != null) {
            Ball bullet = ship.shoot();
            bullet.setMaxX(screenWidth);
            bullet.setMaxY(screenHeight);
            bullets.add(bullet);
        }
    }

    public void update() {
        if (gameOver) return;

        // Mover bolas
        for (Ball b : balls) {
            b.move();
        }

        Iterator<Ball> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Ball bullet = bulletIterator.next();
            bullet.move();
            if (bullet.getY() < 0) {
                bulletIterator.remove();
            }
        }

        // Mover nave
        if (ship != null) {
            ship.move();
        }
    }


    public void checkCollisions() {
        if (gameOver) return;

        checkBallCollisions();
        checkBulletCollisions();
        checkShipCollisions();

        if (balls.isEmpty()) {
            gameOver = true;
            victory = true;
            showGameOverDialog();
        }
    }

    private void checkBallCollisions() {
        for (int i = 0; i < balls.size() - 1; i++) {
            Ball b1 = balls.get(i);
            for (int j = i + 1; j < balls.size(); j++) {
                Ball b2 = balls.get(j);
                if (b1.collision(b2)) {
                    if (b1.getPaint().getColor() == b2.getPaint().getColor()) {
                        // si son del mismo color, fuera
                        balls.remove(j);
                        balls.remove(i);
                        return;
                    } else {
                        b1.reverseDirectionX();
                        b1.reverseDirectionY();
                        b2.reverseDirectionX();
                        b2.reverseDirectionY();
                    }
                }
            }
        }
    }

    private void checkBulletCollisions() {
        Iterator<Ball> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Ball bullet = bulletIterator.next();
            Iterator<Ball> ballIterator = balls.iterator();

            boolean hit = false;
            while (ballIterator.hasNext() && !hit) {
                Ball ball = ballIterator.next();
                if (bullet.collision(ball)) {
                    ballIterator.remove();
                    hit = true;
                    score++;
                }
            }

            if (hit) {
                bulletIterator.remove();
            }
        }
    }

    private void checkShipCollisions() {
        if (ship != null) {
            for (Ball ball : balls) {
                if (ship.collidesWith(ball)) {
                    gameOver = true;
                    victory = false;
                    showGameOverDialog();
                    return;
                }
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (Ball ball : balls) {
            ball.onDraw(canvas);
        }

        for (Ball bullet : bullets) {
            bullet.onDraw(canvas);
        }

        if (ship != null) {
            ship.draw(canvas);
        }

        canvas.drawText("Puntuación: " + score, 400, 60, scorePaint);
    }

    private void showGameOverDialog() {
        post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(victory ? "Tu ganaste" : "Perdiste")
                        .setMessage("Puntuación: " + score + "\n¿Quieres jugar de nuevo?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetGame();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getContext() instanceof Activity) {
                                    ((Activity) getContext()).finish();
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    public void resetGame() {
        balls.clear();
        bullets.clear();
        score = 0;
        gameOver = false;
        victory = false;

        if (screenWidth > 0 && screenHeight > 0) {
            initBalls();
        }
        invalidate();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}

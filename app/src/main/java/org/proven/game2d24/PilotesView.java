package org.proven.game2d24;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Iterator;
import java.util.Random;

/**
 * GameView class - Vista principal del juego
 * @author Versión mejorada
 * @date 10/03/2025
 */
public class PilotesView extends View {
    private ArrayList<Ball> balls;        // Bolas normales
    private ArrayList<Ball> bullets;      // Balas/proyectiles
    private Ship ship;                    // Nave

    private int score = 0;                // Puntuación
    private boolean gameOver = false;     // Indica si el juego ha terminado
    private boolean victory = false;      // Indica si ha ganado

    private Paint scorePaint;             // Para dibujar la puntuación
    private Drawable shipDrawable;        // Imagen de la nave

    private int screenWidth, screenHeight;

    private Random random = new Random();

    private static final int[] COLOR_PALETTE = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW
    };

    /**
     * Constructor básico
     * @param context Contexto
     */
    public PilotesView(Context context) {
        super(context);
        initGame(context);
    }

    public PilotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGame(context);

        // Configurar listener para toques en la pantalla
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gameOver) return false;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();

                    // Comprobar si ha tocado la nave
                    if (ship != null && ship.contains(x, y)) {
                        // Disparar
                        shootBullet();
                        return true;
                    } else {
                        // Crear una bola nueva donde ha tocado
                        createRandomBall((int)x, (int)y);
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void initGame(Context context) {
        // Inicializar colecciones
        balls = new ArrayList<>();
        bullets = new ArrayList<>();

        // Inicializar estado
        gameOver = false;
        victory = false;
        score = 0;

        // Configurar paint para la puntuación
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(50);

        // Crear la nave
        try {
            shipDrawable = context.getResources().getDrawable(R.drawable.nau, context.getTheme());
            ship = new Ship(shipDrawable);
        } catch (Exception e) {
            // Si no se puede cargar el drawable, crear la nave sin imagen
            ship = new Ship(300, 800, 100, 50, 15, context);
            System.out.println("Error cargando drawable: " + e.getMessage());
        }
    }

    /**
     * Se llama cuando cambia el tamaño de la vista
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;

        // Actualizar límites para todas las bolas
        for (Ball b : balls) {
            b.setMaxX(w);
            b.setMaxY(h);
        }

        // Configurar límites de la nave
        if (ship != null) {
            ship.setBounds(w, h);
        }

        // Inicializar bolas si no hay ninguna
        if (balls.isEmpty()) {
            initBalls();
        }
    }

    /**
     * Crea bolas iniciales aleatorias
     */
    private void initBalls() {
        int numBalls = random.nextInt(5) + 5; // 5 a 10 bolas

        for (int i = 0; i < numBalls; i++) {
            // Distribuir las bolas por la parte superior de la pantalla
            int x = random.nextInt(screenWidth - 100) + 50;
            int y = random.nextInt(screenHeight / 3) + 50;
            createRandomBall(x, y);
        }
    }

    /**
     * Crea una bola con propiedades aleatorias en la posición especificada
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    private void createRandomBall(int x, int y) {
        Ball ball = new Ball(x, y);

        // Radio aleatorio entre 30 y 70
        int radius = random.nextInt(41) + 30;
        ball.setRadius(radius);

        // Velocidad aleatoria entre 5 y 15
        ball.setVelocity(random.nextInt(11) + 5);

        // Dirección aleatoria
        ball.setDirectionX(random.nextBoolean());
        ball.setDirectionY(random.nextBoolean());

        // Establecer límites
        ball.setMaxX(screenWidth);
        ball.setMaxY(screenHeight);

        // Color aleatorio de la paleta
        Paint paint = new Paint();
        paint.setColor(COLOR_PALETTE[random.nextInt(COLOR_PALETTE.length)]);
        ball.setPaint(paint);

        balls.add(ball);
    }

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
                        // Mismo color: eliminar ambas
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
                    // Eliminar la bola y la bala
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

        canvas.drawText("Score: " + score, 20, 60, scorePaint);

        if (gameOver) {
            Paint messagePaint = new Paint();
            messagePaint.setTextSize(70);
            messagePaint.setColor(victory ? Color.GREEN : Color.RED);

            String message = victory ? "¡VICTORIA!" : "GAME OVER";
            float textWidth = messagePaint.measureText(message);

            canvas.drawText(message,
                    (screenWidth - textWidth) / 2,
                    screenHeight / 2,
                    messagePaint);
        }
    }

    private void showGameOverDialog() {
        post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(victory ? "¡Victoria!" : "Game Over")
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
package edu.csub.startracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * @author Charles Bonoan
 * @version 1.0
 * Allows us to draw onto a canvas for app
 */
public class GameView extends SurfaceView implements Runnable {

    private final Background background1;
    private final Background background2;
    private HighScore highScore = HighScore.getInstance();
    private Thread thread;
    private boolean isPlaying = true;
    int touchX, touchY;
    private ArrayList<Laser> lasers;
    private ArrayList<GameObject> enemies;
    private GameActivity gameActivity;

    private final Player player;

    private EnemySpawner spawner;
    private int laserDamage = 25; // If want to create power-up that makes lasers stronger, then we can increase this value
    private final float screenWidth, screenHeight;
    private Paint textPaint = new Paint();
    private Paint highScorePaint = new Paint();

    private final MediaPlayer song;

    public GameView(GameActivity context, int screenX, int screenY) {
        super(context);

        Resources res = getResources();

        screenWidth = res.getDisplayMetrics().widthPixels;
        screenHeight = res.getDisplayMetrics().heightPixels;

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(screenWidth * 0.1f);
        highScorePaint.setColor(Color.WHITE);
        highScorePaint.setTextSize(screenWidth * 0.035f);

        // Set background
        background1 = new Background(screenX, screenY, res);
        background2 = new Background(screenX, screenY, res);
        background2.setY(screenY);

        //Create player
        player = new Player(res);

        // Create array list that will hold enemies
        spawner = new EnemySpawner(res);

        lasers = player.getLasers();
        enemies = spawner.getEnemies();

        gameActivity = context;

        song = MediaPlayer.create(context, R.raw.timemachine);
        song.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = (int) event.getX();
        touchY = (int)event.getY();
        return true;
    }

    @Override
    public void run() {
        while(isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        background1.update();
        background2.update();

        player.updateTouch(touchX, touchY);
        player.update();

        spawner.update();

        checkAllCollisions();
        checkEnemiesOffScreen();
    }

    private void checkEnemiesOffScreen() {
        for(GameObject go : enemies) {
            if(go.getY() > screenHeight) {
                player.takeDamage(100);
                go.takeDamage(100);
                gameActivity.gameOver();
            }
        }
    }

    private void checkAllCollisions() {
        // Check if laser collided with enemy
        for(Laser laser : lasers) {
            for(GameObject go : enemies) {
                if(checkCollision(laser, go)) {
                    laser.takeDamage(100);
                    go.takeDamage(laserDamage);
                    highScore.addScore(25);
                }
            }
        }

        //Check if player collides with enemy
        for(GameObject go : enemies) {
            if(checkCollision(player, go)) {
                player.takeDamage(100);
                go.takeDamage(100);
                gameActivity.gameOver();
            }
        }
    }

    private boolean checkCollision(GameObject g1, GameObject g2) {
        return g1.getX() < g2.getX() + g2.getWidth() &&
                g1.getX() + g1.getWidth() > g2.getX() &&
                g1.getY() < g2.getY() + g2.getHeight() &&
                g1.getY() + g1.getHeight() > g2.getY();
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            background1.draw(canvas);
            background2.draw(canvas);

            // Check if player alive to tell user Game Over or not
            if(!player.isAlive()) {
                canvas.drawText("GAME OVER", screenWidth/4f, screenHeight/2f, textPaint);
            }

            canvas.drawText(String.format("Score: %s", highScore.getCurScore()),
                    screenWidth * 0.04f, screenHeight * 0.06f, highScorePaint);

            player.draw(canvas);

            spawner.draw(canvas);

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    /**
     * To achieve 60 frames, we need the thread
     * to sleep for 17 ms
     */
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        isPlaying = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume() {
        isPlaying = true;

        thread = new Thread(this);
        thread.start();
    }
}

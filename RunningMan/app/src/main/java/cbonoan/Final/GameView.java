package cbonoan.Final;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * GameView is a thread that is responsible for updating and drawing the objects on screen, as well
 * as sleeping so that app can run at 60 frames.
 * @author Charles Bonoan
 * @version 1.0
 */
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;

    private GameActivity gameActivity;
    private PauseMenu pauseMenu;

    private GameOver gameOver1, gameOver2; // Two different bitmaps to create flashing effect
    private Background background1, background2;
    private Terrain terrain1, terrain2;
    private Bitmap pauseBtn;
    private Paint pausePaint;
    private RectF pauseRect; // Use to enclose the top right corner of screen where button will be
    private int backgroundTicks = 0;
    private int startTick = 50;
    private int gameOverTick = 0;

    private float screenWidth, screenHeight;

    private HighScore highScore = HighScore.getInstance();
    private Paint textPaint = new Paint();
    private Paint highScorePaint = new Paint();
    private int scoreTick = 0;
    private int addScoreTick = 3; // Set to lower value to give illusion of faster running
    private boolean startScore = false;

    private Player player;

    private SoundEffects soundEffects;

    private EnemySpawner enemySpawner;
    private ArrayList<GameObject> enemies;

    private boolean gamePlaying = true;
    private boolean isPaused = false;

    /**
     * Constructor for game view
     * This constructor will initialize the following:
     * pause menu
     * background
     * terrain
     * paint for the score and highscore
     * player
     * enemy spawner
     * game over bitmap
     * @param context current state of application
     * @param screenX the width of screen
     * @param screenY the height of screen
     */
    public GameView(GameActivity context, int screenX, int screenY) {
        super(context);

        Resources res = getResources();
        this.gameActivity = context;
        pauseMenu = new PauseMenu(gameActivity, res);

        soundEffects = new SoundEffects(context);

        screenWidth = res.getDisplayMetrics().widthPixels;
        screenHeight = res.getDisplayMetrics().heightPixels;

        background1 = new Background(res, screenX, screenY);
        background2 = new Background(res, screenX, screenY);
        background1.setX(0);
        background2.setX(screenX);
        terrain1 = new Terrain(res, screenX, screenY);
        terrain2 = new Terrain(res, screenX, screenY);
        terrain1.setX(0);
        terrain2.setX(screenX);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(screenWidth * 0.1f);
        highScorePaint.setColor(Color.WHITE);
        highScorePaint.setTextSize(screenWidth * 0.035f);

        // Create two temp Bitmap variables to pass into the GameOver class
        Bitmap gameOverFont1 = BitmapFactory.decodeResource(res, R.mipmap.gameoveryellow);
        Bitmap gameOverFont2 = BitmapFactory.decodeResource(res, R.mipmap.gameoverwhite);
        gameOver1 = new GameOver(res, gameOverFont1);
        gameOver2 = new GameOver(res, gameOverFont2);

        player = new Player(this, res);

        enemySpawner = new EnemySpawner(res);
        enemies = enemySpawner.getEnemies();

        pauseBtn = BitmapFactory.decodeResource(res, R.mipmap.pausebtn);
        pausePaint = new Paint();
        pauseRect = new RectF(screenWidth*0.85f, 0, screenWidth, screenHeight*0.1f);
    }

    private void checkCollisions() {
        //Check if player collides with enemy
        // If player collides with enemy, call the gameOver function in both player class and
        // GameActivity class
        for(GameObject go : enemies) {
            if(checkCollision(player, go)) {
                player.gameOver();
                gameActivity.gameOver();
            }
        }
    }

    /**
     * Checks for collision
     * Code reused from StarTracker assignment
     * @param g1
     * @param g2
     * @return
     */
    private boolean checkCollision(GameObject g1, GameObject g2) {
        return g1.getX() < g2.getX() + g2.getWidth() &&
                g1.getX() + g1.getWidth() > g2.getX() &&
                g1.getY() < g2.getY() + g2.getHeight() &&
                g1.getY() + g1.getHeight() > g2.getY();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int jumps = 0;
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN && player.isAlive()) {
            // Since pause bitmap is not a button, we will determine if the user presses it by getting
            // the x and y coords of the touch event
            // If user pauses, we are going to pause this activity and start the pause activity
            if(x > pauseRect.left && x <= pauseRect.right &&
            y < pauseRect.bottom && y >= pauseRect.top) {
                Log.d("Pause", "User paused");
                gameActivity.pauseGame();
            } else {
                jumps = player.actionJump();
                if (jumps < 2)
                    soundEffects.playSound("JUMP");
                else if (jumps == 2)
                    soundEffects.playSound("DOUBLEJUMP");
            }
        }
        return true;
    }

    @Override
    public void run() {
        while(gamePlaying) {
            update();
            draw();
            sleep();
        }
    }

    /**
     * Will call update function for all game objects as well as call the functions that check for
     * collision between enemies and player
     * Function will also update the player score
     */
    private void update() {
        if(player.isAlive()) {
            backgroundTicks++;
            if (backgroundTicks >= startTick) {
                terrain1.update();
                terrain2.update();
            }
            background1.update();
            background2.update();

            player.update();
            enemySpawner.update();

            // Ensures that score is not incremented until player is on screen
            if (backgroundTicks >= startTick + 30) {
                startScore = true;
            }
            // For scoring, we add 1 to the current score if scoreTick is greater than 10
            // Start adding to score once startScore is true, meaning player is on screen
            // Then we reset scoreTick
            // Score will be shown as distance ran in meters
            if (scoreTick > addScoreTick && startScore && player.isAlive()) {
                highScore.addScore(1);
                scoreTick = 0;
            }
            scoreTick++;

            checkCollisions();
        }
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            background1.draw(canvas);
            background2.draw(canvas);

            terrain1.draw(canvas);
            terrain2.draw(canvas);

            player.draw(canvas);

            enemySpawner.draw(canvas);
            // Draw score
            canvas.drawText(String.format("%s M", highScore.getCurScore()),
                    screenWidth * 0.1f, screenHeight * 0.1f, highScorePaint);

            // Draw game over screen if player is dead
            // Creating flashing effect by using a tick and switching between the gameOver objects
            if(!player.isAlive()) {
                if(gameOverTick % 2 == 0) {
                    gameOver1.draw(canvas);
                } else  {
                    gameOver2.draw(canvas);
                }
                gameOverTick++;
            }

            canvas.drawBitmap(pauseBtn, screenWidth * 0.9f, screenHeight * 0.03f, pausePaint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * In order to avoid having to create multiple objects of SoundEffects in multiple files,
     * I created functions that will be called from the class needing the sound and play the sound
     * from this GameView class
     */
    public void playLandingSound() {
        soundEffects.playSound("LANDING");
    }

    public void startFootSteps() {
        soundEffects.startFootSteps();
    }

    public void pauseFootSteps() {
        soundEffects.pauseFootSteps();
    }

    public void resumeFootSteps() {
        soundEffects.resumeFootSteps();
    }

    public void playGameOverSound() {
        soundEffects.playSound("GAMEOVER");
    }

    /**
     * In order to achieve 60 frames, set the thread to sleep for 17ms
     * Code reused from StarTracker assignment
     */
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function will stop the loop inside run() from executing and join threads
     */
    public void pause() {
        gamePlaying = false;

        soundEffects.pauseAllSounds();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        gamePlaying = true;

        soundEffects.resumeAllSounds();

        thread = new Thread(this);
        thread.start();
    }

    public SoundEffects getSoundEffects() {
        return soundEffects;
    }

    public int getCurScore() {
        return highScore.getCurScore();
    }

    public void setCurScore(int score) {
        highScore.setCurScore(score);
    }

}

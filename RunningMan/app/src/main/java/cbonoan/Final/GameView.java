package cbonoan.Final;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * GameView is a thread that is responsible for updating and drawing the objects on screen, as well
 * as sleeping so that app can run at 60 frames.
 * @author Charles Bonoan
 * @version 1.0
 */
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;

    private Background background1, background2;
    private Terrain terrain1, terrain2;
    private boolean startBackground = false;
    private int backgroundTicks = 0;
    private int startTick = 50;

    private float screenWidth, screenHeight;

    private HighScore highScore = HighScore.getInstance();
    private Paint textPaint = new Paint();
    private Paint highScorePaint = new Paint();
    private int scoreTick = 0;
    private int addScoreTick = 5; // Set to lower value to give illusion of faster running
    private boolean startScore = false;

    private Player player;
    private EnemyBird bird;

    boolean gamePlaying = true;

    /**
     * Constructor for game view
     * @param context current state of application
     * @param screenX the width of screen
     * @param screenY the height of screen
     */
    public GameView(GameActivity context, int screenX, int screenY) {
        super(context);

        Resources res = getResources();

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

        player = new Player(res);
        bird = new EnemyBird(res);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.actionJump();
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

    private void update() {
        backgroundTicks++;
        if(backgroundTicks >= startTick) {
            terrain1.update();
            terrain2.update();
        }
        background1.update();
        background2.update();

        player.update();
        bird.update();
        // Ensures that score is not incremented until player is on screen
        if(backgroundTicks >= startTick+30) {
            startScore = true;
        }
        // For scoring, we add 1 to the current score if scoreTick is greater than 10
        // Start adding to score once startScore is true, meaning player is on screen
        // Then we reset scoreTick
        // Score will be shown as distance ran in meters
        if(scoreTick > addScoreTick && startScore) {
            highScore.addScore(1);
            scoreTick = 0;
        }
        scoreTick++;
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            background1.draw(canvas);
            background2.draw(canvas);

            terrain1.draw(canvas);
            terrain2.draw(canvas);

            player.draw(canvas);
            bird.draw(canvas);

            // Draw score
            canvas.drawText(String.format("%s M", highScore.getCurScore()),
                    screenWidth * 0.04f, screenHeight * 0.06f, highScorePaint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * In order to achieve 60 frames, set the thread to sleep for
     * 17 ms
     */
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        gamePlaying = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume() {
        gamePlaying = true;

        thread = new Thread(this);
        thread.start();
    }
}

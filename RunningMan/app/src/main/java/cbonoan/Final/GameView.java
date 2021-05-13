package cbonoan.Final;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

    private Player player;

    boolean gamePlaying = true;


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

        player = new Player(res);
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
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            background1.draw(canvas);
            background2.draw(canvas);

            terrain1.draw(canvas);
            terrain2.draw(canvas);

            player.draw(canvas);

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

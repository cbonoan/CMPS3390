package cbonoan.Final;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

/**
 * GameView is a thread that is responsible for updating and drawing the objects on screen, as well
 * as sleeping so that app can run at 60 frames.
 * @author Charles Bonoan
 * @version 1.0
 */
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;

    private Background background1;
    private Background background2;
    private boolean startBackground = false;
    private int backgroundTicks = 0;
    private int startTick = 30;

    private float screenWidth, screenHeight;

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

    }

    @Override
    public void run() {
        while(gamePlaying) {
            update();
            draw();
            sleep();
        }

    }

    private void draw() {
        Log.d("Draw", "Drawing objects");
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            background1.draw(canvas);
            background2.draw(canvas);

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

    private void update() {
        backgroundTicks++;
        if(backgroundTicks >= startTick) {
            background1.update();
            background2.update();
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

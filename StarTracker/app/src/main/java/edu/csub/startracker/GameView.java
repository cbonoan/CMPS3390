package edu.csub.startracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * @author Charles Bonoan
 * @version 1.0
 * Allows us to draw onto a canvas for app
 */
public class GameView extends SurfaceView implements Runnable {

    private final Background background1;
    private final Background background2;
    private Thread thread;
    private boolean isPlaying = true;
    int touchX, touchY;

    private final Player player;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        Resources res = getResources();

        // Set background
        background1 = new Background(screenX, screenY, res);
        background2 = new Background(screenX, screenY, res);
        background2.setY(screenY);

        //Create player
        player = new Player(res);

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
        player.update(touchX, touchY);
    }
    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            background1.draw(canvas);
            background2.draw(canvas);

            player.draw(canvas);

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

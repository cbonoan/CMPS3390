package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Player class will be responsible for which frame to draw on the sprite sheet as well
 * as change the image of the player for when they jump
 * @author Charles Bonoan
 * @version 1.0
 */
public class Player implements GameObject{
    private final Resources res;
    private int screenX, screenY;
    private float dpi;

    private Bitmap runningPlayer, jumpingPlayer, doubleJump;
    private Bitmap curImage;
    private float runSpeed = 20f;
    private float x ,y;
    private int frameWidth = 175, frameHeight = 175;
    private int frameCount = 12;
    private int curFrame = 0;
    private long frameChangeTime = 0;
    private int frameTimeLength = 25;

    private Rect frameToDraw  = new Rect(0,0,frameWidth,frameHeight);
    private RectF posToDraw;
    private Paint paint = new Paint();

    //Variables needed for jumping physics
    private int velocity = 20;
    private double t = 0.0, gravity = -9.8;

    public Player(Resources res, int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.res = res;
        this.dpi = res.getDisplayMetrics().densityDpi;

        this.runningPlayer = BitmapFactory.decodeResource(res, R.mipmap.running);
        this.runningPlayer = Bitmap.createScaledBitmap(this.runningPlayer, frameWidth * frameCount,
                frameHeight, false);

        this.curImage = this.runningPlayer;

        this.x = -screenX;
        this.y = screenY * 0.74f;

        posToDraw = new RectF(x, y,x+frameWidth, y+frameHeight);
    }


    /**
     * This function how much time has passed for each frame. Variable 'time' keeps track of how
     * long a frame has been running and when that variable becomes greater than the time it took
     * for the last frame to change plus the user defined time length, then I update the frame of the
     * bitmap to the next sprite
     */
    public void manageCurFrame() {
        long time = System.currentTimeMillis();

        if(time > frameChangeTime + frameTimeLength) {
            frameChangeTime = time;
            curFrame++;

            // If the current frame is greater than the number of frames in the
            // bitmap, reset the frame count
            if(curFrame >= frameCount) {
                curFrame = 0;
            }
        }

        // frameToDraw is a rectangle so we need to update the frame it needs to enclose
        // I update left to be the leftmost bound of the frame to draw
        // Then I update the right to be the left bound + the width of the frame
        frameToDraw.left = curFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    /**
     * First the player will start off screen, then will run onto the screen until it is about
     * half way into the screen
     */
    @Override
    public void update() {
        if(this.x < this.screenX * 0.2f) {
            this.x += runSpeed;
        }

        if(this.y > screenY * 0.74f) {
            this.y = screenY * 0.74f;
            t = 0.0;
        } else {
            this.y -= velocity * t + (0.5) * gravity * t * t;
            Log.d("JUMP", "im jumping at y: " + this.y);
            t += 0.2;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        posToDraw.set((int) x, (int) y, (int)x+frameWidth, (int)y+frameHeight);
        manageCurFrame();
        canvas.drawBitmap(curImage, frameToDraw, posToDraw, paint);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public float getHealth() {
        return 0;
    }

    @Override
    public float takeDamage(float damage) {
        return 0;
    }
}

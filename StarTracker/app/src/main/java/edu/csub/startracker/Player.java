package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Charles Bonoan
 * @version 1.0
 * This class will update the image of the player based on movement
 * and also fire its lasers when needed. Lasers will also be deleted
 * in this class so memory isn't used up
 */
public class Player {

    private float x,y, prevX, prevY;
    private final Bitmap playerImg, playerLeft, playerRight;
    private Bitmap curImage;
    private Paint paint = new Paint();
    private final float dpi;
    private int frameTicks = 0, shotTicks = 0;
    private int fireRate = 15; // Speed up fire rate of player by lowering value
    private final Resources res;

    private Laser laser;
    ArrayList<Laser> lasers = new ArrayList<>();

    public Player(Resources res) {
        this.res = res;

        playerImg = BitmapFactory.decodeResource(res, R.mipmap.player);
        playerLeft = BitmapFactory.decodeResource(res, R.mipmap.player_left);
        playerRight = BitmapFactory.decodeResource(res, R.mipmap.player_right);

        curImage = playerImg;


        DisplayMetrics dm = res.getDisplayMetrics();
        dpi = dm.densityDpi;

        x = (dm.widthPixels / 2f) - (playerImg.getWidth() / 2f);
        y = (dm.heightPixels * 0.75f);


    }

    /**
     * This function will update the player position as well as
     * update the laser object
     * @param touchX tracks user touch on x axis
     * @param touchY tracks user touch on y axis
     *
     */
    public void update(float touchX, float touchY) {
        if(touchX > 0 && touchY > 0) {
            this.x = touchX - (playerImg.getWidth()/2f);
            this.y = touchY - (playerImg.getHeight() * 2f);
        }

        if(Math.abs(x - prevX) < 0.04 * dpi) {
            frameTicks++;
        } else {
            frameTicks = 0;
        }

        if(this.x < prevX - 0.04 * dpi) {
            curImage = playerLeft;
        } else if(this.x > prevX + 0.04 * dpi) {
            curImage = playerRight;
        } else if (frameTicks > 7){
            curImage = playerImg;
        }

        prevX = x;
        prevY = y;

        // Increase shotTicks
        shotTicks++;
        // See if we need to fire laser
        if(shotTicks >= fireRate) {
            // fire laser
            Laser tmp = new Laser(this.res);
            tmp.setX(x + (playerImg.getWidth() / 2f) - tmp.getMidX());
            tmp.setY(y - tmp.getHeight() /2f);

            lasers.add(tmp);

            //reset shotTicks
            shotTicks = 0;
        }

        // remove lasers that are off screen
        for(Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext();) {
            Laser laser = iterator.next();
            if(!laser.isOnScreen()) {
                iterator.remove();
            }
        }

        // update all lasers
        for(Laser laser : lasers) {
            laser.update();
        }
    }

    /**
     * Function will draw the bitmap images onto
     * the canvas object
     * @param canvas The object to draw on
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curImage, this.x, this.y, this.paint);

        // draw all lasers
        for(Laser laser : lasers) {
            laser.draw(canvas);
        }
    }
}

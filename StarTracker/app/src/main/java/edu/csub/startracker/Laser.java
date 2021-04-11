package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Charles Bonoan
 * @version 1.0
 *
 */
public class Laser {

    private float x, y;
    private Bitmap laser;
    private float dpi;
    private Paint paint = new Paint();

    public Laser(Resources res) {
        laser = BitmapFactory.decodeResource(res, R.mipmap.bullet);
        dpi = res.getDisplayMetrics().densityDpi;
    }

    public boolean isOnScreen() {
        return !(y < getHeight());
    }

    public void update() {
        y -= 0.1f * dpi;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(laser, this.x, this.y, this.paint);
    }

    public float getMidX() {
        return laser.getWidth() / 2f;
    }

    public float getHeight() {
        return laser.getHeight();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

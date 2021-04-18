package edu.csub.startracker;

import android.app.admin.FreezePeriod;
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
public class Laser implements GameObject{

    private float x, y;
    private Bitmap laser;
    private float dpi;
    private Paint paint = new Paint();
    private float health = 100f;
    private final int width, height;

    public Laser(Resources res) {
        laser = BitmapFactory.decodeResource(res, R.mipmap.bullet);
        dpi = res.getDisplayMetrics().densityDpi;

        width = laser.getWidth();
        height = laser.getHeight();
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

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean isAlive() {
        return health > 0f;
    }

    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public float takeDamage(float damage) {
        return health -= damage;
    }

    @Override
    public float addHealth(float repairAmount) {
        return health += repairAmount;
    }

    @Override
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public void setY(float y) {
        this.y = y;
    }
}

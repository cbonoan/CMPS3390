package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Enemy02 implements  GameObject{

    private float x,y, ySpeed;
    private float health = 100f;
    private final Bitmap enemy;
    private final Bitmap enemy_fast;
    private Bitmap curImage;
    private final int screenWidth, screenHeight, dpi;
    private final int width, height;
    private Paint paint = new Paint();
    private int frameTick, launchTick;

    public Enemy02(Resources res, float x, float y) {
        dpi = res.getDisplayMetrics().densityDpi;
        screenWidth = res.getDisplayMetrics().widthPixels;
        screenHeight = res.getDisplayMetrics().heightPixels;

        enemy = BitmapFactory.decodeResource(res, R.mipmap.enemy02);
        enemy_fast = BitmapFactory.decodeResource(res, R.mipmap.enemy02_fast);
        curImage = enemy;

        width = curImage.getWidth();
        height = curImage.getHeight();

        this.x = x;
        this.y = y;

        ySpeed = 0.01f * dpi;

        launchTick = new Random().nextInt(120-30) + 30;

    }


    @Override
    public void update() {
        // Start slow and wait some time
        frameTick++;

        // Switch images and go fast
        if(frameTick == launchTick) {
            curImage = enemy_fast;
            ySpeed *= 4f;
        }

        // Move enemy object on y
        y += ySpeed;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curImage, x, y, paint);

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
        return width;
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
}

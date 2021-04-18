package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy01 implements GameObject{
    private final float dpi;
    private float x,y, ySpeed;
    private final float width, height;
    private float health = 100f;
    private final Bitmap enemy, enemy_left, enemy_right;
    private Bitmap curImage;
    private int screenWidth, screenHeight;
    private Paint paint = new Paint();

    public Enemy01(Resources res, float x, float y) {
        this.x = x;
        this.y = y;

        dpi = res.getDisplayMetrics().densityDpi;
        screenHeight = res.getDisplayMetrics().heightPixels;
        screenWidth = res.getDisplayMetrics().widthPixels;

        enemy = BitmapFactory.decodeResource(res, R.mipmap.enemy01);
        enemy_left = BitmapFactory.decodeResource(res, R.mipmap.enemy01_left);
        enemy_right = BitmapFactory.decodeResource(res, R.mipmap.enemy01_right);
        curImage = enemy;
        width = curImage.getWidth();
        height = curImage.getHeight();

        ySpeed = 0.02f * dpi;
    }

    @Override
    public void update() {
        // multiplcation before the sin operation determines how wide the curves are
        // the division by y determines how long the curve takes to complete
        float xOff = (float) ( (0.01f * screenWidth) * Math.sin(y / (0.04f * screenHeight)));
        x += xOff;
        // Determine the image of enemy (left or right)
        curImage = xOff > 0 ? enemy_left : enemy_right;
        if(Math.abs(xOff) < 2.5) curImage = enemy; // If xOff is small, use the flat image of enemy
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
        return health > 0;
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

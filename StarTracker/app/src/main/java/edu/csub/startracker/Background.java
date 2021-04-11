package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Background {
    private Bitmap background;
    private int screenX, screenY;
    private Paint paint = new Paint();
    private float dpi;

    private float x = 0f, y = 0f;



    public Background(int screenX, int screenY, Resources res) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.background = BitmapFactory.decodeResource(res, R.mipmap.background);
        this.background = Bitmap.createScaledBitmap(this.background, screenX, screenY, false);
        this.dpi = res.getDisplayMetrics().densityDpi;
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

    public void update() {
        // Ensures background moves at same speed on all devices
        this.y += 0.006f * dpi;

        if(this.y > screenY) {
            this.y = -screenY;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.background, this.x, this.y, paint);
    }
}

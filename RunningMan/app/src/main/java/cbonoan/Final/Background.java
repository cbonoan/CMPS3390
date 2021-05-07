package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Background class that gives the illusion of a moving player by moving the background
 * and making sure to reset the position of background
 * @author Charles Bonoan
 * @version 1.0
 */
public class Background {
    private Bitmap background;
    private int screenX, screenY;
    private Paint paint = new Paint();
    private float dpi;
    private float x = 0f, y =  0f;

    public Background(Resources res, int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.background = BitmapFactory.decodeResource(res, R.mipmap.farbackground);
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

    /**
     * Moving the background to the left and once the one whole background
     * has passed the screen, it resets its x position to the far right
     */
    public void update() {
        this.x -= 0.01f * dpi;

        if(this.x <= -screenX) {
            this.x = screenX;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.background, this.x, this.y, paint);
    }
}

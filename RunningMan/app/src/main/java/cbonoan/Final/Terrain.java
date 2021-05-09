package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

/**
 * Terrain will have the same functionality as the background class in aspects of
 * giving the illusion of infinite terrain
 * @author Charles Bonoan
 * @version 1.0
 */
public class Terrain {
    private Bitmap terrain;
    private int screenX, screenY;
    private Paint paint = new Paint();
    private float dpi;
    private float x = 0f, y =  0f;

    private float runningSpeed = 0.025f;

    public Terrain(Resources res, int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.dpi = res.getDisplayMetrics().densityDpi;

        this.terrain = BitmapFactory.decodeResource(res, R.mipmap.terrain);
        this.terrain = Bitmap.createScaledBitmap(this.terrain, screenX, terrain.getHeight(), false);
    }


    public float getX() {
        return x;
    }

    /**
     * Set initial x position for the terrain
     * @param x value that represents the x axis
     */
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
     * Moving the terrain to the left until the whole bitmap has went across the screen to
     * then it will reset back
     */
    public void update() {
        this.x -= runningSpeed * dpi;

        if(this.x <= -screenX) {
            this.x = screenX;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.terrain, this.x, this.screenY-105, paint);
    }
}

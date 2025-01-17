package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Random;

/**
 * This class represents the bird enemy object and will contain the logic for it
 */
public class EnemyBird implements GameObject{
    Random rand = new Random();

    private final Resources res;
    private int screenX, screenY;
    private float dpi;

    private Bitmap bird;

    private float x, y, runSpeed = 15f, num1, num2;
    private int birdFrameWidth = 125, birdFrameHeight = 125;
    private int birdFrameCount = 9;
    private int birdFrameTimeLength = 10;
    private long birdChangeTime = 0;

    private Rect frameToDraw = new Rect(0,0,birdFrameWidth,birdFrameHeight);
    private RectF posToDraw;

    private Paint paint = new Paint();

    private SpriteManager spriteManager = new SpriteManager(birdFrameWidth, birdFrameHeight,
            birdFrameTimeLength, birdFrameCount);

    /**
     * Constructor for enemy bird
     * @param res
     */
    public EnemyBird(Resources res) {
        this.res = res;
        DisplayMetrics dm = res.getDisplayMetrics();
        this.dpi = dm.densityDpi;
        this.screenX = dm.widthPixels;
        this.screenY = dm.heightPixels;

        this.x = screenX * 2f;
        this.y = screenY * (0.6f + rand.nextFloat() * (0.7f - 0.6f));

        this.bird = BitmapFactory.decodeResource(res, R.mipmap.bluebird);
        this.bird = Bitmap.createScaledBitmap(this.bird, birdFrameWidth*birdFrameCount,
                birdFrameHeight, false);

        // Generate two random numbers that will be used to make the curve of the bird path
        // feel more random
        this.num1 = 0.01f + rand.nextFloat() * (0.03f - 0.01f); // Get a float from 0.01 - 0.03
        this.num2 = 0.01f + rand.nextFloat() * (0.05f - 0.01f); // 0.01 - 0.05

        this.posToDraw = new RectF(x,y,x+birdFrameWidth,y+birdFrameHeight);
    }

    /**
     * Will manage what frame to show on canvas
     */
    private void updateBird() {
        this.birdChangeTime = this.spriteManager.manageCurFrame(this.birdChangeTime);

        frameToDraw.left = spriteManager.curFrame * birdFrameWidth;
        frameToDraw.right = frameToDraw.left + birdFrameWidth;
    }

    /**
     * This function will move the bird to the left as well as update its y coordinate in a sin curve
     * behavior
     */
    @Override
    public void update() {
        // Move object to left
        if(this.x > -screenX) {
            this.x -= runSpeed;
        }

        // Need to change y to move in sin curve
        // Multiplcation before sin operation determines wideness of curve. Increasing the float
        // will create a larger curve
        // The division inside the sin determines how fast the curve is completed.
        // Code rehashed from StarTracker application
        float yOff = (float) ( (num1 * screenY) * Math.sin(x / (num2 * screenX)));
        if(this.y+yOff < screenY * 0.7f){
            this.y += yOff;
        }
    }

    /**
     * Drawing the correct frame for bird spritesheet
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        this.posToDraw.set((int) this.x, (int) this.y, (int) this.x + this.birdFrameWidth,
                (int) this.y + this.birdFrameHeight);
        updateBird();
        canvas.drawBitmap(bird, frameToDraw, posToDraw, paint);
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getWidth() {
        return this.birdFrameWidth;
    }

    @Override
    public float getHeight() {
        return this.birdFrameHeight;
    }

}

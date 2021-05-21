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

public class EnemyBird implements GameObject{
    Random rand = new Random();

    private final Resources res;
    private int screenX, screenY;
    private float dpi;

    private Bitmap bird;

    private float x, y, runSpeed = 15f;
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
        this.y = screenY * (0.3f + rand.nextFloat() * (0.5f - 0.3f));

        this.bird = BitmapFactory.decodeResource(res, R.mipmap.bluebird);
        this.bird = Bitmap.createScaledBitmap(this.bird, birdFrameWidth*birdFrameCount,
                birdFrameHeight, false);

        this.posToDraw = new RectF(x,y,x+birdFrameWidth,y+birdFrameHeight);
    }

    private void updateBird() {
        this.birdChangeTime = this.spriteManager.manageCurFrame(this.birdChangeTime);

        frameToDraw.left = spriteManager.curFrame * birdFrameWidth;
        frameToDraw.right = frameToDraw.left + birdFrameWidth;
    }

    /**
     * Update function will move the bird in a sin curve behavior
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
        // The division inside the sin determines how fast the curve is completed. Incresing the float
        // value will make the curve longer
        // Code rehashed from StarTracker application
        float yOff = (float) ( (0.02f * screenY) * Math.sin(x / (0.07f * screenX)));
        this.y+=yOff;
    }

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

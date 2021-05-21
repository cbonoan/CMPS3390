package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

/**
 * Class for turtle enemy logic
 */
public class EnemyTurtle implements GameObject{
    private final Resources res;
    private int screenX, screenY;
    private float dpi;

    private Bitmap turtle;

    private float x, y, groundLevel, runSpeed = 10f;
    private int turtleFrameWidth = 170, turtleFrameHeight = 100;
    private int turtleFrameCount = 14;
    private int turtleFrameTimeLength = 10;
    private long turtleChangeTime = 0;

    private Rect frameToDraw = new Rect(0,0,turtleFrameWidth,turtleFrameHeight);
    private RectF posToDraw;

    private Paint paint = new Paint();

    private SpriteManager turtleSpriteManager = new SpriteManager(turtleFrameWidth, turtleFrameHeight,
            turtleFrameTimeLength, turtleFrameCount);

    /**
     * Constructor for class
     * Initialize x value to be off screen when spawned
     * @param res
     */
    public EnemyTurtle(Resources res) {
        this.res = res;
        DisplayMetrics dm = res.getDisplayMetrics();
        this.dpi = dm.densityDpi;
        this.screenX = dm.widthPixels;
        this.screenY = dm.heightPixels;

        this.x = screenX * 2f;
        this.y = screenY * 0.81f;

        this.turtle = BitmapFactory.decodeResource(res, R.mipmap.turtle);
        this.turtle = Bitmap.createScaledBitmap(this.turtle, turtleFrameWidth*turtleFrameCount,
                turtleFrameHeight, false);

        this.posToDraw = new RectF(x,y,x+turtleFrameWidth,y+turtleFrameHeight);
    }

    private void updateTurtle() {
        this.turtleChangeTime = this.turtleSpriteManager.manageCurFrame(this.turtleChangeTime);

        frameToDraw.left = turtleSpriteManager.curFrame * turtleFrameWidth;
        frameToDraw.right = frameToDraw.left + turtleFrameWidth;
    }

    /**
     * Move turtle to left until off screen
     */
    @Override
    public void update() {
        if(this.x > -screenX) {
            this.x -= runSpeed;
        }
    }

    /**
     * Drawing the correct frame for turtle sprite sheet
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        this.posToDraw.set((int) this.x, (int) this.y, (int) this.x + this.turtleFrameWidth,
                (int) this.y + this.turtleFrameHeight);
        updateTurtle();
        canvas.drawBitmap(turtle, frameToDraw, posToDraw, paint);
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
        return this.turtleFrameWidth;
    }

    @Override
    public float getHeight() {
        return this.turtleFrameHeight;
    }

}

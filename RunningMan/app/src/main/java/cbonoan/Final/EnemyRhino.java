package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

public class EnemyRhino implements GameObject{
    private final Resources res;
    private int screenX, screenY;
    private float dpi;

    private Bitmap rhino;

    private float x, y, groundLevel, runSpeed = 18f;
    private int rhinoFrameWidth = 250, rhinoFrameHeight = 250;
    private int rhinoFrameCount = 6;
    private int rhinoFrameTimeLength = 13;
    private long rhinoChangeTime = 0;

    private Rect frameToDraw = new Rect(0,0,rhinoFrameWidth,rhinoFrameHeight);
    private RectF posToDraw;

    private Paint paint = new Paint();

    private SpriteManager rhinoSpriteManager = new SpriteManager(rhinoFrameWidth, rhinoFrameHeight,
            rhinoFrameTimeLength, rhinoFrameCount);

    public EnemyRhino(Resources res) {
        this.res = res;
        DisplayMetrics dm = res.getDisplayMetrics();
        this.dpi = dm.densityDpi;
        this.screenX = dm.widthPixels;
        this.screenY = dm.heightPixels;

        this.x = screenX * 2f;
        this.y = this.groundLevel = screenY * 0.675f;

        this.rhino = BitmapFactory.decodeResource(res, R.mipmap.rhinorun);
        this.rhino = Bitmap.createScaledBitmap(this.rhino, rhinoFrameWidth*rhinoFrameCount,
                rhinoFrameHeight, false);

        this.posToDraw = new RectF(x,y,x+rhinoFrameWidth,y+rhinoFrameHeight);
    }

    @Override
    public void update() {
        if(this.x > -screenX) {
            this.x -= runSpeed;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        this.posToDraw.set((int) this.x, (int) this.y, (int) this.x + this.rhinoFrameWidth,
                (int) this.y + this.rhinoFrameHeight);
        updateRhino();
        canvas.drawBitmap(rhino, frameToDraw, posToDraw, paint);
    }

    /**
     * This class will use the SpriteManager class to update the frameChangeTime as well as update
     * which frame to draw for the rhino enemy
     */
    private void updateRhino() {
        this.rhinoChangeTime = this.rhinoSpriteManager.manageCurFrame(this.rhinoChangeTime);

        frameToDraw.left = rhinoSpriteManager.curFrame * rhinoFrameWidth;
        frameToDraw.right = frameToDraw.left + rhinoFrameWidth;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public float getHealth() {
        return 0;
    }

    @Override
    public float takeDamage(float damage) {
        return 0;
    }
}

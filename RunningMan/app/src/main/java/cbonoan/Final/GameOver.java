package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * This class will handle the game over screen
 * @author Charles Bonoan
 * @version 1.0
 */
public class GameOver {
    private Bitmap gameOver;
    private int screenX, screenY;

    private Paint paint = new Paint();

    /**
     * This constructor will take in a Resources object and also a bitmap to set to the
     * gameOver variable
     * @param res
     * @param gameOverFont
     */
    public GameOver(Resources res, Bitmap gameOverFont) {
        this.screenX = res.getDisplayMetrics().widthPixels;
        this.screenY = res.getDisplayMetrics().heightPixels;

        this.gameOver = gameOverFont;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.gameOver, this.screenX * 0.4f, this.screenY/2f, paint);
    }
}

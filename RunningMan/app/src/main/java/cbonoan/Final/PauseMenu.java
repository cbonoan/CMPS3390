package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.print.PrintAttributes;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * This class will create a FrameLayout that will go over the GameView activity when the user hits
 * the pause button. A FrameLayout is great to use for blocking out an area
 * @author Charles Bonoan
 * @version 1.0
 */
public class PauseMenu {
    private int dpi;

    private ImageView paused;
    private ImageButton muteBtn, quitBtn, retryBtn, resumeBtn;

    private Bitmap pausedFont, mute, unmute, quit, retry, resume;

    private FrameLayout pauseMenu;

    /**
     * This constructor will create a frame layout programatically
     * @param context
     * @param res
     */
    public PauseMenu(GameActivity context, Resources res) {
        this.dpi = res.getDisplayMetrics().densityDpi;

        pausedFont = BitmapFactory.decodeResource(res, R.mipmap.paused);
        mute = BitmapFactory.decodeResource(res, R.mipmap.mute);
        unmute = BitmapFactory.decodeResource(res, R.mipmap.unmute);
        quit = BitmapFactory.decodeResource(res, R.mipmap.quit);
        retry = BitmapFactory.decodeResource(res, R.mipmap.retry);
        resume = BitmapFactory.decodeResource(res, R.mipmap.resume);

        // This is used to determine certain attributes of the concent inside the frame layout
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        paused = new ImageView(context);
        paused.setScaleType(ImageView.ScaleType.FIT_CENTER);
        paused.setImageBitmap(pausedFont);
        lp.setMargins(0,0,0,400);
        paused.setLayoutParams(lp);

        muteBtn = new ImageButton(context);
        muteBtn.setImageBitmap(unmute);
        lp.setMargins(0,0,0,200);
        muteBtn.setLayoutParams(lp);

        quitBtn = new ImageButton(context);
        quitBtn.setImageBitmap(quit);
        lp.setMargins(0,0,400,0);
        quitBtn.setLayoutParams(lp);

        retryBtn = new ImageButton(context);
        retryBtn.setImageBitmap(retry);
        lp.setMargins(0,0,0,0);
        retryBtn.setLayoutParams(lp);

        resumeBtn = new ImageButton(context);
        resumeBtn.setImageBitmap(resume);
        lp.setMargins(400,0,0,0);
        resumeBtn.setLayoutParams(lp);

        // Initializing frame layout
        pauseMenu = new FrameLayout(context);
        pauseMenu.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        // Add all elements to frame layout
        pauseMenu.addView(paused);
        pauseMenu.addView(muteBtn);
        pauseMenu.addView(quitBtn);
        pauseMenu.addView(retryBtn);
        pauseMenu.addView(resumeBtn);
    }

    public FrameLayout getPauseMenu() {
        return this.pauseMenu;
    }
}

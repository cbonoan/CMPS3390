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

/**
 * Player class will be responsible for which frame to draw on the sprite sheet as well
 * as change the image of the player for when they jump
 * @author Charles Bonoan
 * @version 1.0
 */
public class Player implements GameObject{
    private final Resources res;
    private int screenX, screenY;
    private float dpi;

    private Bitmap runningPlayer, jumpingPlayer, fallingPlayer, doubleJump;
    private Bitmap curImage;
    private float runSpeed = 20f;
    private float x ,y, y0, groundLevel;
    // Running sprite sheet variables
    private int runFrameWidth = 150, runFrameHeight = 150;
    private int runFrameCount = 12;
    private long runFrameChangeTime = 0;
    private int runFrameTimeLength = 23;
    // Double jumping sprite sheet variables
    private int doubleFrameWidth = 150, doubleFrameHeight = 150;
    private int doubleFrameCount = 6;
    private long doubleFrameChangeTime = 0;
    private int doubleFrameTimeLength = 12;

    private SpriteManager doubleJumpSpriteManager = new SpriteManager(doubleFrameWidth,
            doubleFrameHeight, doubleFrameTimeLength, doubleFrameCount);
    private SpriteManager runSpriteManager = new SpriteManager(runFrameWidth, runFrameHeight,
            runFrameTimeLength, runFrameCount);

    // Rectangles used to draw the running spritesheet frames
    private Rect runFrameToDraw = new Rect(0,0, runFrameWidth, runFrameHeight);
    private RectF runPosToDraw;
    // Rects used to draw the double jump spritesheet frames
    private Rect doubleFrameToDraw = new Rect(0,0,doubleFrameWidth, doubleFrameHeight);
    private RectF doublePosToDraw;
    private Paint paint = new Paint();

    //Variables needed for jumping physics
    private int velocity = 20;
    private float t = 0.0f, gravity = -7.5f;
    private boolean jump = false, doubleJumped = false;
    private int jumps = 0;

    /**
     * Constructor for player
     * Sets the bitmaps for all player animations such as running, jumping, double jumping, and
     * falling
     * Also set the initial position of player to be off screen to the left
     * @param res
     */
    public Player(Resources res) {
        this.res = res;
        DisplayMetrics dm = res.getDisplayMetrics();
        this.dpi = dm.densityDpi;
        this.screenX = dm.widthPixels;
        this.screenY = dm.heightPixels;

        this.runningPlayer = BitmapFactory.decodeResource(res, R.mipmap.running);
        this.runningPlayer = Bitmap.createScaledBitmap(this.runningPlayer, runFrameWidth * runFrameCount,
                runFrameHeight, false);

        this.jumpingPlayer = BitmapFactory.decodeResource(res, R.mipmap.singlejump);
        this.jumpingPlayer = Bitmap.createScaledBitmap(this.jumpingPlayer, runFrameWidth, runFrameHeight, false);

        this.doubleJump = BitmapFactory.decodeResource(res, R.mipmap.doublejump);
        this.doubleJump = Bitmap.createScaledBitmap(this.doubleJump, doubleFrameWidth*doubleFrameCount,
                doubleFrameHeight, false);

        this.fallingPlayer = BitmapFactory.decodeResource(res, R.mipmap.falling);
        this.fallingPlayer = Bitmap.createScaledBitmap(this.fallingPlayer, runFrameWidth, runFrameHeight, false);

        this.curImage = this.runningPlayer;

        this.x = -screenX;
        this.y = this.y0 = this.groundLevel = screenY * 0.77f;

        runPosToDraw = new RectF(x, y,x+ runFrameWidth, y+ runFrameHeight);
        doublePosToDraw = new RectF(0,0, x+doubleFrameWidth, y+doubleFrameHeight);
    }

    /**
     * Function calls Sprite Manager to manage all the player running frames
     */
    public void manageRunCurFrame() {
        runFrameChangeTime = runSpriteManager.manageCurFrame(runFrameChangeTime);

        // frameToDraw is a rectangle so we need to update the frame it needs to enclose
        // I update left to be the leftmost bound of the frame to draw
        // Then I update the right to be the left bound + the width of the frame
        runFrameToDraw.left = runSpriteManager.curFrame * runFrameWidth;
        runFrameToDraw.right = runFrameToDraw.left + runFrameWidth;
    }

    /**
     * Function calls Sprite Manager to manage all the player double jump frames
     */
    public void manageDoubleJumFrame() {
        doubleFrameChangeTime = doubleJumpSpriteManager.manageCurFrame(doubleFrameChangeTime);

        doubleFrameToDraw.left = doubleJumpSpriteManager.curFrame * doubleFrameWidth;
        doubleFrameToDraw.right = doubleFrameToDraw.left + doubleFrameWidth;
    }

    /**
     * Called in GameView when player touches the screen
     */
    public void actionJump() {
        this.jump = true;
        this.jumps++;
        if(this.jumps==2) {
            doubleJumped = true;
        }
    }

    /**
     * First the player will start off screen, then will run onto the screen until it is about
     * half way into the screen, this is when the game will start
     *
     * For the y position, the y value will not change until jump is true (when player touches screen)
     * since this function is called as long as the app is running, I treated the else if statement
     * for the y value as a while loop, and the base case is when the y value goes below a certain value
     *
     * The y value is also calculated using a physics formula when finding the height of a bouncing
     * ball
     */
    @Override
    public void update() {
        if(this.x < this.screenX * 0.2f) {
            this.x += runSpeed;
        }

        // While jump is true
        if(this.jump) {
            //Check if player double jumps and make sure player is not allowed to jump more than twice
            // If player double jumps, then reset the time variable t which will reset the arc of jump
            // then reset boolean doubleJumped back to false
            if(this.jumps == 2 && this.doubleJumped) {
                this.t = 0.0f;
                this.doubleJumped = false;
            }

            // Once player hits the ground, reset values
            if (this.y > this.groundLevel) { // See if player is below the ground
                this.y = this.y0 = this.groundLevel;
                this.t = 0.0f;
                this.jump = false;
                this.jumps = 0;
                curImage = runningPlayer;
            } else {
                this.y = (float) (this.y0 - (this.velocity * this.t + (0.5) * this.gravity * this.t * this.t));

                // Comparing y0 and y will determine whether the player is going upwards or downwards
                // This will help when determining what to set curImage to
                // (i.e jumpingPlayer or fallingPlayer)
                // This block will also see if the player double jumps and use the doubleJump bitmap
                if(this.y0 >= this.y && jumps < 2) {
                    curImage = jumpingPlayer;
                } else if(this.y0 >= this.y && jumps >= 2) {
                    curImage = doubleJump;
                } else if(this.y0 < this.y) {
                    curImage = fallingPlayer;
                }

                this.y0 = this.y;
                this.t += 0.25;
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(curImage == runningPlayer) {
            runPosToDraw.set((int) this.x, (int) this.y, (int) this.x + this.runFrameWidth,
                    (int) this.y + this.runFrameHeight);
            manageRunCurFrame();
            canvas.drawBitmap(curImage, runFrameToDraw, runPosToDraw, paint);
        } else if(curImage == doubleJump) {
            doublePosToDraw.set((int) this.x, (int) this.y, (int) this.x + this.doubleFrameWidth,
                    (int) this.y + this.doubleFrameHeight);
            manageDoubleJumFrame();
            canvas.drawBitmap(curImage, doubleFrameToDraw, doublePosToDraw, paint);
        } else {
            canvas.drawBitmap(curImage, this.x, this.y, paint);
        }
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
        return curImage.getWidth();
    }

    @Override
    public float getHeight() {
        return curImage.getHeight();
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

package cbonoan.Final;

import android.content.Context;
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
    private final GameView gameView;
    private int screenX, screenY;
    private float dpi;

    private boolean alive = true;
    private Bitmap runningPlayer, jumpingPlayer, fallingPlayer, gameOverPlayer, doubleJump;
    private Bitmap curImage;
    private float runSpeed = 23f;
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
    private int doubleFrameTimeLength = 5;
    // Player getting hit sprite sheet vars
    private int hitFrameWidth = 150, hitFrameHeight = 150;
    private int hitFrameCount = 7;
    private long hitFrameChangeTime = 0;
    private int hitFrameTimeLength = 20;

    private int width, height;

    private SpriteManager doubleJumpSpriteManager = new SpriteManager(doubleFrameWidth,
            doubleFrameHeight, doubleFrameTimeLength, doubleFrameCount);
    private SpriteManager runSpriteManager = new SpriteManager(runFrameWidth, runFrameHeight,
            runFrameTimeLength, runFrameCount);
    private SpriteManager hitSpriteManager = new SpriteManager(hitFrameWidth, hitFrameHeight,
            hitFrameTimeLength, hitFrameCount);

    // Rectangles used to draw the running spritesheet frames
    private Rect runFrameToDraw = new Rect(0,0, runFrameWidth, runFrameHeight);
    private RectF runPosToDraw;
    // Rects used to draw the double jump spritesheet frames
    private Rect doubleFrameToDraw = new Rect(0,0,doubleFrameWidth, doubleFrameHeight);
    private RectF doublePosToDraw;
    // Rects used to draw the player hit spritesheet frames
    private Rect hitFrameToDraw = new Rect(0,0,hitFrameWidth, hitFrameHeight);
    private RectF hitPosToDraw;

    private Paint paint = new Paint();

    private boolean startedFootSteps = false;

    //Variables needed for jumping physics
    private float velocity = runSpeed;
    private float t = 0.0f, gravity = -8f;
    private boolean jump = false, doubleJumped = false;
    private int jumps = 0;
    private boolean bringPlayerBack = false;

    /**
     * Constructor for player
     * Sets the bitmaps for all player animations such as running, jumping, double jumping, and
     * falling
     * Also set the initial position of player to be off screen to the left
     * @param res
     */
    public Player(GameView gameView, Resources res) {
        this.res = res;
        this.gameView = gameView;
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

        this.gameOverPlayer = BitmapFactory.decodeResource(res, R.mipmap.playergameover);
        this.gameOverPlayer = Bitmap.createScaledBitmap(this.gameOverPlayer, hitFrameWidth*hitFrameCount,
                hitFrameHeight, false);

        this.curImage = this.runningPlayer;

        this.width = this.runFrameWidth;
        this.height = this.runFrameHeight;

        this.x = -screenX;
        this.y = this.y0 = this.groundLevel = screenY * 0.77f;

        runPosToDraw = new RectF(x, y,x+ runFrameWidth, y+ runFrameHeight);
        doublePosToDraw = new RectF(0,0, x+doubleFrameWidth, y+doubleFrameHeight);
        hitPosToDraw = new RectF(x,y,x+hitFrameWidth, y+hitFrameHeight);
    }

    public boolean isAlive() {
        return alive;
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
    public void manageDoubleJumpFrame() {
        doubleFrameChangeTime = doubleJumpSpriteManager.manageCurFrame(doubleFrameChangeTime);

        doubleFrameToDraw.left = doubleJumpSpriteManager.curFrame * doubleFrameWidth;
        doubleFrameToDraw.right = doubleFrameToDraw.left + doubleFrameWidth;
    }
    private void manageHitFrame() {
        hitFrameChangeTime = hitSpriteManager.manageCurFrame(hitFrameChangeTime);

        hitFrameToDraw.left = hitSpriteManager.curFrame * hitFrameWidth;
        hitFrameToDraw.right = hitFrameToDraw.left + hitFrameWidth;
    }

    /**
     * Called in GameView when player touches the screen
     * @return return the number of jumps to determine in GameView which sound to play (single or
     * double jump sound) or not to play a sound at all
     */
    public int actionJump() {
        this.jump = true;
        this.jumps++;
        if(this.jumps==2) {
            doubleJumped = true;
        }
        this.gameView.pauseFootSteps();
        return this.jumps;
    }

    /**
     * This will set the image of player to the gameOverPlayer sprite and play the animation
     */
    public void gameOver() {
        this.gameView.playGameOverSound();
        curImage = gameOverPlayer;
    }

    /**
     * First the player will start off screen, then will run onto the screen until it is about
     * half way into the screen, this is when the game will start
     *
     * Footsteps will start playing once the player reaches the left edge of screen
     *
     * For the y position, the y value will not change until jump is true (when player touches screen)
     * since this function is called as long as the app is running, I treated the else if statement
     * for the y value as a while loop, and the base case is when the y value goes below a certain value
     *
     * The y value is also calculated using a physics formula when finding the height of a bouncing
     * ball
     *
     * This function will also need to make sure the width and height variables are set to the correct
     * sprite sheet so that collision is accurate
     */
    @Override
    public void update() {
        if(this.x < this.screenX * 0.2f) {
            this.x += runSpeed;
            bringPlayerBack = false;
        }

        // Since player moves forward on jump, we need to reset the x position after they land
        if(bringPlayerBack && !jump) {
            this.x -= 10f;
        }

        if(this.x >= 0 && !startedFootSteps) {
            this.gameView.startFootSteps();
            startedFootSteps = true;
        }

        // While jump is true
        if(this.jump) {
            this.x += 10f;
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
                this.gameView.playLandingSound();
                this.gameView.resumeFootSteps();
                bringPlayerBack = true;
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
                this.t += 0.3;
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
        // Changing the alive boolean to false here ensures that the gameOverPlayer sprite
        // animation is only played once
        if(alive) {
            if (curImage == gameOverPlayer) {
                hitPosToDraw.set((int) this.x, (int) this.y, (int) this.x + this.hitFrameWidth,
                        (int) this.y + this.hitFrameHeight);
                manageHitFrame();
                canvas.drawBitmap(curImage, hitFrameToDraw, hitPosToDraw, paint);
                alive = false;
            } else if (curImage == runningPlayer) {
                runPosToDraw.set((int) this.x, (int) this.y, (int) this.x + this.runFrameWidth,
                        (int) this.y + this.runFrameHeight);
                manageRunCurFrame();
                canvas.drawBitmap(curImage, runFrameToDraw, runPosToDraw, paint);
            } else if (curImage == doubleJump) {
                doublePosToDraw.set((int) this.x, (int) this.y, (int) this.x + this.doubleFrameWidth,
                        (int) this.y + this.doubleFrameHeight);
                manageDoubleJumpFrame();
                canvas.drawBitmap(curImage, doubleFrameToDraw, doublePosToDraw, paint);
            } else {
                canvas.drawBitmap(curImage, this.x, this.y, paint);
            }
        } else {
            canvas.drawBitmap(curImage, hitFrameToDraw, hitPosToDraw, paint);
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
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }


}

package cbonoan.Final;

/**
 * This application has a lot of sprites that use a sprite sheet, meaning there will be a lot of
 * repeated code. This class will help reduce those certain code blocks
 * @author Charles Bonoan
 * @version 1.0
 */
public class SpriteManager {
    private int frameWidth, frameHeight;
    private int frameTimeLength;
    private int frameCount;
    int curFrame = 0;


    public SpriteManager(int frameWidth, int frameHeight, int frameLengthTime, int frameCount) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameTimeLength = frameLengthTime;
        this.frameCount = frameCount;
    }

    /**
     * This function how much time has passed for each frame. Variable 'time' keeps track of how
     * long a frame has been running and when that variable becomes greater than the time it took
     * for the last frame to change plus the user defined time length, then I update the frame of the
     * bitmap to the next sprite
     */
    public long manageCurFrame(long frameChangeTime) {
        long time = System.currentTimeMillis();

        if(time > frameChangeTime + frameTimeLength) {
            frameChangeTime = time;
            curFrame++;

            if (curFrame >= frameCount) {
                curFrame = 0;
            }
        }

        return frameChangeTime;
    }
}

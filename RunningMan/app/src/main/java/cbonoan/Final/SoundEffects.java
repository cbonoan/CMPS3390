package cbonoan.Final;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

/**
 * This class will be responsible for handling the game music as well as the sound effects of certain
 * actions during gameplay
 * @author Charles Bonoan
 * @version 1.0
 */
public class SoundEffects {
    private AudioAttributes audioAttributes;
    private final int MAX = 15;

    private static int jumpSound;
    private static int doubleJumpSound;
    private static int landingSound;
    private static int footSteps;

    private static int gameMusic;
    private static int gameOverSound;

    private static SoundPool soundPool;

    /**
     * Constructor will check if the SDK version of the device is greater than Lollipop. If it is,
     * it will use AudioAttributes to initialize SoundPool since the constructor for SoundPool is
     * deprecated after version Lollipop.
     * Sounds will also be initialized here
     * @param context
     */
    public SoundEffects(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_GAME)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build();
            soundPool = new SoundPool.Builder()
                                    .setAudioAttributes(audioAttributes)
                                    .setMaxStreams(MAX)
                                    .build();
        } else {
            soundPool = new SoundPool(MAX, AudioManager.STREAM_MUSIC, 0);
        }

        footSteps = soundPool.load(context, R.raw.footstep, 1);
        jumpSound = soundPool.load(context, R.raw.jumpsound, 1);
        doubleJumpSound = soundPool.load(context, R.raw.doublejumpsound, 1);
        landingSound = soundPool.load(context, R.raw.landing, 1);

        gameOverSound = soundPool.load(context, R.raw.gameover,1);

    }

    /**
     * This function will handle which sound to play based on the action passed in
     * @param action the action that player does
     */
    public void playSound(String action) {
        int soundId = 0;
        switch (action) {
            case "JUMP":
                soundId = jumpSound;
                break;
            case "DOUBLEJUMP":
                soundId = doubleJumpSound;
                break;
            case "LANDING":
                soundId = landingSound;
                break;
            case "GAMEOVER":
                soundId = gameOverSound;
                break;
        }
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * When the player pauses the game, the sound effects will also be paused
     */
    public void pauseAllSounds() {
        soundPool.pause(jumpSound);
        soundPool.pause(doubleJumpSound);
        soundPool.pause(footSteps);
        soundPool.pause(landingSound);
    }

    /**
     * When player return to this activity, resume the sounds
     */
    public void resumeAllSounds() {
        soundPool.resume(jumpSound);
        soundPool.resume(doubleJumpSound);
        soundPool.resume(footSteps);
        soundPool.resume(landingSound);
    }

    /**
     * Since footsteps will always be repeated, I will just call this function once and the sound
     * of footsteps will be in a loop
     */
    public void startFootSteps() {
        soundPool.play(footSteps, 2.0f, 2.0f, 1, -1, 1.5f);
    }

    /**
     * Pause and resume functions will be made for footsteps when player is not running on the
     * ground
     */
    public void pauseFootSteps() {
        soundPool.pause(footSteps);
    }

    public void resumeFootSteps() {
        soundPool.resume(footSteps);
    }

    /**
     * If the app pauses or quits, we need to release the resources that SoundPool has taken up.
     */
    public void end() {
        if(soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}

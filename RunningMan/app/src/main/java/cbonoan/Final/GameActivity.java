package cbonoan.Final;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    private SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private final int MAX = 1;
    private static int gameMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        Resources res = getResources();
        gameView = new GameView(this, point.x, point.y);

        // Start the game music in the activity to avoid issued of it stopping when playing
        // another audio
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
        gameMusic = soundPool.load(this, R.raw.gamemusic, 2);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundId, int status) {
                if(soundId == gameMusic && status == 0) {
                    Log.d("Music", "Starting music");
                    soundPool.play(gameMusic, 0.7f, 0.7f, 3, -1, 1.0f);
                }
            }
        });

        setContentView(gameView);
    }

    /**
     * When returned from PauseActivity, we will take in the result code and determine what to do with
     * it
     * @param requestCode
     * @param resultCode 100 if player wants to restart and 101 if player wants to quit
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 101) { // Player wants to restart
            restartGame();
        } else if(resultCode == 102) { //Player wants to quit
            quitGame();
        }
    }

    private void quitGame() {
        finish();
    }

    /**
     * This function will reset the score and the recreate function will finish the activity and
     * run it again
     */
    private void restartGame() {
        gameView.setCurScore(0);
        recreate();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        soundPool.setVolume(gameMusic, 0.7f, 0.7f);

        gameView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.getSoundEffects().end();
    }

    /**
     * Use a handler so we can flash a game over screen to the player before ending the activity
     * The runnable object will also be responsible for releasing the resources from the SoundLoop
     * object
     */
    public void gameOver() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    /**
     * This function will turn down the music volume as well as switch activities
     *
     * startActivityResult function is similar to startActivity, but when GameActivity is returned to,
     * it will expect a result code that is handled in the onActivityResult method
     */
    public void pauseGame() {
        soundPool.setVolume(gameMusic, 0.3f, 0.3f);
        startActivityForResult(new Intent(GameActivity.this, PauseActivity.class), 101);
    }
}
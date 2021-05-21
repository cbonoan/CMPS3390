package cbonoan.Final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class PauseActivity extends AppCompatActivity {
    private Bitmap mute, unmute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pause_menu);
    }

    public void resumeGame(View view) {
        finish();
    }

    /**
     * For this function, we need to create a new Intent that will send back a code back to the original
     * activity.
     * @param view
     */
    public void restartGame(View view) {
        Intent returnIntent = new Intent();
        setResult(100, returnIntent); // We will send a 100 code letting GameActivity know to restart
        finish();
    }

    /**
     * Same as the restartGame function where we need to create an intent that will send back a
     * response code
     * @param view
     */
    public void quitGame(View view) {
        Intent returnIntent = new Intent();
        setResult(101, returnIntent); // We will send a 101 to quit the game
        finish();
    }

    public void changeVolume(View view) {

    }
}
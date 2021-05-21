package cbonoan.Final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStructure;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Main driver class for final project
 * @author Charles Bonoan
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private HighScore highScore = HighScore.getInstance();

    /**
     * Creates the MainActivity instance and sets the view to activity_main
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * When player dies, they will be sent back to MainActivity to where to where we will show
     * the highest score they have gotten
     */
    @Override
    protected void onResume() {
        super.onResume();
        TextView tvHighScore = findViewById(R.id.tvHighScore);
        tvHighScore.setText(String.format("%s", highScore.getHighScore()));
    }

    /**
     * When user starts game, we will reset the current score and start the GameActivity
     * @param view
     */
    public void startGame(View view) {
        highScore.resetCurScore();
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}
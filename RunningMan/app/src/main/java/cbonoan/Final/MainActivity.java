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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tvHighScore = findViewById(R.id.tvHighScore);
        tvHighScore.setText(String.format("%s", highScore.getHighScore()));
    }

    public void startGame(View view) {
        highScore.resetCurScore();
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}
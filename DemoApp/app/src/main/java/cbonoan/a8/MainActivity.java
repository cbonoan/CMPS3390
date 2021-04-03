package cbonoan.a8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("STATE", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("STATE", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("STATE", "onResume");
    }

    public void onBtnClicked(View view) {
        TextView txtOutput = findViewById(R.id.txtOutput);

        if(view.getId() == R.id.btnSayHi)
            txtOutput.setText(R.string.hi_android);
        else if(view.getId() == R.id.btnSayBye)
            txtOutput.setText(R.string.bye_android);
    }
}
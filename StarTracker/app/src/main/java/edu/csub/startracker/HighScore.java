package edu.csub.startracker;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Bonoan
 * @version 1.0
 * Singleton
 */
public final class HighScore {
    private static final HighScore INSTANCE = new HighScore();
    private int curScore = 0;
    private int highScore = 0;
    private String name = "Player 1";
    private FirebaseFirestore db;

    private HighScore(){
        db = FirebaseFirestore.getInstance();
    }

    public static HighScore getInstance() {
        return INSTANCE;
    }

    public void addScore(int score) {
        curScore += score;

        if(curScore > highScore) {
            highScore = curScore;
        }
    }

    public int getCurScore() {
        return curScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public String getName() {
        return name;
    }

    public void resetCurScore() {
        curScore = 0;
    }

    public void setPlayerName(String playerName) {
        name = playerName;
    }

    public void getHighScores(int howMany, ListView highScores, Context context) {
        ArrayList<String> topScores = new ArrayList<>();
        db.collection("HighScore")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(howMany)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            String tmpString = String.format("%s: %s", doc.getId(), doc.get("score"));
                            topScores.add(tmpString);
                            Log.d("SCORE", tmpString);
                        }
                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_list_item_1, topScores);
                        highScores.setAdapter(itemsAdapter);
                    }
                });
    }

    public void postHighScore() {
        Map<String, Integer> hs = new HashMap<>();
        hs.put("score", highScore);
        db.collection("HighScore").document(name)
                .set(hs)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Data", name + "'s score was set");
                    }
                });

    }
}

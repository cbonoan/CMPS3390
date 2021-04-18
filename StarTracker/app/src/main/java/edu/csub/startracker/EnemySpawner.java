package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Charles Bonoan
 * @version 1.0
 * This class will create an array list that will hold all our enemies
 */
public class EnemySpawner {
    private ArrayList<GameObject> enemies;
    float x,y = 0;
    int screenWidth;
    int wave = 1, enemy01Spawned = 0, enemy02Spawned = 0;
    int frameTick = 0, spawnTick, waveTick = 0;
    Resources res;
    private Paint paint = new Paint();

    public EnemySpawner(Resources res) {
        screenWidth = res.getDisplayMetrics().widthPixels;
        this.res = res;
        enemies = new ArrayList<>();
        spawnTick = new Random().nextInt(120 - 60) + 60;
        paint.setColor(Color.WHITE);
        paint.setTextSize(screenWidth * 0.05f);
    }

    public void update() {
        frameTick++;

        // Spawning logic
        // Wait a given amount of frames
        if(frameTick >= spawnTick) {
            // Reset frameTick
            frameTick = 0;
            spawnTick = new Random().nextInt(120 - 60) + 60;

            // Move x to new pos
            x = new Random().nextInt( (int) (screenWidth * 0.75f - screenWidth * 0.05f) ) + screenWidth * 0.05f;

            // Choose enemy to spawn
            int tmp = (int) Math.round(Math.random());

            // if tmp is 0, spawn enemy01, else spawn enemy02
            // and checking if their spawn count is less than the wave
            // Spawn that enemy and increment its spawn count
            if(tmp == 0 && enemy01Spawned < wave) {
                enemies.add(new Enemy01(res,x,y));
                enemy01Spawned++;
            } else if(tmp == 1 && enemy02Spawned < wave) {
                enemies.add(new Enemy02(res, x, y));
                enemy02Spawned++;
            }

        }

        // Check if all enemies per wave have spawned
        if(enemy01Spawned >= wave && enemy02Spawned >= wave) {
            // Increment waveTick
            waveTick++;

        }
        // Wait some frames to start the next wave
        if(waveTick >= 240) {
            wave++;
            waveTick = 0;
            enemy01Spawned = 0;
            enemy02Spawned = 0;
        }

        // Update all enemies
        for(Iterator<GameObject> iterator = enemies.iterator(); iterator.hasNext();) {
            GameObject go = iterator.next();
            go.update();

            // Check if game object should be destroyed
            if(!go.isAlive()) {
                iterator.remove();
            }
        }

    }

    public void draw(Canvas canvas) {
        canvas.drawText("Wave: " + wave, screenWidth * 0.05f, screenWidth * 0.05f, paint);

        for(GameObject go : enemies) {
            go.draw(canvas);
        }
    }

    public ArrayList<GameObject> getEnemies() {
        return enemies;
    }
}

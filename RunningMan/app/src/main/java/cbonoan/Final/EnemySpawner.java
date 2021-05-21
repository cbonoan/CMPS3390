package cbonoan.Final;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Class EnemySpawner will be responsible for holding all of our enemies and choosing which enemy
 * to spawn. Also responsible for despawning and enemies that go off screen.
 * @author Charles Bonoan
 * @version 1.0
 */
public class EnemySpawner {
    private Resources res;

    private ArrayList<GameObject> enemies;
    int frameTick = 0, spawnTick;

    int maxEnemies = 4; // Can increase if adding new enemies

    /**
     * Constructor for EnemySpawner
     * Initializes the resources as well as the spawnTick which decides how long to wait
     * before spawning a new enemy
     * @param res
     */
    public EnemySpawner(Resources res) {
        this.res = res;

        enemies = new ArrayList<>();

        spawnTick = new Random().nextInt(120 - 60) + 60;
    }

    /**
     * This function will keep checking if it is time to spawn a new enemy and determine which
     * enemy to spawn
     */
    public void update() {
        frameTick++;

        // Once frameTick is greater than equal to the spawnTick, we spawn a new enemy
        if(frameTick >= spawnTick) {
            frameTick = 0;
            spawnTick = new Random().nextInt(240 - 120) + 120;

            // Use switch cases to determine what enemy to spawn
            // enemyToSpawn will be anywhere from 1 to maxEnemies
            int enemyToSpawn  = new Random().nextInt(maxEnemies) + 1;
            switch (enemyToSpawn) {
                case 1:
                    enemies.add(new EnemyRhino(res));
                    break;
                case 2:
                    enemies.add(new EnemyTurtle(res));
                    break;
                case 3:
                    enemies.add(new EnemyBird(res));
                    break;
                case 4:
                    enemies.add(new EnemyBird(res));
                    enemies.add(new EnemyRhino(res));
                    break;
            }

        }

        // Update all enemies using iterator
        for(Iterator<GameObject> iterator = enemies.iterator(); iterator.hasNext();) {
            GameObject go = iterator.next();
            go.update();

            // If enemy is off screen to the left, remove it from list
            if(go.getX() <= -100) {
                iterator.remove();
            }
        }
    }

    public void draw(Canvas canvas) {
        for(GameObject go : enemies) {
            go.draw(canvas);
        }
    }

    public ArrayList<GameObject> getEnemies() {
        return enemies;
    }
}

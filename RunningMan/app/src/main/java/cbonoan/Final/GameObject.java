package cbonoan.Final;

import android.graphics.Canvas;

/**
 * Every object that is created in this application (player and enemies mostly)
 * will implements these methods in order to keep track of health and damage
 * @author Charles Bonoan
 * @version 1.0
 */
public interface GameObject {
    void update();
    void draw(Canvas canvas);

    float getX();
    float getY();
    float getWidth();
    float getHeight();

    boolean isAlive();
    float getHealth();
    float takeDamage(float damage);
}

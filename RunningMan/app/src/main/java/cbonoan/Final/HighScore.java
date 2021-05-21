package cbonoan.Final;

public final class HighScore {
    private static final HighScore INSTANCE = new HighScore();
    private int curScore = 0;
    private int highScore = 0;

    /**
     * Singleton
     * @return an instance of HighScore
     */
    public static HighScore getInstance() { return INSTANCE; }

    /**
     * Will add onto the current score of the player
     * Will give illusion of measuring distance player has ran in meters
     * @param score
     */
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

    public void resetCurScore() {
        curScore = 0;
    }
}

package bagel.builds.hide_n_seek.instance;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.manager.GameManager;

public class Game {
    private Main main;
    private static int gameTime;
    private static int hideTime;
    private GameManager gameManager;

    public Game(Main main) {
        this.main = main;
    }

    public void start() {}

    public void reset() {

    }

    public static void setGameTime(int time) {
        gameTime = time;
    }

    public GameManager getGameManager() { return gameManager; }

}

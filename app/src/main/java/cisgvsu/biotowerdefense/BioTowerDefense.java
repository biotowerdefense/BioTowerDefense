package cisgvsu.biotowerdefense;

import android.app.Application;

/**
 * Global application context.
 */

public class BioTowerDefense extends Application {
    /** The game object that all classes can reference. */
    private Game game = new Game();

    /**
     * Get the game object for this instance of the application.
     * @return
     */
    public Game getGame() {
        return game;
    }

    /**
     * Restart the game.
     * @return
     */
    public Game startNew() {
        game = new Game();
        return game;
    }

}

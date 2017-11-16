package cisgvsu.biotowerdefense;

import android.app.Application;

/**
 * Created by Kelsey on 11/16/2017.
 */

public class BioTowerDefense extends Application {
    private Game game = new Game();

    public Game getGame() {
        return game;
    }

}

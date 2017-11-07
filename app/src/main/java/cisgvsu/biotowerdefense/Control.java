package cisgvsu.biotowerdefense;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Create a new game instance, start/stop game, move into store or library of info
 * Created by Kelsey on 9/6/2017.
 */

public class Control extends Activity {
    Game game = new Game();

    boolean play = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
    }
}
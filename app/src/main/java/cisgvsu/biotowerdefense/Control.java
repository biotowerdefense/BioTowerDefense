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

        //Declare the buttons
        final Button stopStartButton = (Button)findViewById(R.id.stopStartButton);
        final Button tower0Button = (Button)findViewById(R.id.tower0Button);
        final Button tower1Button = (Button)findViewById(R.id.tower1Button);
        final Button tower2Button = (Button)findViewById(R.id.tower2Button);
        final Button tower3Button = (Button)findViewById(R.id.tower3Button);
        final Button tower4Button = (Button)findViewById(R.id.tower4Button);
        final Button storeButton = (Button)findViewById(R.id.storeButton);
        final Button libraryButton = (Button)findViewById(R.id.libraryButton);;

        //Stop and start the game
        stopStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                play = !play;
                if (play) {
                    //change button text
                    stopStartButton.setText("Pause");
                    //hide extra buttons
                    storeButton.setVisibility(View.INVISIBLE);
                    libraryButton.setVisibility(View.INVISIBLE);
                    //Mute tower buttons
                    tower0Button.setClickable(false);
                    tower1Button.setClickable(false);
                    tower2Button.setClickable(false);
                    //start the game
                    game.startGame();
                } else {
                    //pause the game
                    game.stopGame();
                    //change button text
                    stopStartButton.setText("Play");
                    //show extra buttons / explanations
                    storeButton.setVisibility(View.VISIBLE);
                    libraryButton.setVisibility(View.VISIBLE);
                    //Un-mute tower buttons
                    tower0Button.setClickable(true);
                    tower1Button.setClickable(true);
                    tower2Button.setClickable(true);
                }
            }
        });

        //Tower 0
        tower0Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Show selection of  available
                //Get selection
                game.addTower(AntibioticType.linezolid, 0);
            }
        });

        //Tower 1
        tower1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Show selection of antibiotics available
                //Get selection
                game.addTower(AntibioticType.penicillin, 1);
            }
        });

        //Tower 2
        tower2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Show selection of antibiotics available
                //Get selection
                game.addTower(AntibioticType.vancomycin, 2);
            }
        });

        //Tower 3
        tower3Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Show selection of antibiotics available
                //Get selection
                game.addTower(AntibioticType.linezolid, 3);
            }
        });

        //Tower 4
        tower4Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Show selection of antibiotics available
                //Get selection
                game.addTower(AntibioticType.vancomycin, 4);
            }
        });

        //Open the store
        storeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the store
            }
        });

        //Open the library
        libraryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the library
            }
        });
    }
}
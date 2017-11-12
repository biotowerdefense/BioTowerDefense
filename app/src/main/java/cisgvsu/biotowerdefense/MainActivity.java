package cisgvsu.biotowerdefense;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements Observer {

    public static final String EXTRA_TOWER_POSITION = "cisgvsu.biotowerdefense.TOWER_POSITION";
    public static final String EXTRA_INVENTORY = "cisgvsu.biotowerdefense.EXTRA_INVENTORY";
    public final Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String tower = extras.getString(InventoryFragment.EXTRA_TOWER_TO_PLACE);
            int position = extras.getInt(InventoryFragment.EXTRA_TOWER_POSITION);
            Log.d("tag", "Tower name: " + tower + " tower position: " + position);
        }

        // Add ourselves as an observer, and then pass the game object to the view
        this.game.addObserver(this);
        ((GameSurfaceView) findViewById(R.id.surfaceView)).setGame(game);

        // Control starting and pausing the game
        final Button startStop = (Button) findViewById(R.id.startStop);
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the game if it was paused, otherwise pause it
                if (game.isPaused()) {
                    game.restartGame();
                    startStop.setText(R.string.pauseGame);
                } else {
                    game.stopGame();
                    startStop.setText(R.string.startGame);
                }
            }
        });

        final ImageView tower0 = (ImageView) findViewById(R.id.tower0);
        tower0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchStore(0);
                tower0.setImageResource(R.drawable.tower);
                //add a tower to place zero
                // TODO: in future code call the store/inventory here to pick the correct tower.
                // For now just use penicillin for everything
                //game.addTower(AntibioticType.penicillin, 0);
            }
        });

        final ImageView tower1 = (ImageView) findViewById(R.id.tower1);
        tower1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchStore(1);
                //if (tower1.get)
                //tower1.setImageResource(R.drawable.tower);
                //add a tower to place one
                //game.addTower(AntibioticType.penicillin, 1);
            }
        });

        final ImageView tower2 = (ImageView) findViewById(R.id.tower2);
        tower2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchStore(2);
                //tower2.setImageResource(R.drawable.tower);
                //add a tower to place two
                //game.addTower(AntibioticType.penicillin, 2);
            }
        });

        final ImageView tower3 = (ImageView) findViewById(R.id.tower3);
        tower3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchStore(3);
                //tower3.setImageResource(R.drawable.tower);
                //add a tower to place three
               // game.addTower(AntibioticType.penicillin, 3);
            }
        });

        final ImageView tower4 = (ImageView) findViewById(R.id.tower4);
        tower4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchStore(4);
                //tower4.setImageResource(R.drawable.tower);
                //add a tower to place four
                //game.addTower(AntibioticType.penicillin, 4);
            }
        });
    }

    /**
     * Launch the store/inventory screen and pass to it which tower was pressed.
     * @param position The tower that was pressed.
     */
    public void launchStore(int position) {
        Intent intent = new Intent(this, StoreInventoryNavigationActivity.class);
        intent.putExtra(EXTRA_TOWER_POSITION, position);
        intent.putStringArrayListExtra(EXTRA_INVENTORY, game.getInventoryAsStrings());
        startActivity(intent);
    }


    /**
     * If we get updated from the game that something has become resistant,
     * show a dialog alerting the user.
     * @param o Observable object we got message from
     * @param arg Arg from game
     */
    @Override
    public void update(Observable o, Object arg) {
        Log.d("tag", (String) arg);
        final String msg = (String) arg;

        // We have to run this on the UI thread to avoid errors
        new Thread() {
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Pause game, show dialog
                            game.stopGame();
                            getDialog(msg).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * Create a dialog that shows a message.
     * @param msg Message to be shown in dialog.
     * @return The dialog.
     */
    private AlertDialog getDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);

        // Add button to handle when user wants to place the tower
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Restart game on close
                game.startGame();
            }
        });

        return builder.create();
    }
}

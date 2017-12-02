package cisgvsu.biotowerdefense;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements Observer {

    public static final String EXTRA_TOWER_POSITION = "cisgvsu.biotowerdefense.TOWER_POSITION";
    public static final String EXTRA_INVENTORY = "cisgvsu.biotowerdefense.EXTRA_INVENTORY";
    public static final String EXTRA_MONEY = "cisgvsu.biotowerdefense.EXTRA_MONEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        BioTowerDefense app = (BioTowerDefense) getApplicationContext();
        final Game game = app.getGame();

        // Add ourselves as an observer, and then pass the game object to the view
        game.addObserver(this);
        ((GameSurfaceView) findViewById(R.id.surfaceView)).setGame(game);

        // Control starting and pausing the game
        final Button startStop = (Button) findViewById(R.id.startStop);
        if (game.isPaused()) {
            Log.d("*****************", "game is paused");
            startStop.setText("Start");
        } else {
            Log.d("*****************", "game is not paused!");
            startStop.setText("Pause");
        }

        // Set click listener for library button
        final Button library = (Button) findViewById(R.id.library);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.stopGame();
                launchLibrary();
            }
        });

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

        // Handle any extras we may have gotten (aka we navigated here from store/inventory)
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Get the extras from the inventory, if that's where we came from
            String tower = extras.getString(InventoryFragment.EXTRA_TOWER_TO_PLACE);
            int position = extras.getInt(InventoryFragment.EXTRA_TOWER_POSITION, -999);

            // Came from inventory
            if (position != -999 && tower != null) {
                String strType = tower.substring(0, tower.indexOf("\n"));

                // Add tower to game
                AntibioticType type = AntibioticType.stringToEnum(strType);
                game.takeOutOfInventoryAndAdd(type, position);
            } else {
                // Get the extras from the store, if that's where we came from
                position = extras.getInt(StoreFragment.EXTRA_TOWER_POSITION, -999);
                tower = extras.getString(StoreFragment.EXTRA_TOWER_TO_PLACE);

                // Came from store
                if (position != -999 && tower != null) {
                    String strType = tower.substring(0, tower.indexOf("\n"));

                    // Add tower to game
                    AntibioticType type = AntibioticType.stringToEnum(strType);
                    game.buyTower(type, position);
                }
            }
        }

        ArrayList<ImageView> towerImages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int index = i;
            final ImageView t;
            switch (i) {
                case 0:
                    t = (ImageView) findViewById(R.id.tower0);
                    break;
                case 1:
                    t = (ImageView) findViewById(R.id.tower1);
                    break;
                case 2:
                    t = (ImageView) findViewById(R.id.tower2);
                    break;
                case 3:
                    t = (ImageView) findViewById(R.id.tower3);
                    break;
                case 4:
                    t = (ImageView) findViewById(R.id.tower4);
                    break;
                default:
                    t = null;
            }

            if (t != null) {
                t.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        launchStore(index);
                    }
                });

                // If this spot has an actual tower, get the right resource for it
                AntibioticTower towerFromGame = game.towerAtIndex(i);
                if (towerFromGame != null) {
                    AntibioticType type = towerFromGame.getType();
                    t.setImageResource(AntibioticType.getImage(type));
                }
            }

            towerImages.add(t);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set appropriate text for start/stop button
        Button startStop = (Button) findViewById(R.id.startStop);
        BioTowerDefense app = (BioTowerDefense) getApplicationContext();
        Game game = app.getGame();
        if (game.isPaused()) {
            startStop.setText("Start");
        } else {
            startStop.setText("Pause");
        }
    }

    /**
     * Launch the store/inventory screen and pass to it which tower was pressed.
     * @param position The tower that was pressed.
     */
    public void launchStore(int position) {
        BioTowerDefense app = (BioTowerDefense) getApplicationContext();
        final Game game = app.getGame();
        game.stopGame();

        Intent intent = new Intent(this, StoreInventoryNavigationActivity.class);
        intent.putExtra(EXTRA_TOWER_POSITION, position);
        intent.putStringArrayListExtra(EXTRA_INVENTORY, game.getInventoryAsStrings());
        intent.putExtra(EXTRA_MONEY, game.getMoney());
        startActivity(intent);
    }

    public void launchLibrary() {
        Intent intent = new Intent(this, LibraryActivity.class);
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
        // Get the message we were sent, handle depending on type
        ObserverMessage msg = (ObserverMessage) arg;

        // Get game object
        BioTowerDefense app = (BioTowerDefense) getApplicationContext();
        final Game game = app.getGame();

        if (msg.getType() == ObserverMessage.RESISTANCE) {
            // Bacteria became resistance
            final String text = msg.getText();

            // We have to run this on the UI thread to avoid errors
            new Thread() {
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Pause game, show dialog
                                game.stopGame();
                                getDialog(text, true, false).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }.start();

        } else if (msg.getType() == ObserverMessage.GAME_OVER) {
            // Game has been lost
            final String text = msg.getText();
            ((GameSurfaceView) findViewById(R.id.surfaceView)).setGame(app.startNew());

            new Thread() {
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Pause game, show dialog
                                getDialog(text, false, true).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * Create a dialog that shows a message.
     * @param msg Message to be shown in dialog.
     * @return The dialog.
     */
    private AlertDialog getDialog(String msg, final boolean restartOnClose, final boolean newGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        BioTowerDefense app = (BioTowerDefense) getApplicationContext();
        final Game game = app.getGame();

        // Add button to handle when user wants to place the tower
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (restartOnClose) {
                    // Restart game on close
                    game.restartGame();
                } if (newGame) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        return builder.create();
    }
}

package cisgvsu.biotowerdefense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TOWER_POSITION = "cisgvsu.biotowerdefense.TOWER_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        final ImageView tower0 = (ImageView) findViewById(R.id.tower0);
        tower0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchStore(0);
                tower0.setImageResource(R.drawable.tower);
                //add a tower to place zero
                // TODO: in future code call the store/inventory here to pick the correct tower.
                // For now just use penicillin for everything
                AntibioticTower towerZero = new AntibioticTower(AntibioticType.penicillin, 0);
            }
        });

        final ImageView tower1 = (ImageView) findViewById(R.id.tower1);
        tower1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if (tower1.get)
                tower1.setImageResource(R.drawable.tower);
                //add a tower to place one
                AntibioticTower towerOne = new AntibioticTower(AntibioticType.penicillin, 1);
            }
        });

        final ImageView tower2 = (ImageView) findViewById(R.id.tower2);
        tower2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower2.setImageResource(R.drawable.tower);
                //add a tower to place two
                AntibioticTower towerTwo = new AntibioticTower(AntibioticType.penicillin, 2);
            }
        });

        final ImageView tower3 = (ImageView) findViewById(R.id.tower3);
        tower3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower3.setImageResource(R.drawable.tower);
                //add a tower to place three
                AntibioticTower towerThree = new AntibioticTower(AntibioticType.penicillin, 3);
            }
        });

        final ImageView tower4 = (ImageView) findViewById(R.id.tower4);
        tower4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower4.setImageResource(R.drawable.tower);
                //add a tower to place four
                AntibioticTower towerFour = new AntibioticTower(AntibioticType.penicillin, 4);
            }
        });
    }

    /**
     * Launch the store/inventory screen and pass to it which tower was pressed.
     * @param position
     */
    public void launchStore(int position) {
        Intent intent = new Intent(this, StoreInventoryNavigationActivity.class);
        intent.putExtra(EXTRA_TOWER_POSITION, position);
        startActivity(intent);
    }
}

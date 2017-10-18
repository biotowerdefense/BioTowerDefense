package cisgvsu.biotowerdefense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        final ImageView tower0 = (ImageView) findViewById(R.id.tower0);
        tower0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower0.setImageResource(R.drawable.tower);
            }
        });

        final ImageView tower1 = (ImageView) findViewById(R.id.tower1);
        tower1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower1.setImageResource(R.drawable.tower);
            }
        });

        final ImageView tower2 = (ImageView) findViewById(R.id.tower2);
        tower2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower2.setImageResource(R.drawable.tower);
            }
        });

        final ImageView tower3 = (ImageView) findViewById(R.id.tower3);
        tower3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower3.setImageResource(R.drawable.tower);
            }
        });

        final ImageView tower4 = (ImageView) findViewById(R.id.tower4);
        tower4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tower4.setImageResource(R.drawable.tower);
            }
        });
    }
}

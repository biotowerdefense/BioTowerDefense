package cisgvsu.biotowerdefense;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Library");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Description for each type
        int[] details = new int[6];
        details[0] = AntibioticType.getDescription(AntibioticType.penicillin);
        details[1] = AntibioticType.getDescription(AntibioticType.vancomycin);
        details[2] = AntibioticType.getDescription(AntibioticType.linezolid);
        details[3] = BacteriaType.getDescription(BacteriaType.pneumonia);
        details[4] = BacteriaType.getDescription(BacteriaType.strep);
        details[5] = BacteriaType.getDescription(BacteriaType.staph);

        // Name for each type
        String[] names = new String[6];
        names[0] = AntibioticType.toString(AntibioticType.penicillin);
        names[1] = AntibioticType.toString(AntibioticType.vancomycin);
        names[2] = AntibioticType.toString(AntibioticType.linezolid);
        names[3] = BacteriaType.getLongName(BacteriaType.pneumonia) + " (" + BacteriaType.getShortName(BacteriaType.pneumonia) + ")";
        names[4] = BacteriaType.getLongName(BacteriaType.strep) + " (" + BacteriaType.getShortName(BacteriaType.strep) + ")";
        names[5] = BacteriaType.getLongName(BacteriaType.staph) + " (" + BacteriaType.getShortName(BacteriaType.staph) + ")";

        // Image resource for each type
        int[] imgs = new int[6];
        imgs[0] = AntibioticType.getImage(AntibioticType.penicillin);
        imgs[1] = AntibioticType.getImage(AntibioticType.vancomycin);
        imgs[2] = AntibioticType.getImage(AntibioticType.linezolid);
        imgs[3] = BacteriaType.getImage(BacteriaType.pneumonia);
        imgs[4] = BacteriaType.getImage(BacteriaType.strep);
        imgs[5] = BacteriaType.getImage(BacteriaType.staph);

        // Set up adapter
       LibraryEntryAdapter adapter = new LibraryEntryAdapter(this.getApplicationContext(), details, names, imgs);

        // Attach to GridView
        GridView gridView = (GridView) findViewById(R.id.libraryGridView);
        gridView.setAdapter(adapter);

    }




}

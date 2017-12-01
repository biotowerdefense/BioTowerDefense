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

        int[] list = new int[6];
        list[0] = AntibioticType.getDescription(AntibioticType.penicillin);
        list[1] = AntibioticType.getDescription(AntibioticType.vancomycin);
        list[2] = AntibioticType.getDescription(AntibioticType.linezolid);
        list[3] = BacteriaType.getDescription(BacteriaType.pneumonia);
        list[4] = BacteriaType.getDescription(BacteriaType.strep);
        list[5] = BacteriaType.getDescription(BacteriaType.staph);

        AntibioticType[] abTypes = new AntibioticType[3];
        abTypes[0] = AntibioticType.penicillin;
        abTypes[1] = AntibioticType.vancomycin;
        abTypes[2] = AntibioticType.linezolid;

        BacteriaType[] bacTypes = new BacteriaType[3];
        bacTypes[0] = BacteriaType.pneumonia;
        bacTypes[1] = BacteriaType.strep;
        bacTypes[2] = BacteriaType.staph;

        // Set up adapter
       LibraryEntryAdapter adapter = new LibraryEntryAdapter(this.getApplicationContext(), list, abTypes, bacTypes);

        // Attach to GridView
        GridView gridView = (GridView) findViewById(R.id.libraryGridView);
        gridView.setAdapter(adapter);

    }




}

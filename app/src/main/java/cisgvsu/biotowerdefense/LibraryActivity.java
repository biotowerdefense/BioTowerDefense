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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] list = new String[3];
        list[0] = AntibioticType.getDescription(AntibioticType.penicillin);
        list[1] = AntibioticType.getDescription(AntibioticType.vancomycin);
        list[2] = AntibioticType.getDescription(AntibioticType.linezolid);

        AntibioticType[] types = new AntibioticType[3];
        types[0] = AntibioticType.penicillin;
        types[1] = AntibioticType.vancomycin;
        types[2] = AntibioticType.linezolid;

        // Set up adapter
       LibraryEntryAdapter adapter = new LibraryEntryAdapter(this.getApplicationContext(), list, types);

        // Attach to GridView
        GridView gridView = (GridView) findViewById(R.id.libraryGridView);
        gridView.setAdapter(adapter);

    }




}

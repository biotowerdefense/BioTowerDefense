package cisgvsu.biotowerdefense;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Kelsey on 11/5/2017.
 */

public class StoreFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view for this fragment
        View view = inflater.inflate(R.layout.store_fragment, container, false);

        // Set up adapter
        ArrayList<String> list = new ArrayList<>();
        list.add("Tower 1");
        list.add("Tower 2");
        list.add("Tower 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);

        // Attach to GridView
        GridView gridView = (GridView) view.findViewById(R.id.storeGridView);
        gridView.setAdapter(adapter);

        return view;
    }
}

package cisgvsu.biotowerdefense;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Control the contents of the Inventory tab.
 * Created by Kelsey on 11/5/2017.
 */

public class InventoryFragment extends android.support.v4.app.Fragment {

    /**
     * Set up the handler for when an item in the inventory is clicked on.
     */
    private AdapterView.OnItemClickListener mMessageClickHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.d("Tag", "Clicked position " + position + " with ID " + id);
            getDialog(position).show();
        }
    };

    /**
     * Build a dialog to prompt the user about whether they want to place the tower.
     */
    private AlertDialog getDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Place this tower?");

        // Add button to handle when user wants to place the tower
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add
                Log.d("tag", "pressed add tower");
            }
        });

        // Add button to handle when user cancels placing tower
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel
                Log.d("tag", "pressed cancel");
            }
        });

        return builder.create();
    }

    /**
     * Create the view for the inventory by populating the GridView.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view for this fragment
        View view = inflater.inflate(R.layout.inventory_fragment, container, false);

        // Set up adapter
        ArrayList<String> list = new ArrayList<>();
        list.add("Tower 1");
        list.add("Tower 2");
        list.add("Tower 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);

        // Attach to GridView
        GridView gridView = (GridView) view.findViewById(R.id.inventoryGridView);
        gridView.setAdapter(adapter);

        // Set click listener
        gridView.setOnItemClickListener(mMessageClickHandler);

        return view;
    }
}

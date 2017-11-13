package cisgvsu.biotowerdefense;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.HashMap;

/**
 * Control the contents of the Inventory tab.
 * Created by Kelsey on 11/5/2017.
 */

public class InventoryFragment extends android.support.v4.app.Fragment {
    private ArrayList<String> strInventory = new ArrayList<>();
    private HashMap<String, Integer> inventory = new HashMap<>();
    private int towerPosition = 0;
    final static String EXTRA_TOWER_TO_PLACE = "cisgvsu.biotowerdefense.tower_to_place";
    final static String EXTRA_TOWER_POSITION = "cisgvsu.biotowerdefense.tower_position";

    /**
     * Set up the handler for when an item in the strInventory is clicked on.
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
    private AlertDialog getDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Place this tower?");

        // Add button to handle when user wants to place the tower
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add the tower
                handleAdd(position);
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
     * Create the view for the strInventory by populating the GridView.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the stuff that the main navigation activity sent us
        if (getArguments() != null) {
            this.towerPosition = getArguments().getInt(MainActivity.EXTRA_TOWER_POSITION);
            parseInventory(getArguments().getStringArrayList(MainActivity.EXTRA_INVENTORY));
            createStrInventory();
        }

        // Get the view for this fragment
        View view = inflater.inflate(R.layout.inventory_fragment, container, false);

        // Set up adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, this.strInventory);

        // Attach to GridView
        GridView gridView = (GridView) view.findViewById(R.id.inventoryGridView);
        gridView.setAdapter(adapter);

        // Set click listener
        gridView.setOnItemClickListener(mMessageClickHandler);

        return view;
    }

    /**
     * If we're adding this tower, then build an intent for it
     * and send user back to the main screen.
     * @param index Index in list of tower to add.
     */
    public void handleAdd(int index) {
        String tower = this.strInventory.get(index);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(InventoryFragment.EXTRA_TOWER_TO_PLACE, tower);
        intent.putExtra(InventoryFragment.EXTRA_TOWER_POSITION, this.towerPosition);
        startActivity(intent);
    }

    /**
     * Parse the list of strings we got into a HashMap of the types
     * of towers and how many are available to be placed.
     */
    public void parseInventory(ArrayList<String> strInv) {
        HashMap<String, Integer> inv = new HashMap<>();
        for (String str : strInv) {
            // Get the type and whether it was placed
            String type = str.substring(0, str.indexOf(":"));
            String placed = str.substring(str.indexOf(":")+2);

            // If not placed, always add
            if (placed.equals("false")) {
                // If it's already placed, add 1 to the count
                if (inv.containsKey(type)) {
                    inv.put(type, inv.get(type)+1);
                } else {
                    inv.put(type, 1);
                }
            } else {
                // If placed, only add if this is the first one we found
                // and note that 0 are available to be placed
                if (!inv.containsKey(type)) {
                    inv.put(type, 0);
                }
            }
        }
        this.inventory = inv;
    }

    /**
     * Create the list that will be displayed
     * as the inventory.
     */
    public void createStrInventory() {
        ArrayList<String> list = new ArrayList<>();
        for (String type : this.inventory.keySet()) {
            String tmp = type.substring(0,1).toUpperCase() + type.substring(1);
            list.add(tmp + "\nAvailable: " + this.inventory.get(type));
        }
        this.strInventory = list;
    }
}

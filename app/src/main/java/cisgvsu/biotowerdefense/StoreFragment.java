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

/**
 * Control the contents of the store tab.
 */

public class StoreFragment extends android.support.v4.app.Fragment {
    private int towerPosition = 0;
    private int money = 0;
    final static String EXTRA_TOWER_TO_PLACE = "cisgvsu.biotowerdefense.EXTRA_TOWER_TO_PLACE";
    final static String EXTRA_TOWER_POSITION = "cisgvsu.biotowerdefense.EXTRA_TOWER_POSITION";
    private ArrayList<String> store;

    /**
     * Set up the handler for when an item in the inventory is clicked on.
     */
    private AdapterView.OnItemClickListener mMessageClickHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.d("Tag", "Clicked position " + position + " with ID " + id);
            getPurchaseDialog(position).show();
        }
    };

    /**
     * Build a dialog to prompt the user about whether they want to place the tower.
     */
    private AlertDialog getPurchaseDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Place this tower?");

        // Add button to handle when user wants to place the tower
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add
                handlePurchase(position);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the stuff that the main navigation activity sent us
        if (getArguments() != null) {
            this.towerPosition = getArguments().getInt(MainActivity.EXTRA_TOWER_POSITION);
            this.money = getArguments().getInt(MainActivity.EXTRA_MONEY);
            Log.d("tag", "Money = " + this.money);
        }

        // Get the view for this fragment
        View view = inflater.inflate(R.layout.store_fragment, container, false);

        // Set up adapter
        this.store = new ArrayList<>();
        store.add("Penicillin\nCost: " + AntibioticType.getCost(AntibioticType.penicillin));
        store.add("Vancomycin\nCost: " + AntibioticType.getCost(AntibioticType.vancomycin));
        store.add("Linezolid\nCost: " + AntibioticType.getCost(AntibioticType.linezolid));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, store);

        // Attach to GridView
        GridView gridView = (GridView) view.findViewById(R.id.storeGridView);
        gridView.setAdapter(adapter);

        // Set click listener
        gridView.setOnItemClickListener(mMessageClickHandler);

        return view;
    }

    public void handlePurchase(int index) {
        // Parse values
        String str = this.store.get(index);
        //String tower = str.substring(0, str.indexOf("\n"));
        int cost = Integer.parseInt(str.substring(str.indexOf(":")+2));

        Log.d("tag", "Money: " + this.money + " Cost: " + cost);

        // See if we can afford it, place tower if we can
        if (cost <= this.money) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(StoreFragment.EXTRA_TOWER_TO_PLACE, str);
            intent.putExtra(StoreFragment.EXTRA_TOWER_POSITION, this.towerPosition);
            startActivity(intent);
        } else {
            // Tell the user they can't afford it if it's too much
            Log.d("tag", "That costs too much");
            getTooExpensiveDialog().show();
        }
    }


    /**
     * Build a dialog to tell the user that the tower they picked is
     * too expensive.
     */
    private AlertDialog getTooExpensiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You don't have enough money to buy that tower.");

        // Add button to handle when user wants to place the tower
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add
                Log.d("tag", "Closed dialog");
            }
        });

        return builder.create();
    }
}

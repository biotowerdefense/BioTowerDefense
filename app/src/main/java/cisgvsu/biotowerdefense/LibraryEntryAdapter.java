package cisgvsu.biotowerdefense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom listview for library.
 * Created by Kelsey on 11/30/2017.
 */

public class LibraryEntryAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final int[] values;
    private final AntibioticType[] abTypes;
    private final BacteriaType[] bacTypes;

    public LibraryEntryAdapter(Context context, int[] values, AntibioticType[] abTypes, BacteriaType[] bacTypes) {
        super(context, -1, new String[6]);
        this.context = context;
        this.values = values;
        this.abTypes = abTypes;
        this.bacTypes = bacTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        // Set detail text
        TextView detailText = (TextView) rowView.findViewById(R.id.detail);
        detailText.setText(values[position]);

        // Set label text
        TextView labelText = (TextView) rowView.findViewById(R.id.label);
        if (position < 3) {
            labelText.setText(AntibioticType.toString(abTypes[position]));
        } else {
            String title = BacteriaType.getLongName(bacTypes[position-3]) + " (" + BacteriaType.getShortName(bacTypes[position-3]) + ")";
            labelText.setText(title);
        }

        // Set icon
        ImageView img = (ImageView) rowView.findViewById(R.id.icon);
        if (position < 3) {
            img.setImageResource(AntibioticType.getImage(abTypes[position]));
        } else {
            img.setImageResource(BacteriaType.getImage(bacTypes[position-3]));
        }

        return rowView;
    }

}

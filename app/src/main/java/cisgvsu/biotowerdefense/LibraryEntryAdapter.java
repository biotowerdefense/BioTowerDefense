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
    private final String[] values;
    private final AntibioticType[] types;

    public LibraryEntryAdapter(Context context, String[] values, AntibioticType[] types) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.types = types;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        // Set label text
        TextView labelText = (TextView) rowView.findViewById(R.id.label);
        labelText.setText(AntibioticType.toString(types[position]));

        // Set detail text
        TextView detailText = (TextView) rowView.findViewById(R.id.detail);
        detailText.setText(values[position]);

        // Set icon
        ImageView img = (ImageView) rowView.findViewById(R.id.icon);
        img.setImageResource(AntibioticType.getImage(types[position]));

        return rowView;
    }

}

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
    private final int[] details;
    private final String[] names;
    private final int[] imgs;

    public LibraryEntryAdapter(Context context, int[] details, String[] names, int[] imgs) {
        super(context, -1, new String[6]);
        this.context = context;
        this.details = details;
        this.names = names;
        this.imgs = imgs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        // Set label text
        TextView labelText = (TextView) rowView.findViewById(R.id.label);
        labelText.setText(names[position]);
        labelText.setAllCaps(true);

        // Set detail text
        TextView detailText = (TextView) rowView.findViewById(R.id.detail);
        detailText.setText(details[position]);

        // Set icon
        ImageView img = (ImageView) rowView.findViewById(R.id.icon);
        img.setImageResource(imgs[position]);

        return rowView;
    }

}

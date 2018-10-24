package com.example.android.sunshine.app;

/**
 * Created by Grigoras on 24.04.2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] text;
    private final Integer[] imageId;

    public CustomAdapter(Activity context,
                      String[] text, Integer[] imageId) {
        super(context, R.layout.list_item_forecast, text);
        this.context = context;
        this.text = text;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item_forecast, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.day_description);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.day_icon);
        txtTitle.setText(text[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }

    public void add ( int position , String txt , Integer icon ) {
        this.text[position] = txt ;
        this.imageId[position] = icon ;
    }
}

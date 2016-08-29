package com.roflcopter.starter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Area51 on 28-Jul-16.
 */
public class CustomAdapter extends ArrayAdapter<Application> {

    public CustomAdapter(Context context, int resource, ArrayList<Application> objects) {
        super(context, R.layout.gird_item_layout, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.gird_item_layout,parent,false);
        Application application = getItem(position);

        ImageView imageTesting = (ImageView)customView.findViewById(R.id.imagetesting);
        imageTesting.setImageBitmap(application.getBitmap());

        return customView;
    }
}

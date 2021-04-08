package com.example.mpd_coursework;

/*
    Name : Connor Goodman
    ID : S1625004
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Earthquake> {

    private ArrayList<Earthquake> currList = null;
    private Context context;

    public CustomArrayAdapter(Context nContext, ArrayList<Earthquake> nList)
    {
        super(nContext, -1, nList);

        this.currList = nList;
        this.context = nContext;

        Log.e("MyTag", "Array Adapter");

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View itemView, ViewGroup list)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_listview, list, false);

        Earthquake currEarthquake = currList.get(position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.location);
        TextView txtMag = (TextView) rowView.findViewById(R.id.magnatudeLine);
        TextView txtDepth = (TextView) rowView.findViewById(R.id.depthLine);

        txtTitle.setText(currEarthquake.getLocation());
        txtMag.setText("Magnatude : " + currEarthquake.getMagnitude());
        txtDepth.setText(("Depth : " + currEarthquake.getDepth() + " Km"));

        rowView.setBackgroundColor(Color.parseColor(currEarthquake.getMagColour()));


        return rowView;
    }

    @Override
    public void add(Earthquake newEarthquake) {
        super.add(newEarthquake);

        //currList.add(newEarthquake);
    }
}

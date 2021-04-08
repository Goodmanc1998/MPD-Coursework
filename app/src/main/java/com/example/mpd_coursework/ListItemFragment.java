package com.example.mpd_coursework;
/*
    Name : Connor Goodman
    ID : S1625004
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ListItemFragment extends Fragment implements View.OnClickListener {

    private Earthquake currEarthquake;
    private View fragView;
    private TextView txtTitle, txtMag, txtDepth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragView =  inflater.inflate(R.layout.activity_listview, container, false);

        txtTitle = (TextView) fragView.findViewById(R.id.location);
        txtMag = (TextView) fragView.findViewById(R.id.magnatudeLine);
        txtDepth = (TextView) fragView.findViewById(R.id.depthLine);

        fragView.setOnClickListener(this);

        return fragView;
    }

    public void setCurrEarthquake(Earthquake nEarthquake) {

        if(nEarthquake != null)
        {
            currEarthquake = nEarthquake;

            txtTitle.setText(currEarthquake.getLocation());
            txtMag.setText("Magnatude : " + currEarthquake.getMagnitude());
            txtDepth.setText(("Depth : " + currEarthquake.getDepth() + " Km"));

            fragView.setBackgroundColor(Color.parseColor(currEarthquake.getMagColour()));
        }
        else
        {
            txtTitle.setText("No Available Earthquake");

            txtMag.setText("");
            txtDepth.setText("");

            fragView.setBackgroundColor(Color.WHITE);
        }


    }

    @Override
    public void onClick(View view) {

        Log.e("onItemClicked","Clicked : " + currEarthquake.getTitle());

        Intent nIntent = new Intent(getContext(),ItemView.class);
        nIntent.putExtra("Single", currEarthquake);

        startActivity(nIntent);

    }
}

package com.example.mpd_coursework;
/*
    Name : Connor Goodman
    ID : S1625004
 */
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class ItemView extends AppCompatActivity implements OnClickListener {

    Earthquake currEarthquake = new Earthquake();

    TextView txtTitle, txtDate, txtCategory, txtMagnitude, txtDepth, txtLat, txtLong, txtEqSize;
    RelativeLayout itemParent;
    Button btnMap, btnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        Intent intent = getIntent();
        currEarthquake = (Earthquake) intent.getSerializableExtra("Single");

        itemParent = (RelativeLayout)findViewById(R.id.itemView);
        itemParent.setBackgroundColor(Color.parseColor(currEarthquake.getMagColour()));

        txtTitle = (TextView)findViewById(R.id.title);
        txtTitle.setText(currEarthquake.getLocation());

        SimpleDateFormat sdf = new SimpleDateFormat("'Date :' dd/MM/yyyy 'Time :' HH:mm:ss");

        txtDate = (TextView)findViewById(R.id.date);
        txtDate.setText(sdf.format(currEarthquake.getDateTime()).toString());

        txtCategory = (TextView)findViewById(R.id.category);
        txtCategory.setText(currEarthquake.getCategory());

        txtMagnitude = (TextView)findViewById(R.id.magnatude);
        txtMagnitude.setText("Magnitude : " +currEarthquake.getMagnitude());

        txtDepth = (TextView)findViewById(R.id.depth);
        txtDepth.setText("Depth : " +currEarthquake.getDepth());

        txtLat = (TextView)findViewById(R.id.geoLat);
        txtLat.setText("Lat : " +currEarthquake.getGeoLat());

        txtLong = (TextView)findViewById(R.id.geoLong);
        txtLong.setText("Long : " +currEarthquake.getGeoLat());

        btnLink = (Button)findViewById(R.id.link);
        btnLink.setOnClickListener(this);

        btnMap = (Button)findViewById(R.id.mapButton);
        btnMap.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view == btnMap)
        {
            Intent nIntent = new Intent(getApplicationContext(),MapView.class);
            nIntent.putExtra("Single", currEarthquake);
            startActivity(nIntent);

        }
        else if(view == btnLink)
        {
            Intent nIntent = new Intent(Intent.ACTION_VIEW);
            nIntent.setData(Uri.parse(currEarthquake.getLink()));

            startActivity(nIntent);
        }
    }
}

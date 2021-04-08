package com.example.mpd_coursework;
/*
    Name : Connor Goodman
    ID : S1625004
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapView extends AppCompatActivity implements OnMapReadyCallback {


    Earthquake currEarthquake = new Earthquake();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Intent intent = getIntent();
        currEarthquake = (Earthquake) intent.getSerializableExtra("Single");

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng location = new LatLng(currEarthquake.getGeoLat(), currEarthquake.getGetLong());

        UiSettings settings = googleMap.getUiSettings();

        settings.setZoomControlsEnabled(true);

        googleMap.setMaxZoomPreference(25f);

        googleMap.addMarker(new MarkerOptions().position(location)
                .title(currEarthquake.getLocation())
                .snippet("Mag : " + currEarthquake.getMagnitude() + "  Depth : " + currEarthquake.getDepth() )
                .icon(getIcon(currEarthquake.getMagColour()
                )));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

    }

    public BitmapDescriptor getIcon(String sColour)
    {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(sColour), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.MapMarkers;

public class EstablishmentMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public static final String TAG = "establishment map";

    private GoogleMap googleMap;
    private Establishment establishment;

    public EstablishmentMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        establishment = MainModel.get(getContext()).getSelectedEstablishment();
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MapMarkers mapMarkers = new MapMarkers();
        Marker marker = googleMap.addMarker(mapMarkers.createMarkerOptions(establishment));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(),16.0f);
        googleMap.moveCamera(cameraUpdate);
    }
}

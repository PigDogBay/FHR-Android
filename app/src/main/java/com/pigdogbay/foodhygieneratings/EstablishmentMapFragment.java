package com.pigdogbay.foodhygieneratings;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.Injector;
import com.pigdogbay.foodhygieneratings.model.MapMarkers;

import java.io.IOException;
import java.util.List;

public class EstablishmentMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public static final String TAG = "establishment map";

    private GoogleMap googleMap;
    private Establishment establishment;
    private Marker marker;

    public EstablishmentMapFragment() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        establishment = Injector.INSTANCE.getMainModel().getSelectedEstablishment();
        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(establishment.getBusiness().getName());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MapMarkers mapMarkers = new MapMarkers();
        marker = googleMap.addMarker(mapMarkers.createMarkerOptions(establishment));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(),16.0f);
        googleMap.moveCamera(cameraUpdate);
        startGeocoderWorker();
    }

    private void startGeocoderWorker(){
        new Thread(() -> {
            final LatLng latLng = getGeocoderLocation();
            if (latLng!=null){
                getActivity().runOnUiThread(() -> {
                    marker.setPosition(latLng);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(),16.0f);
                    googleMap.animateCamera(cameraUpdate);
                });
            }
        }).start();
    }

    private LatLng getGeocoderLocation(){
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> result = geocoder.getFromLocationName(establishment.getAddress().flatten(), 1);
            if (result!=null && result.size()>0) {
                return new LatLng(result.get(0).getLatitude(), result.get(0).getLongitude());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}

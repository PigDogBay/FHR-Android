package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.IDataProvider;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.MapMarkers;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    public static final String TAG = "map";
    private GoogleMap googleMap;
    private MapMarkers mapMarkers;
    private IDataProvider getDataProvider(){
        return MainModel.get(getContext()).getDataProvider();
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mapMarkers = new MapMarkers();
        getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnInfoWindowClickListener(this);
        addMarkers();

    }

    private void addMarkers(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Establishment establishment : getDataProvider().getResults()){
            Marker m =  googleMap.addMarker(mapMarkers.createMarkerOptions(establishment));
            m.setTag(establishment);
            builder.include(m.getPosition());
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(),0);
//        googleMap.animateCamera(cameraUpdate);
        googleMap.moveCamera(cameraUpdate);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Establishment establishment = (Establishment) marker.getTag();
        MainModel.get(getContext()).setSelectedEstablishment(establishment);
        ((MainActivity)getActivity()).showDetails();
    }
}

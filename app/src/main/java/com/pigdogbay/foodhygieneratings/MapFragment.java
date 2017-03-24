package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.pigdogbay.foodhygieneratings.model.Coordinate;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FetchState;
import com.pigdogbay.foodhygieneratings.model.IDataProvider;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.MapMarkers;
import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, ObservableProperty.PropertyChangedObserver<FetchState> {

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

    @Override
    public void onResume() {
        super.onResume();
        getDataProvider().getFetchStateProperty().addObserver(this);
        update();
    }

    @Override
    public void onPause() {
        super.onPause();
        getDataProvider().getFetchStateProperty().removeObserver(this);
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
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnInfoWindowClickListener(this);
        LatLngBounds ukBounds = createUKBounds();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(ukBounds, 0);
        googleMap.moveCamera(cameraUpdate);
        update();
    }

    private void addMarkers(){
        List<Establishment> results = getDataProvider().getResults();
        if (results.size()==0){
            //do nothing
        } else if (results.size()==1) {
            Marker m = googleMap.addMarker(mapMarkers.createMarkerOptions(results.get(0)));
            m.setTag(results.get(0));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(m.getPosition(),16.0f);
            googleMap.animateCamera(cameraUpdate);
        } else {
            //fit markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Establishment establishment : results) {
                if (establishment.getCoordinate().isWithinUk()) {
                    Marker m = googleMap.addMarker(mapMarkers.createMarkerOptions(establishment));
                    m.setTag(establishment);
                    builder.include(m.getPosition());
                }
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 0);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Establishment establishment = (Establishment) marker.getTag();
        MainModel.get(getContext()).setSelectedEstablishment(establishment);
        ((MainActivity)getActivity()).showDetails();
    }

    @Override
    public void update(ObservableProperty<FetchState> sender, final FetchState update) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update(update);
            }
        });
    }

    private void update(){
        FetchState state = getDataProvider().getFetchStateProperty().getValue();
        update(state);
    }

    private void update(FetchState state){
        switch (state){
            case ready:
                break;
            case requestingLocationAuthorization:
                break;
            case locating:
                break;
            case foundLocation:
                break;
            case notAuthorizedForLocating:
                break;
            case errorLocating:
                break;
            case loading:
                break;
            case loaded:
                if (googleMap!=null) {
                    addMarkers();
                }
                break;
            case error:
                break;
        }
    }

    private LatLngBounds createUKBounds(){
        LatLng ukSouthWest = new LatLng(Coordinate.ukSouth,Coordinate.ukWest);
        LatLng ukNorthEast = new LatLng(Coordinate.ukNorth,Coordinate.ukEast);
        return new LatLngBounds(ukSouthWest, ukNorthEast);
    }
}

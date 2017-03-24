package com.pigdogbay.foodhygieneratings.model;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Mark on 22/03/2017.
 *
 */
public class MapMarkers
{
    private static final String COLOR_5 = "#00CC00";
    private static final String COLOR_4 = "#66CC00";
    private static final String COLOR_3 = "#CCCC00";
    private static final String COLOR_2 = "#CC6600";
    private static final String COLOR_1 = "#CC0000";
    private static final String COLOR_0 = "#660000";
    private static final String COLOR_DEFAULT = "#0000CC";

    private BitmapDescriptor BD_0, BD_1, BD_2, BD_3, BD_4, BD_5, BD_DEFAULT;

    private BitmapDescriptor getBd5() {
        if (BD_5 == null)
        {
            BD_5 = getBitmapDescriptor(COLOR_5);
        }
        return BD_5;
    }
    private BitmapDescriptor getBd4() {
        if (BD_4 == null)
        {
            BD_4 = getBitmapDescriptor(COLOR_4);
        }
        return BD_4;
    }
    private BitmapDescriptor getBd3() {
        if (BD_3 == null)
        {
            BD_3 = getBitmapDescriptor(COLOR_3);
        }
        return BD_3;
    }
    private BitmapDescriptor getBd2() {
        if (BD_2 == null)
        {
            BD_2 = getBitmapDescriptor(COLOR_2);
        }
        return BD_2;
    }
    private BitmapDescriptor getBd1() {
        if (BD_1 == null)
        {
            BD_1 = getBitmapDescriptor(COLOR_1);
        }
        return BD_1;
    }
    private BitmapDescriptor getBd0() {
        if (BD_0 == null)
        {
            BD_0 = getBitmapDescriptor(COLOR_0);
        }
        return BD_0;
    }
    private BitmapDescriptor getBdDefault() {
        if (BD_DEFAULT == null)
        {
            BD_DEFAULT = getBitmapDescriptor(COLOR_DEFAULT);
        }
        return BD_DEFAULT;
    }

    public MarkerOptions createMarkerOptions(Establishment establishment){
        LatLng coords = new LatLng(establishment.getCoordinate().getLatitude(), establishment.getCoordinate().getLongitude());
        return new MarkerOptions()
                .title(establishment.getBusiness().getName())
                .icon(getMarkerIcon(establishment))
                .snippet(getSnippet(establishment))
                .position(coords);
    }

    private String getSnippet(Establishment establishment){
        return establishment.getRating().getName() + ": " + establishment.getBusiness().getType();
    }

    private BitmapDescriptor getBitmapDescriptor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


    private BitmapDescriptor getMarkerIcon(Establishment establishment){
        switch (establishment.getRating().getRatingValue()){
            case ratingOf0:
                return getBd0();
            case ratingOf1:
                return getBd1();
            case ratingOf2:
                return getBd2();
            case ratingOf3:
                return getBd3();
            case ratingOf4:
                return getBd4();
            case ratingOf5:
                return getBd5();
            case fhis_pass:
                return getBd4();
            case fhis_passEatSafe:
                return getBd5();
            case fhis_improvementRequired:
                return getBd1();
            default:
                return getBdDefault();

        }
    }




}

package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 18/03/2017.
 */

public class Coordinate {

    public static final double ukEast = 1.46;
    public static final double ukWest = -8.638;
    public static final double ukNorth = 60.51;
    public static final double ukSouth = 49.53;


    private final double longitude, latitude;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private static Coordinate emptyCoordinate;
    public static Coordinate getEmptyCoordinate(){
        if (emptyCoordinate==null){
            emptyCoordinate = new Coordinate(0,0);
        }
        return emptyCoordinate;
    }

    public boolean isWithinUk(){
        return latitude>=ukSouth && latitude<=ukNorth && longitude>=ukWest && longitude<=ukEast;
    }
}

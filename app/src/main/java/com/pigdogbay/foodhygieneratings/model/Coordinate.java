package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 18/03/2017.
 */

public class Coordinate {
    private final double longitude, latitude;

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
}

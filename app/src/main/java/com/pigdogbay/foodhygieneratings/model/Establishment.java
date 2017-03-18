package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 17/03/2017.
 */

public class Establishment {

    private final Business business;
    private final Rating rating;
    private final Address address;
    private final LocalAuthority localAuthority;
    private final double distance;
    private final Coordinate coordinate;

    public Establishment(Business business, Rating rating, Address address, LocalAuthority localAuthority, double distance, Coordinate coordinate) {
        this.business = business;
        this.rating = rating;
        this.address = address;
        this.localAuthority = localAuthority;
        this.distance = distance;
        this.coordinate = coordinate;
    }

    public Business getBusiness() {
        return business;
    }

    public Rating getRating() {
        return rating;
    }

    public Address getAddress() {
        return address;
    }

    public LocalAuthority getLocalAuthority() {
        return localAuthority;
    }

    public double getDistance() {
        return distance;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}

package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 17/03/2017.
 */

public class Establishment {

    private String name;
    private String address;

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Establishment(String name, String address){
        this.name = name;
        this.address = address;
    }


}

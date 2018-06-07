package com.pigdogbay.foodhygieneratings.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 18/03/2017.
 */

public class Address {

    private final String line1, line2, line3, line4, postcode;
    private final List<String> address;
    private final boolean isAddressSpecified;

    public String getLine1() {
        return line1;
    }
    public String getLine2() {
        return line2;
    }
    public String getLine3() {
        return line3;
    }
    public String getLine4() {
        return line4;
    }
    public String getPostcode() {
        return postcode;
    }
    public boolean isAddressSpecified() {
        return isAddressSpecified;
    }

    public Address(String line1, String line2, String line3, String line4, String postcode) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.postcode = postcode;

        address = new ArrayList<>();
        if (!line1.isEmpty()){address.add(line1);}
        if (!line2.isEmpty()){address.add(line2);}
        if (!line3.isEmpty()){address.add(line3);}
        if (!line4.isEmpty()){address.add(line4);}
        if (!postcode.isEmpty()){address.add(postcode);}
        if (address.size()==0){
            isAddressSpecified = false;
            address.add("Not specified");
        } else {
            isAddressSpecified = true;
        }
    }

    public String flatten() {
        if (address.size()==0) { return  "";}
        StringBuilder builder = new StringBuilder(address.get(0));
        for (int i=1 ; i<address.size(); i++){
            builder.append(", ");
            builder.append(address.get(i));
        }
        return builder.toString();

    }
}

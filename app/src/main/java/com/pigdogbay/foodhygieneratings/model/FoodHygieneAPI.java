package com.pigdogbay.foodhygieneratings.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 19/03/2017.
 */
public class FoodHygieneAPI {

    public static List<Establishment> parseEstablishments(JSONObject jsonObject) throws JSONException {
        ArrayList<Establishment> results = new ArrayList<>();
        JSONArray estamblishmentsJsonArray = jsonObject.getJSONArray("establishments");
        for (int i=0; i<estamblishmentsJsonArray.length(); i++){
            JSONObject estObject = estamblishmentsJsonArray.getJSONObject(i);
            results.add(parseEstablishment(estObject));
        }
        return results;
    }

    private static Establishment parseEstablishment(JSONObject establishmentObject) throws JSONException {
        Business business = parseBusiness(establishmentObject);
        Rating rating = parseRating(establishmentObject);
        LocalAuthority localAuthority = parseLocalAuthority(establishmentObject);
        Address address = parseAddress(establishmentObject);
        Coordinate coordinate = parseCoordinate(establishmentObject);
        double distance = parseDistance(establishmentObject);
        Establishment establishment = new Establishment(business,rating,address,localAuthority,distance,coordinate);
        return establishment;
    }

    private static Business parseBusiness(JSONObject estObject) throws JSONException{
        String name = estObject.getString("BusinessName");
        String businessType = estObject.getString("BusinessType");
        int businessTypeId = estObject.getInt("BusinessTypeID");
        int fhrsId = estObject.getInt("FHRSID");
        return new Business(name,businessType,businessTypeId,fhrsId);
    }
    private static Rating parseRating(JSONObject estObject) throws JSONException{
        return null;
    }
    private static Address parseAddress(JSONObject estObject) throws JSONException{
        String line1 = estObject.getString("AddressLine1");
        String line2 = estObject.getString("AddressLine2");
        String line3 = estObject.getString("AddressLine3");
        String line4 = estObject.getString("AddressLine4");
        String postcode = estObject.getString("PostCode");
        return new Address(line1,line2,line3,line4,postcode);
    }
    private static LocalAuthority parseLocalAuthority(JSONObject estObject) throws JSONException{
        return null;
    }
    private static Coordinate parseCoordinate(JSONObject estObject) throws JSONException{
        return null;
    }
    private static double parseDistance(JSONObject estObject){
        try {
            return estObject.getDouble("Distance");
        } catch (JSONException e) {
        }
        return 0.0;
    }



}

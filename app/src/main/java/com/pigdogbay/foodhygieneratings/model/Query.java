package com.pigdogbay.foodhygieneratings.model;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark on 20/03/2017.
 *
 */
public class Query
{
    private Map<String,String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public Query(){
    }

    public Query(double longitude, double latitude, int radiusInMiles){
        addSearchParameter("longitude", String.valueOf(longitude));
        addSearchParameter("latitude", String.valueOf(latitude));
        addSearchParameter("maxDistanceLimit", String.valueOf(radiusInMiles));
    }

    public Query setBusinessName(String name){
        addSearchParameter("name",name);
        return this;
    }
    public Query setPlaceName(String place){
        addSearchParameter("address",place);
        return this;
    }
    public Query setLocalAuthorityId(String localAuthorityId){
        addSearchParameter("localAuthorityId",localAuthorityId);
        return this;
    }
    public Query setRatingKey(String ratingKey){
        addSearchParameter("ratingKey",ratingKey);
        return this;
    }
    public Query setRatingOperatorKey(String ratingOperatorKey){
        addSearchParameter("ratingOperatorKey",ratingOperatorKey);
        return this;
    }
    public Query setBusinessTypeId(String businessTypeId){
        addSearchParameter("businessTypeId",businessTypeId);
        return this;
    }


    private void addSearchParameter(String name, String value){
        if (name!=null && value!=null && !value.isEmpty()){
            map.put(name,value);
        }
    }
}

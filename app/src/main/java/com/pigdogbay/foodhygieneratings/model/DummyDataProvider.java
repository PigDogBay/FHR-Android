package com.pigdogbay.foodhygieneratings.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Mark on 20/03/2017.
 */
public class DummyDataProvider implements MainModel.IDataProvider {

    private final String jsonData;

    public DummyDataProvider(String jsonData) {
        this.jsonData = jsonData;
    }
    @Override
    public List<Establishment> findEstablishments(Query query) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(jsonData);
        return FoodHygieneAPI.parseEstablishments(jsonObject);

    }
}

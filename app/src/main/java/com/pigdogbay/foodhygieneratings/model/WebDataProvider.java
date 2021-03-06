package com.pigdogbay.foodhygieneratings.model;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mark on 26/03/2017.
 *
 */
class WebDataProvider implements MainModel.IDataProvider {

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private int timeout = 30*1000;

    @Override
    public List<Establishment> findEstablishments(Query query) throws IOException, JSONException, ParseException {
        Uri uri = WebServiceClient.createQueryUri(query);
        final JSONObject json = WebServiceClient.getJson(uri, timeout);
        return FoodHygieneAPI.parseEstablishments(json);
    }
}

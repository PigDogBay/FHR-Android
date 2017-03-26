package com.pigdogbay.foodhygieneratings.model;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mark on 26/03/2017.
 *
 */
class WebServiceClient {

    private static final String ENDPOINT = "http://api.ratings.food.gov.uk/establishments/";

    static Uri createQueryUri(Query query){
        final Uri.Builder builder = Uri.parse(ENDPOINT).buildUpon();
        for (String key : query.getMap().keySet()){
            builder.appendQueryParameter(key,query.getMap().get(key));
        }
        return builder.build();
    }

    static JSONObject getJson(Uri uri) throws IOException, JSONException {

        URL url = new URL(uri.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.addRequestProperty("x-api-version","2");
        connection.addRequestProperty("accept","application/json");
        connection.addRequestProperty("content-type","application/json");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine())!=null){
                sb.append(line+"\n");
            }
            reader.close();
            return new JSONObject(sb.toString());

        } finally {
            connection.disconnect();
        }


    }

}

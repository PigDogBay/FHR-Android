package com.pigdogbay.foodhygieneratings.model;

import android.net.Uri;

import com.pigdogbay.lib.utils.ObservableProperty;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 26/03/2017.
 *
 */
class WebDataProvider implements IDataProvider {

    private ObservableProperty<FetchState> fetchStateObservableProperty;
    private List<Establishment> results;
    private Coordinate coordinate;
    private boolean isBusy = false;

    WebDataProvider(){
        fetchStateObservableProperty = new ObservableProperty<>(FetchState.ready);
        results = new ArrayList<>();
        coordinate = Coordinate.getEmptyCoordinate();

    }
    @Override
    public ObservableProperty<FetchState> getFetchStateProperty() {
        return fetchStateObservableProperty;
    }

    @Override
    public List<Establishment> getResults() {
        return results;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean findLocalEstablishments() {
        if (isBusy){
            return false;
        }
        isBusy = true;
        results.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchStateObservableProperty.setValue(FetchState.locating);
                fetchStateObservableProperty.setValue(FetchState.foundLocation);
                Query query = new Query(-2.204094,52.984120,1);
                search(query);
                isBusy = false;
            }
        }).start();
        return true;
    }

    @Override
    public boolean findEstablishments(Query query) {
        if (isBusy){
            return false;
        }
        isBusy = true;
        return false;
    }

    private void search(Query query){
        fetchStateObservableProperty.setValue(FetchState.loading);
        Uri uri = WebServiceClient.createQueryUri(query);
        try {
            final JSONObject json = WebServiceClient.getJson(uri);
            this.results = FoodHygieneAPI.parseEstablishments(json);
            fetchStateObservableProperty.setValue(FetchState.loaded);
        } catch (Exception e) {
            fetchStateObservableProperty.setValue(FetchState.error);
        }
    }

}

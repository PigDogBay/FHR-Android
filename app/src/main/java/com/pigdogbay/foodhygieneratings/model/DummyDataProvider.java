package com.pigdogbay.foodhygieneratings.model;

import com.pigdogbay.lib.utils.ObservableProperty;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 20/03/2017.
 *
 */
public class DummyDataProvider implements IDataProvider {

    private ObservableProperty<FetchState> fetchStateObservableProperty;
    private List<Establishment> results;
    private Coordinate coordinate;
    private final String jsonData;
    private boolean isBusy = false;

    public DummyDataProvider(String jsonData){
        this.jsonData = jsonData;
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
        fetchStateObservableProperty.setValue(FetchState.ready);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchStateObservableProperty.setValue(FetchState.locating);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Stoke
                coordinate = new Coordinate(-2.204094,52.984120);
                fetchStateObservableProperty.setValue(FetchState.foundLocation);
                search();
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
        fetchStateObservableProperty.setValue(FetchState.ready);
        new Thread(new Runnable() {
            @Override
            public void run() {
                search();
                isBusy = false;
            }
        }).start();
        return true;
    }

    private void search(){
        try {
            fetchStateObservableProperty.setValue(FetchState.loading);
            JSONObject jsonObject = new JSONObject(jsonData);
            Thread.sleep(1000);
            results = FoodHygieneAPI.parseEstablishments(jsonObject);
            fetchStateObservableProperty.setValue(FetchState.loaded);

        } catch (Exception e) {
            e.printStackTrace();
            fetchStateObservableProperty.setValue(FetchState.error);
        }

    }
}

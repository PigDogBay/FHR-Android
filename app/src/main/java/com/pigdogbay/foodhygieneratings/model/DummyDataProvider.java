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

    private ObservableProperty<AppState> appStateObservableProperty;
    private List<Establishment> results;
    private Coordinate coordinate;
    private final String jsonData;
    private boolean isBusy = false;

    public DummyDataProvider(String jsonData){
        this.jsonData = jsonData;
        appStateObservableProperty = new ObservableProperty<>(AppState.ready);
        results = new ArrayList<>();
        coordinate = Coordinate.getEmptyCoordinate();
    }

    @Override
    public ObservableProperty<AppState> getAppStateProperty() {
        return appStateObservableProperty;
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
        appStateObservableProperty.setValue(AppState.ready);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appStateObservableProperty.setValue(AppState.locating);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Stoke
                coordinate = new Coordinate(-2.204094,52.984120);
                appStateObservableProperty.setValue(AppState.foundLocation);
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
        appStateObservableProperty.setValue(AppState.ready);
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
            appStateObservableProperty.setValue(AppState.loading);
            JSONObject jsonObject = new JSONObject(jsonData);
            Thread.sleep(1000);
            results = FoodHygieneAPI.parseEstablishments(jsonObject);
            appStateObservableProperty.setValue(AppState.loaded);

        } catch (Exception e) {
            e.printStackTrace();
            appStateObservableProperty.setValue(AppState.error);
        }

    }
}

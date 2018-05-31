package com.pigdogbay.foodhygieneratings.model;

import android.content.Context;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.lib.utils.ActivityUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Injector {

    private boolean isBuilt = false;
    private static Injector injector;
    private MainModel mainModel;
    private List<LocalAuthority> localAuthorities;

    public static MainModel getMainModel() {
        return injector.mainModel;
    }

    public static Injector getInjector() {
        if (injector == null){
            injector = new Injector();
        }
        return injector;
    }

    public void build(Context context){
        if (isBuilt){
            return;
        }
        isBuilt = true;

        Context applicationContext = context.getApplicationContext();

        mainModel = new MainModel();
        mainModel.setDataProvider(dummyDataProvider(applicationContext));
        //mainModel.setDataProvider(new WebDataProvider());
    }

    private MainModel.IDataProvider dummyDataProvider(Context context){
        try {
            String data = ActivityUtils.readResource(context, R.raw.stoke);
            return new DummyDataProvider(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocalAuthority> getLocalAuthorities(Context context){
        if (localAuthorities==null) {
            try {
                String data = ActivityUtils.readResource(context.getApplicationContext(), R.raw.authorities);
                JSONObject jsonObject = new JSONObject(data);
                localAuthorities = FoodHygieneAPI.parseAuthorities(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                localAuthorities =  new ArrayList<>();
            }
        }
        return localAuthorities;
    }



}

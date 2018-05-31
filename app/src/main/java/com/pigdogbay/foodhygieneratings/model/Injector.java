package com.pigdogbay.foodhygieneratings.model;

import android.content.Context;

public class Injector {

    private boolean isBuilt = false;
    private static Injector injector;
    private MainModel mainModel;

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

        mainModel = new MainModel(applicationContext);
    }

}

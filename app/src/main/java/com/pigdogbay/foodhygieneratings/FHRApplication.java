package com.pigdogbay.foodhygieneratings;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Mark on 28/03/2017.
 *
 */

public class FHRApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);    }
}

package com.pigdogbay.foodhygieneratings;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Created by Mark on 28/03/2017.
 *
 */
public class FHRApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //need this to use vector drawables in pre-lollipop (v21) devices
        //However there is a potential memory leak issue
        //http://stackoverflow.com/questions/35761636/is-it-possible-to-use-vectordrawable-in-buttons-and-textviews-using-androiddraw
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}

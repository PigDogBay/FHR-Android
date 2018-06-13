package com.pigdogbay.foodhygieneratings.model

import android.content.Context
import com.google.android.gms.location.places.Places

import com.pigdogbay.foodhygieneratings.R
import com.pigdogbay.foodhygieneratings.places.DummyPlaceFactory
import com.pigdogbay.foodhygieneratings.places.GooglePlaceFactory
import com.pigdogbay.foodhygieneratings.places.GooglePlaceImage
import com.pigdogbay.foodhygieneratings.places.IPlaceFactory
import com.pigdogbay.lib.utils.ActivityUtils
import com.pigdogbay.lib.utils.BitmapUtils
import com.pigdogbay.lib.utils.PreferencesHelper

import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList

object Injector {

    private var isBuilt = false
    lateinit var mainModel: MainModel
    lateinit var settings: Settings
    lateinit var placeFactory : IPlaceFactory
    private var localAuthorities: List<LocalAuthority>? = null

    fun build(context: Context) {
        if (isBuilt) {
            return
        }
        isBuilt = true

        val applicationContext = context.applicationContext
        val preferencesHelper = PreferencesHelper(applicationContext)
        settings = Settings(preferencesHelper)

        mainModel = MainModel()
//        mainModel.dataProvider = dummyDataProvider(applicationContext)
        mainModel.dataProvider = WebDataProvider()

        //placeFactory = DummyPlaceFactory(BitmapUtils.getBitmap(context,R.drawable.fhis_pass_and_eat_safe))
        placeFactory = GooglePlaceFactory(Places.getGeoDataClient(context))

    }

    private fun dummyDataProvider(context: Context): MainModel.IDataProvider {
        try {
            val data = ActivityUtils.readResource(context, R.raw.stoke)
            return DummyDataProvider(data)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    fun getLocalAuthorities(context: Context): List<LocalAuthority> {
        if (localAuthorities == null) {
            try {
                val data = ActivityUtils.readResource(context.applicationContext, R.raw.authorities)
                val jsonObject = JSONObject(data)
                localAuthorities = FoodHygieneAPI.parseAuthorities(jsonObject)
            } catch (e: Exception) {
                e.printStackTrace()
                localAuthorities = ArrayList()
            }

        }
        return localAuthorities!!
    }
}

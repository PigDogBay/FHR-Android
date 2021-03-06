package com.pigdogbay.foodhygieneratings.model

import android.content.Context

import com.pigdogbay.foodhygieneratings.R
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
        mainModel.dataProvider = WebDataProvider()
    }

    fun createFetcher(context: Context) : IPlaceFetcher {
        return DummyPlace(BitmapUtils.getBitmap(context, R.drawable.fhis_pass_and_eat_safe))
    }

    private fun dummyDataProvider(context: Context): MainModel.IDataProvider {
        try {
            val data = ActivityUtils.readResource(context, R.raw.stoke)
            return DummyDataProvider(data)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    /*
        To update R.raw.authorities, go to
        http://api.ratings.food.gov.uk/Help/Api/GET-Authorities-basic
        Press test API and copy JSON into https://jsonformatter.org/json-pretty-print

        To get a more detailed authorities info see
        http://ratings.food.gov.uk/authorities/json

     */
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

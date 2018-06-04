package com.pigdogbay.foodhygieneratings.model

import android.content.Context

import com.pigdogbay.foodhygieneratings.R
import com.pigdogbay.lib.utils.ActivityUtils
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

        //val dataProvider = dummyDataProvider(applicationContext)
        val dataProvider = WebDataProvider()
        mainModel = MainModel()
        mainModel.setDataProvider(dataProvider)
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

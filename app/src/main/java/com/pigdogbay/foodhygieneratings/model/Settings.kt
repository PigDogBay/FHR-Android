package com.pigdogbay.foodhygieneratings.model

import com.pigdogbay.foodhygieneratings.R
import com.pigdogbay.lib.utils.PreferencesHelper

class Settings(private val preferencesHelper: PreferencesHelper) {

    var searchRadius : Double
        get() = preferencesHelper.getDouble(R.string.key_pref_search_radius,1.0)
        set(value){preferencesHelper.setDouble(R.string.key_pref_search_radius,value)}

    var searchTimeout : Int
        get() = preferencesHelper.getInt(R.string.key_pref_search_timeout,30)
        set(value){preferencesHelper.setInt(R.string.key_pref_search_timeout,value)}

    var isTextFilterNameAndAddress : Boolean
        get() = preferencesHelper.getInt(R.string.key_pref_search_results_text_filter,0)!=0
        set(value){
            val filterType = if (value) 1 else 0
            preferencesHelper.setInt(R.string.key_pref_search_results_text_filter,filterType)
        }
}
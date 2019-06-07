package com.pigdogbay.foodhygieneratings

import android.app.Activity
import android.os.Bundle
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.net.MalformedURLException
import java.net.URL

/*
EU Consent add this dependency to the apps gradle file
    implementation 'com.google.android.ads.consent:consent-library:1.0.7'

 */
class Ads(private val activity : Activity){

    private val adView : AdView = activity.findViewById(R.id.adView)
    private val publisherIds = arrayOf("pub-3582986480189311")
    private val adId = "ca-app-pub-3582986480189311~2972071182"
    private lateinit var form: ConsentForm


    //Call from Activities onCreate
    fun showAds(){
        MobileAds.initialize(activity,adId)
        checkConsent()
    }

    fun onRestart(){
        adView.resume()
    }

    fun onPause(){
        adView.pause()
    }

    private fun checkConsent(){
        val consentInformation = ConsentInformation.getInstance(activity)
        consentInformation.addTestDevice(activity.getString(R.string.code_test_device_acer_tablet))
        consentInformation.addTestDevice(activity.getString(R.string.code_test_device_moto_g))
        consentInformation.addTestDevice(activity.getString(R.string.code_test_device_nokia_6))
        //consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                when (consentStatus) {
                    ConsentStatus.UNKNOWN -> showConsentForm()
                    ConsentStatus.NON_PERSONALIZED -> requestAd(true)
                    ConsentStatus.PERSONALIZED -> requestAd(false)
                }
            }
            override fun onFailedToUpdateConsentInfo(reason: String) {
                requestAd(false)
            }
        })

    }

    private fun requestAd(useNonPersonalizedAds : Boolean){
        val extras = Bundle()
        extras.putString("max_ad_content_rating", "G")
        if (useNonPersonalizedAds) {
            extras.putString("npa","1")
        }
        //MA = Mature Adult, may improve eCPM?
        //extras.putString("max_ad_content_rating", "MA");
        val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(activity.getString(R.string.code_test_device_acer_tablet))
                .addTestDevice(activity.getString(R.string.code_test_device_moto_g))
                .addTestDevice(activity.getString(R.string.code_test_device_nokia_6))
                .build()
        adView.loadAd(adRequest)
    }

    private fun showConsentForm() {
        var privacyUrl: URL? = null
        try {
            privacyUrl = URL(activity.getString(R.string.privacy_policy_url))
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        form = ConsentForm.Builder(activity, privacyUrl)
        .withListener(object : ConsentFormListener() {
            override fun onConsentFormLoaded() {
                form.show()
            }

            override fun onConsentFormOpened() {
            }

            override fun onConsentFormClosed(
                    consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                ConsentInformation.getInstance(activity).consentStatus = consentStatus
                requestAd(consentStatus==ConsentStatus.NON_PERSONALIZED)
            }

            override fun onConsentFormError(errorDescription: String?) {
                requestAd(false)
            }
        })
        .withPersonalizedAdsOption()
        .withNonPersonalizedAdsOption()
        .build()

        form.load()
    }


}
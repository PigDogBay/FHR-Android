package com.pigdogbay.foodhygieneratings.places

import android.graphics.Bitmap
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoMetadataResponse
import com.pigdogbay.lib.utils.ObservableProperty
import com.pigdogbay.foodhygieneratings.model.Establishment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.LatLng

data class MBPlace(val id : String, val telephone : String, val web : String, val rating : Float, val images : List<IPlaceImage>)

interface IPlaceImage {
    enum class ImageStatus {
        Uninitialized, Fetching, Ready, Error
    }
    val attribution : String
    val observableStatus : ObservableProperty<ImageStatus>
    val bitmap : Bitmap?
    fun fetchBitmap()
}

interface IPlaceFactory{
    fun createPlace(establishment: Establishment, callback: (mbPlace : MBPlace?) -> Unit)
}


class DummyPlaceImage(override val attribution: String, private val srcBitmap: Bitmap?) : IPlaceImage{
    private val status = ObservableProperty<IPlaceImage.ImageStatus>(IPlaceImage.ImageStatus.Uninitialized)

    override val bitmap: Bitmap?
        get() = when (observableStatus.value){
            IPlaceImage.ImageStatus.Uninitialized -> null
            IPlaceImage.ImageStatus.Fetching -> null
            IPlaceImage.ImageStatus.Ready -> srcBitmap
            IPlaceImage.ImageStatus.Error -> null
        }

    override val observableStatus: ObservableProperty<IPlaceImage.ImageStatus>
        get() = status

    override fun fetchBitmap() {
        if (observableStatus.value == IPlaceImage.ImageStatus.Uninitialized) {
            Thread({
                observableStatus.value = IPlaceImage.ImageStatus.Fetching
                Thread.sleep(2000L)
                //observableStatus.value = IPlaceImage.ImageStatus.Error
                observableStatus.value = IPlaceImage.ImageStatus.Ready
            }).start()
        }
    }
}

class DummyPlaceFactory(val bitmap: Bitmap) : IPlaceFactory {
    override fun createPlace(establishment: Establishment, callback: (mbPlace: MBPlace?) -> Unit) {
        Thread {
            val images: ArrayList<IPlaceImage> = ArrayList()
            images.add(DummyPlaceImage("Dummy Photographer", bitmap))
            val place = MBPlace("ChIJYUPe12BoekgR6iLyIiC9DJk", "+44 1782 865646", "http://www.omg-restaurant.co.uk", 3.5F, images)
            Thread.sleep(500L)
            callback(place)
        }.start()
    }
}

class GooglePlaceImage(private val geoDataClient: GeoDataClient, private val metaData: PlacePhotoMetadata) : IPlaceImage {
    private val status = ObservableProperty<IPlaceImage.ImageStatus>(IPlaceImage.ImageStatus.Uninitialized)
    private var srcBitmap : Bitmap? = null

    override val attribution: String
        get() = metaData.attributions.toString()
    override val observableStatus: ObservableProperty<IPlaceImage.ImageStatus>
        get() = status
    override val bitmap: Bitmap?
        get() = srcBitmap

    override fun fetchBitmap() {
        val scaledPhotoResult = geoDataClient.getScaledPhoto(metaData, 640, 320)
        scaledPhotoResult.addOnFailureListener({ e -> status.value = IPlaceImage.ImageStatus.Error })
        scaledPhotoResult.addOnSuccessListener({ result ->
            srcBitmap = result.bitmap
            status.value = IPlaceImage.ImageStatus.Ready
        })
    }
}

class GooglePlaceFactory(private val geoDataClient: GeoDataClient) : IPlaceFactory {

    override fun createPlace(establishment: Establishment, callback: (mbPlace: MBPlace?) -> Unit) {
        val bounds = createBounds(establishment)
        val findTask = geoDataClient.getAutocompletePredictions(establishment.business.name, bounds, GeoDataClient.BoundsMode.STRICT,null)
        //Fetch a list of matching places
        findTask.addOnSuccessListener { response ->
            val predictions = DataBufferUtils.freezeAndClose(response)
            if (predictions.size>0) {
                //Fetch details of the first place
                val placeTask = geoDataClient.getPlaceById(predictions[0].placeId)
                placeTask.addOnSuccessListener { placeResponse ->
                    val places = DataBufferUtils.freezeAndClose(placeResponse)
                    if (places.size>0){
                        //Fetch image meta data for the place
                        val metadataTask = geoDataClient.getPlacePhotos(places[0].id)
                        metadataTask.addOnSuccessListener { metadataResponse ->
                            //All done fetching, put it all together and send it off to the client
                            foundPlaceAndImages(places[0], metadataResponse, callback)
                        }
                    }
                }
            }
        }
    }

    private fun foundPlaceAndImages(place : Place, ppmr: PlacePhotoMetadataResponse, callback: (mbPlace: MBPlace?) -> Unit){
        val placePhotoMetadataList = DataBufferUtils.freezeAndClose(ppmr.photoMetadata)
        val placeImages : List<IPlaceImage> = placePhotoMetadataList.map{ it -> GooglePlaceImage(geoDataClient,it)}
        val phone = if (place.phoneNumber==null) "" else place.phoneNumber.toString()
        val web = if (place.websiteUri==null) "" else place.websiteUri.toString()
        val mbPlace = MBPlace(place.id,phone,web,place.rating,placeImages)
        callback(mbPlace)
    }

    /*
       Returns bounds of about 0.25 mile radius about the establishment
       1° longitude ~ 66 miles, 1° latitude ~ 69 miles, so for at least quarter mile radius
     */
    private fun createBounds(establishment: Establishment) : LatLngBounds {
        val latitude = establishment.coordinate.latitude
        val longitude = establishment.coordinate.longitude
        val delta = 0.005
        val northeast = LatLng(latitude + delta, longitude + delta)
        val southwest = LatLng(latitude - delta, longitude - delta)
        return LatLngBounds(southwest, northeast)
    }

}
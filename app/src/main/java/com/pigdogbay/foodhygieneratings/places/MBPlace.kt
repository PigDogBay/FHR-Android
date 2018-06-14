package com.pigdogbay.foodhygieneratings.places

import android.graphics.Bitmap
import com.pigdogbay.lib.patterns.ObservableProperty
import com.pigdogbay.foodhygieneratings.model.Establishment

data class MBPlace(val id : String, val telephone : String, val web : String, val rating : Float, val images : List<IPlaceImage>)
enum class FetchStatus {
    Uninitialized, Fetching, Ready, Error
}
interface IPlaceImage {
    val attribution : String
    val observableStatus : ObservableProperty<FetchStatus>
    val bitmap : Bitmap?
    fun fetchBitmap()
}

interface IPlaceFetcher{
    val observableStatus : ObservableProperty<FetchStatus>
    val mbPlace : MBPlace?
    fun fetch(establishment: Establishment)
}


class DummyPlaceImage(override val attribution: String, private val srcBitmap: Bitmap?) : IPlaceImage{
    private val status = ObservableProperty(this, FetchStatus.Uninitialized)

    override val bitmap: Bitmap?
        get() = when (observableStatus.value){
            FetchStatus.Uninitialized -> null
            FetchStatus.Fetching -> null
            FetchStatus.Ready -> srcBitmap
            FetchStatus.Error -> null
        }

    override val observableStatus: ObservableProperty<FetchStatus>
        get() = status

    override fun fetchBitmap() {
        if (observableStatus.value == FetchStatus.Uninitialized) {
            Thread{
                observableStatus.value = FetchStatus.Fetching
                Thread.sleep(2000L)
                observableStatus.value = FetchStatus.Ready
            }.start()
        }
    }
}

class DummyPlace(val bitmap: Bitmap) : IPlaceFetcher {

    private var srcMBPlace : MBPlace? = null

    private val status = ObservableProperty(this, FetchStatus.Uninitialized)
    override val observableStatus: ObservableProperty<FetchStatus>
        get() = status

    override val mbPlace: MBPlace?
        get() = srcMBPlace

    override fun fetch(establishment: Establishment) {
        if (observableStatus.value == FetchStatus.Uninitialized) {
            Thread {
                observableStatus.value = FetchStatus.Fetching
                val images: ArrayList<IPlaceImage> = ArrayList()
                images.add(DummyPlaceImage("Dummy Photographer", bitmap))
                Thread.sleep(500L)
                srcMBPlace = MBPlace("ChIJYUPe12BoekgR6iLyIiC9DJk", "+44 1782 865646", "http://www.omg-restaurant.co.uk", 3.5F, images)
                observableStatus.value = FetchStatus.Ready
            }.start()
        }
    }
}
//class GooglePlaceImage(private val geoDataClient: GeoDataClient, private val metaData: PlacePhotoMetadata) : IPlaceImage {
//    private val status = ObservableProperty<FetchStatus>(FetchStatus.Uninitialized)
//    private var srcBitmap : Bitmap? = null
//
//    override val attribution: String
//        get() = metaData.attributions.toString()
//    override val observableStatus: ObservableProperty<FetchStatus>
//        get() = status
//    override val bitmap: Bitmap?
//        get() = srcBitmap
//
//    override fun fetchBitmap() {
//        val scaledPhotoResult = geoDataClient.getScaledPhoto(metaData, 640, 320)
//        scaledPhotoResult.addOnFailureListener({ e -> status.value = FetchStatus.Error })
//        scaledPhotoResult.addOnSuccessListener({ result ->
//            srcBitmap = result.bitmap
//            status.value = FetchStatus.Ready
//        })
//    }
//}
//
//class GooglePlaceFactory(private val geoDataClient: GeoDataClient) : IPlaceFetcher {
//
//    override fun createPlace(establishment: Establishment, callback: (mbPlace: MBPlace?) -> Unit) {
//        val bounds = createBounds(establishment)
//        val findTask = geoDataClient.getAutocompletePredictions(establishment.business.name, bounds, GeoDataClient.BoundsMode.STRICT,null)
//        //Fetch a list of matching places
//        findTask.addOnSuccessListener { response ->
//            val predictions = DataBufferUtils.freezeAndClose(response)
//            if (predictions.size>0) {
//                //Fetch details of the first place
//                val placeTask = geoDataClient.getPlaceById(predictions[0].placeId)
//                placeTask.addOnSuccessListener { placeResponse ->
//                    val places = DataBufferUtils.freezeAndClose(placeResponse)
//                    if (places.size>0){
//                        //Fetch image meta data for the place
//                        val metadataTask = geoDataClient.getPlacePhotos(places[0].id)
//                        metadataTask.addOnSuccessListener { metadataResponse ->
//                            //All done fetching, put it all together and send it off to the client
//                            foundPlaceAndImages(places[0], metadataResponse, callback)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun foundPlaceAndImages(place : Place, ppmr: PlacePhotoMetadataResponse, callback: (mbPlace: MBPlace?) -> Unit){
//        val placePhotoMetadataList = DataBufferUtils.freezeAndClose(ppmr.photoMetadata)
//        val placeImages : List<IPlaceImage> = placePhotoMetadataList.map{ it -> GooglePlaceImage(geoDataClient,it)}
//        val phone = if (place.phoneNumber==null) "" else place.phoneNumber.toString()
//        val web = if (place.websiteUri==null) "" else place.websiteUri.toString()
//        val mbPlace = MBPlace(place.id,phone,web,place.rating,placeImages)
//        callback(mbPlace)
//    }
//
//    /*
//       Returns bounds of about 0.25 mile radius about the establishment
//       1° longitude ~ 66 miles, 1° latitude ~ 69 miles, so for at least quarter mile radius
//     */
//    private fun createBounds(establishment: Establishment) : LatLngBounds {
//        val latitude = establishment.coordinate.latitude
//        val longitude = establishment.coordinate.longitude
//        val delta = 0.005
//        val northeast = LatLng(latitude + delta, longitude + delta)
//        val southwest = LatLng(latitude - delta, longitude - delta)
//        return LatLngBounds(southwest, northeast)
//    }
//
//}

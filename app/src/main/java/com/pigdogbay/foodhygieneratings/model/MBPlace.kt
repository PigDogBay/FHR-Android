package com.pigdogbay.foodhygieneratings.model

//import android.content.res.Resources
import android.graphics.Bitmap
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.LatLngBounds
//import com.google.android.libraries.places.api.model.*
//import com.google.android.libraries.places.api.net.FetchPhotoRequest
//import com.google.android.libraries.places.api.net.FetchPlaceRequest
//import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
//import com.google.android.libraries.places.api.net.PlacesClient
import com.pigdogbay.lib.patterns.ObservableProperty

data class MBPlace(val id : String, val telephone : String, val web : String, val rating : Double?, val images : List<IPlaceImage>)
enum class FetchStatus {
    Uninitialized, Fetching, Ready, Error
}
interface IPlaceImage {
    val attribution : String
    val observableStatus : ObservableProperty<FetchStatus>
    val bitmap : Bitmap?
    var index : Int
    fun fetchBitmap()
}

interface IPlaceFetcher{
    val observableStatus : ObservableProperty<FetchStatus>
    val mbPlace : MBPlace?
    fun fetch(establishment: Establishment)
}


class DummyPlaceImage(override val attribution: String, private val srcBitmap: Bitmap?) : IPlaceImage {
    override var index: Int = 0
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
                images.add(DummyPlaceImage("<a href=\"https://maps.google.com/maps/contrib/110455252459989089441/photos\">OMG Grill</a>", bitmap))
                Thread.sleep(500L)
                srcMBPlace = MBPlace("ChIJYUPe12BoekgR6iLyIiC9DJk", "07591 755084", "http://www.omg-restaurant.co.uk", 3.5, images)
                observableStatus.value = FetchStatus.Ready
            }.start()
        }
    }
}
/*
class GooglePlaceImage(private val placesClient: PlacesClient, private val metaData: PhotoMetadata) : IPlaceImage {
    override var index: Int = 0
    private val status = ObservableProperty(this, FetchStatus.Uninitialized)
    private var srcBitmap : Bitmap? = null

    override val attribution: String
        get() = metaData.attributions

    override val observableStatus: ObservableProperty<FetchStatus>
        get() = status

    override val bitmap: Bitmap?
        get() = srcBitmap

    override fun fetchBitmap() {
        status.value = FetchStatus.Fetching
        //set width to be 80% of the screen width
        val width =  (Resources.getSystem().displayMetrics.widthPixels*80)/100
        val request = FetchPhotoRequest.builder(metaData)
                .setMaxWidth(width)
                .build()
        placesClient.fetchPhoto(request)
                .addOnSuccessListener {
                    response ->
                        srcBitmap = response.bitmap
                        status.value = FetchStatus.Ready
                }
                .addOnFailureListener{status.value = FetchStatus.Error}
    }
}

class GooglePlaceFetcher(private val placesClient: PlacesClient) : IPlaceFetcher {
    private val status = ObservableProperty(this, FetchStatus.Uninitialized)
    private var foundPlace : MBPlace? = null

    override val observableStatus: ObservableProperty<FetchStatus>
        get() = status
    override val mbPlace: MBPlace?
        get() = foundPlace

     /**
     * To fetch a place requires 2 tasks:
     * Find matching places (only first one is used)
     * Fetch place details
     *
     * This information is then collated into MBPlace
     */
    override fun fetch(establishment: Establishment) {
        status.value = FetchStatus.Fetching
        val bounds = createBounds(establishment)
        val request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(RectangularBounds.newInstance(bounds))
                .setQuery(establishment.business.name)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setCountry("uk")
                .build()

        //First Task is to find a list of places that match the name and location
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener {
                    response ->
                    if (response.autocompletePredictions.size>0){
                        fetchPlace(response.autocompletePredictions[0])
                    } else {
                        status.value = FetchStatus.Error
                    }
                }
                .addOnFailureListener{
                    status.value = FetchStatus.Error
                }
    }

    private fun fetchPlace(prediction: AutocompletePrediction) {
        val placeFields = listOf(Place.Field.NAME, Place.Field.WEBSITE_URI, Place.Field.PHONE_NUMBER, Place.Field.RATING, Place.Field.PHOTO_METADATAS)
        val request = FetchPlaceRequest.builder(prediction.placeId,placeFields).build()
        placesClient.fetchPlace(request)
                .addOnSuccessListener {
                    response ->
                    val place = response.place
                    val phone = if (place.phoneNumber==null) "" else place.phoneNumber.toString().replace("+44 ","0")
                    val web = if (place.websiteUri==null) "" else place.websiteUri.toString()
                    val placeImages : List<IPlaceImage> = place.photoMetadatas?.map{ GooglePlaceImage(placesClient,it)} ?: ArrayList()

                    foundPlace = MBPlace(prediction.placeId,phone,web,place.rating,placeImages)
                    status.value = FetchStatus.Ready
                }
                .addOnFailureListener{
                    status.value = FetchStatus.Error
                }

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
*/
package com.pigdogbay.foodhygieneratings.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
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
//class GooglePlaceImage(private val geoDataClient: GeoDataClient, private val metaData: PlacePhotoMetadata) : IPlaceImage {
//    override var index: Int = 0
//    private val status = ObservableProperty(this, FetchStatus.Uninitialized)
//    private var srcBitmap : Bitmap? = null
//
//    override val attribution: String
//        get() = metaData.attributions.toString()
//
//    override val observableStatus: ObservableProperty<FetchStatus>
//        get() = status
//
//    override val bitmap: Bitmap?
//        get() = srcBitmap
//
//    override fun fetchBitmap() {
//        status.value = FetchStatus.Fetching
//        val scaledPhotoResult = geoDataClient.getPhoto(metaData)
//        scaledPhotoResult.addOnFailureListener{ _ -> status.value = FetchStatus.Error }
//        scaledPhotoResult.addOnSuccessListener{ result ->
//            srcBitmap = result.bitmap
//            status.value = FetchStatus.Ready
//        }
//    }
//}
//
class GooglePlaceFetcher(private val placesClient: PlacesClient) : IPlaceFetcher {
    private val status = ObservableProperty(this, FetchStatus.Uninitialized)
    private var foundPlace : MBPlace? = null

    override val observableStatus: ObservableProperty<FetchStatus>
        get() = status
    override val mbPlace: MBPlace?
        get() = foundPlace

     /**
     * To fetch a place requires 2 tasks:
     * Fetch matching places (only first one is used)
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
                    foundPlace = MBPlace(prediction.placeId,phone,web,place.rating,ArrayList())
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

//class GooglePlaceFetcherOld(private val geoDataClient: GeoDataClient) : IPlaceFetcher {
//    private val status = ObservableProperty(this, FetchStatus.Uninitialized)
//    private var foundPlace : MBPlace? = null
//
//    override val observableStatus: ObservableProperty<FetchStatus>
//        get() = status
//
//    override val mbPlace: MBPlace?
//        get() = foundPlace
//
//    /**
//     * To fetch a place requires 3 tasks:
//     * Fetch matching places (only first one is used)
//     * Fetch place details
//     * Fetch metadata for the place images
//     *
//     * This information is then collated into MBPlace
//     */
//    override fun fetch(establishment: Establishment) {
//        status.value = FetchStatus.Fetching
//        val bounds = createBounds(establishment)
//        //First Task is to find a list of places that match the name and location
//        val firstTask = geoDataClient.getAutocompletePredictions(establishment.business.name, bounds, GeoDataClient.BoundsMode.STRICT,null)
//        firstTask.addOnSuccessListener { response ->
//            val predictions = DataBufferUtils.freezeAndClose(response)
//            if (predictions.size > 0) {
//                fetchPlace(predictions[0])
//            } else {
//                //nothing found, indicate error so as not to show any result
//                status.value = FetchStatus.Error
//            }
//        }
//        firstTask.addOnFailureListener { _ ->
//            status.value = FetchStatus.Error
//        }
//    }
//
//    private fun fetchPlace(autocompletePrediction: AutocompletePrediction) {
//        val placeTask = geoDataClient.getPlaceById(autocompletePrediction.placeId)
//        placeTask.addOnSuccessListener { placeResponse ->
//            val places = DataBufferUtils.freezeAndClose(placeResponse)
//            if (places.size > 0) {
//                fetchImageMetadata(places[0])
//            } else {
//                //no details found, indicate error so as not to show any result
//                status.value = FetchStatus.Error
//            }
//        }
//        placeTask.addOnFailureListener{ _ ->
//            status.value = FetchStatus.Error
//        }
//    }
//
//    private fun fetchImageMetadata(place: Place) {
//        val metadataTask = geoDataClient.getPlacePhotos(place.id)
//        metadataTask.addOnSuccessListener { metadataResponse ->
//            val placePhotoMetadataList = DataBufferUtils.freezeAndClose(metadataResponse.photoMetadata)
//            val placeImages : List<IPlaceImage> = placePhotoMetadataList.map{ it -> GooglePlaceImage(geoDataClient,it)}
//            foundPlace = createPlace(place,placeImages)
//            //All done now
//            status.value = FetchStatus.Ready
//        }
//
//        metadataTask.addOnFailureListener{_ ->
//            //even though the metadata failed, there are still the place details to be shown
//            foundPlace = createPlace(place,ArrayList())
//            status.value = FetchStatus.Ready
//        }
//    }
//
//    private fun createPlace(place: Place, placeImages : List<IPlaceImage>) : MBPlace {
//        //Convert phone number from the international format to UK
//        val phone = if (place.phoneNumber==null) "" else place.phoneNumber.toString().replace("+44 ","0")
//        val web = if (place.websiteUri==null) "" else place.websiteUri.toString()
//        return MBPlace(place.id,phone,web,place.rating,placeImages)
//
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

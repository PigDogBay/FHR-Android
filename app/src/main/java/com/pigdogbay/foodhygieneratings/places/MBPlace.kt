package com.pigdogbay.foodhygieneratings.places

import android.graphics.Bitmap
import com.pigdogbay.lib.utils.ObservableProperty

data class MBPlace(val id : String, val name : String, val telephone : String, val web : String, val rating : Float, val images : List<IPlaceImage>)

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
    fun createPlace(callback: (mbPlace : MBPlace?) -> Unit)
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
    override fun createPlace(callback: (mbPlace: MBPlace?) -> Unit) {
        Thread {
            val images: ArrayList<IPlaceImage> = ArrayList()
            images.add(DummyPlaceImage("Dummy Photographer", bitmap))
            val place = MBPlace("ChIJYUPe12BoekgR6iLyIiC9DJk", "Allo Pizza", "+44 1782 865646", "http://www.omg-restaurant.co.uk", 3.5F, images)
            Thread.sleep(500L)
            callback(place)
        }.start()
    }
}
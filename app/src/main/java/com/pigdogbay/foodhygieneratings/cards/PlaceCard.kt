package com.pigdogbay.foodhygieneratings.cards

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.pigdogbay.foodhygieneratings.R
import com.pigdogbay.foodhygieneratings.model.Establishment
import com.pigdogbay.foodhygieneratings.model.FetchStatus
import com.pigdogbay.foodhygieneratings.model.IPlaceFetcher
import com.pigdogbay.foodhygieneratings.model.MBPlace

class PlaceCard(private val placeFetcher: IPlaceFetcher, val establishment: Establishment) : ICard {

    override fun getViewType(): Int {
        return 6
    }

    override fun createViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.card_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder?) {
        val vh = viewHolder as PlaceViewHolder
        when (placeFetcher.observableStatus.value) {
            FetchStatus.Uninitialized -> {
                placeFetcher.fetch(establishment)
                vh.fetchingPlace()
            }
            FetchStatus.Fetching -> vh.fetchingPlace()
            FetchStatus.Ready -> {
                val place = placeFetcher.mbPlace!!
                if (place.images.isNotEmpty()) {
                    bindPlaceImage(vh,place)
                } else {
                    vh.readyNoImage(place)
                }
            }
            FetchStatus.Error -> vh.fetchingPlaceError()
        }
    }
    private fun bindPlaceImage(vh : PlaceViewHolder, place: MBPlace){
        val firstImg = place.images[0]
        when (firstImg.observableStatus.value) {
            FetchStatus.Uninitialized -> {
                firstImg.fetchBitmap()
                vh.fetchingImage(place)
            }
            FetchStatus.Fetching -> vh.fetchingImage(place)
            FetchStatus.Ready -> vh.readyState(place)
            FetchStatus.Error -> vh.fetchingImageError(place)
        }
    }
}

class PlaceViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val textAttribution : TextView = view.findViewById(R.id.textAttribution)
    val textPhone : TextView = view.findViewById(R.id.textPhone)
    val textWeb : TextView = view.findViewById(R.id.textWeb)
    val imagePlace : ImageView = view.findViewById(R.id.imagePlace)
    val ratingBar : RatingBar = view.findViewById(R.id.ratingBar)

    fun fetchingPlace(){
        Log.v("mpdb","fetching place")
        textPhone.text = ""
        textAttribution.text = "Fetching Place Details..."
        textWeb.text = ""
        ratingBar.rating = 0.0f
        imagePlace.setImageResource(R.drawable.ic_fetching_photo)
    }
    fun fetchingImage(place : MBPlace){
        Log.v("mpdb","fetching image")
        textAttribution.text = "Fetching Photo..."
        imagePlace.setImageResource(R.drawable.ic_fetching_photo)
        setPlaceDetails(place)
    }
    fun readyNoImage(place : MBPlace){
        Log.v("mpdb","ready no image")
        textAttribution.visibility = View.GONE
        imagePlace.visibility = View.GONE
        setPlaceDetails(place)
    }
    fun readyState(place : MBPlace){
        Log.v("mpdb","ready state")
        val firstImg = place.images[0]
        imagePlace.setImageBitmap(firstImg.bitmap)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textAttribution.text = Html.fromHtml(firstImg.attribution, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textAttribution.text = Html.fromHtml(firstImg.attribution)
        }
        setPlaceDetails(place)
    }
    fun fetchingImageError(place: MBPlace){
        Log.v("mpdb","fetching image error")
        textAttribution.text = "Oh fiddlesticks!"
        imagePlace.setImageResource(R.drawable.ic_broken_image)
        setPlaceDetails(place)
    }
    fun fetchingPlaceError(){
        Log.v("mpdb","fetching place error")
        textAttribution.text = "Outside Context Problem"
        imagePlace.visibility = View.GONE
        textPhone.text = ""
        textWeb.text = ""
        ratingBar.rating = 0.0f
    }

    private fun setPlaceDetails(place: MBPlace){
        textPhone.text = place.telephone
        textWeb.text = place.web
        ratingBar.rating = place.rating
    }
}
package com.pigdogbay.foodhygieneratings.cards

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.pigdogbay.foodhygieneratings.R
import com.pigdogbay.foodhygieneratings.model.FetchStatus
import com.pigdogbay.foodhygieneratings.model.MBPlace

class PlaceCard(val place: MBPlace) : ICard {

    override fun getViewType(): Int {
        return 6
    }

    override fun createViewHolder(parent: ViewGroup?): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.card_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun bindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?) {
        val vh = viewHolder as PlaceViewHolder
        if (place.images.isNotEmpty()) {
            bindPlaceImage(vh,place)
        } else {
            vh.readyNoImage(place)
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

class PlaceViewHolder(view : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
    val textAttribution : TextView = view.findViewById(R.id.textAttribution)
    val textPhone : TextView = view.findViewById(R.id.textPhone)
    val textWeb : TextView = view.findViewById(R.id.textWeb)
    val imagePlace : ImageView = view.findViewById(R.id.imagePlace)

    init {
        textAttribution.movementMethod = LinkMovementMethod.getInstance()
    }

    fun fetchingImage(place : MBPlace){
        textAttribution.text =""
        imagePlace.setImageResource(R.drawable.ic_fetching_photo)
        setPlaceDetails(place)
    }
    fun readyNoImage(place : MBPlace){
        textAttribution.visibility = View.GONE
        imagePlace.visibility = View.GONE
        setPlaceDetails(place)
    }
    fun readyState(place : MBPlace){
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
        textAttribution.visibility = View.GONE
        imagePlace.setImageResource(R.drawable.ic_broken_image)
        setPlaceDetails(place)
    }

    private fun setPlaceDetails(place: MBPlace){
        textPhone.text = if (place.telephone.isEmpty()) "Not specified" else place.telephone
        textWeb.text = if (place.web.isEmpty()) "Not specified" else place.web
    }
}
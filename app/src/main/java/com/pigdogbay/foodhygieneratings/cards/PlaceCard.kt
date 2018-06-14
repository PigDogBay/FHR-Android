package com.pigdogbay.foodhygieneratings.cards

import android.support.v7.widget.RecyclerView
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

    override fun createViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.card_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder?) {
        val vh = viewHolder as PlaceViewHolder
        vh.textPhone.text = place.telephone
        vh.textWeb.text = place.web
        vh.ratingBar.rating = place.rating
        if (place.images.size>0){
            val firstImg = place.images[0]
            when (firstImg.observableStatus.value){

                FetchStatus.Uninitialized -> {
                    firstImg.fetchBitmap()
                    vh.textAttribution.text = "Loading..."
                    vh.imagePlace.setImageResource(R.drawable.ic_fetching_photo)
                }
                FetchStatus.Ready -> {
                    vh.imagePlace.setImageBitmap(firstImg.bitmap)
                    vh.textAttribution.text = firstImg.attribution
                }
                FetchStatus.Error -> {
                    vh.textAttribution.text = "Oh fiddlesticks!"
                    vh.imagePlace.setImageResource(R.drawable.ic_broken_image)
                }
                FetchStatus.Fetching -> {}
            }
        } else {
            vh.textAttribution.visibility = View.GONE
            vh.imagePlace.visibility = View.GONE
        }
    }
}

class PlaceViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val textAttribution : TextView = view.findViewById(R.id.textAttribution)
    val textPhone : TextView = view.findViewById(R.id.textPhone)
    val textWeb : TextView = view.findViewById(R.id.textWeb)
    val imagePlace : ImageView = view.findViewById(R.id.imagePlace)
    val ratingBar : RatingBar = view.findViewById(R.id.ratingBar)
}
package com.pigdogbay.foodhygieneratings.cards

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pigdogbay.foodhygieneratings.R

class PlaceCard : ICard {
    override fun getViewType(): Int {
        return 6
    }

    override fun createViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.card_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder?) {
        val vh = viewHolder as PlaceViewHolder
        vh.textAttribution.text = "MP David Bailey"
        vh.textPhone.text = "01782 644571"
        vh.textWeb.text = "pigdogbay.com"
        vh.imagePlace.setImageResource(R.drawable.fhis_exempt)
    }
}

class PlaceViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val textAttribution : TextView = view.findViewById(R.id.textAttribution)
    val textPhone : TextView = view.findViewById(R.id.textPhone)
    val textWeb : TextView = view.findViewById(R.id.textWeb)
    val imagePlace : ImageView = view.findViewById(R.id.imagePlace)

}
package com.pigdogbay.foodhygieneratings.cards

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pigdogbay.foodhygieneratings.R
import com.pigdogbay.foodhygieneratings.model.FetchStatus
import com.pigdogbay.foodhygieneratings.model.IPlaceImage

class PhotoCard (val placeImage: IPlaceImage) : ICard{
    override fun getViewType(): Int {
        return 7
    }

    override fun createViewHolder(parent: ViewGroup?): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.card_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun bindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?) {
        val vh = viewHolder as PhotoViewHolder
        when (placeImage.observableStatus.value){
            FetchStatus.Uninitialized -> {
                placeImage.fetchBitmap()
                vh.fetchingPhoto()
            }
            FetchStatus.Fetching -> vh.fetchingPhoto()
            FetchStatus.Ready -> vh.readyState(placeImage)
            FetchStatus.Error -> vh.fetchingImageError()
        }
    }
}

class PhotoViewHolder(view : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val textAttribution: TextView = view.findViewById(R.id.textAttribution)
    val imageView: ImageView = view.findViewById(R.id.imageView)

    init {
        //allow the link to be clickable and open map/web
        //autolink does not work <a href>
        textAttribution.movementMethod = LinkMovementMethod.getInstance()
    }

    fun fetchingPhoto(){
        textAttribution.text = ""
        imageView.setImageResource(R.drawable.ic_fetching_photo)
    }
    fun readyState(placeImage: IPlaceImage){
        imageView.setImageBitmap(placeImage.bitmap!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textAttribution.text = Html.fromHtml(placeImage.attribution, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textAttribution.text = Html.fromHtml(placeImage.attribution)
        }
    }
    fun fetchingImageError(){
        textAttribution.text = ""
        imageView.setImageResource(R.drawable.ic_broken_image)
    }
}
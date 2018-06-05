package com.pigdogbay.foodhygieneratings

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pigdogbay.foodhygieneratings.model.Establishment
import com.pigdogbay.foodhygieneratings.model.SearchType
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener
import java.util.*

class EstablishmentAdapter(private val listener: OnListItemClickedListener<Establishment>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var establishments : List<Any> = ArrayList()
    var searchType = SearchType.quick

    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_ESTABLISHMENT = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ESTABLISHMENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_result, parent, false)
                EstablishmentViewHolder(view)
            } else -> throw IllegalArgumentException("Unknown view type")

        }
    }

    override fun getItemCount(): Int {
        return establishments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            VIEW_TYPE_HEADER -> {
                val viewHolder = holder as HeaderViewHolder
                viewHolder.bindItem(establishments[position] as String)
            }
            VIEW_TYPE_ESTABLISHMENT -> {
                val viewHolder = holder as EstablishmentViewHolder
                viewHolder.bindItem(establishments[position] as Establishment, searchType)
                if (listener!=null){
                    viewHolder.view.setOnClickListener({_ -> listener.onListItemClicked(viewHolder.establishment,viewHolder.adapterPosition)})
                }
            }
            else -> {
                throw IllegalArgumentException("Can not bind to unknown view holder type")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = establishments[position]
        return when (item){
            is String -> VIEW_TYPE_HEADER
            is Establishment -> VIEW_TYPE_ESTABLISHMENT
            else -> throw IllegalArgumentException("Object type is not allowed")
        }
    }
}

class EstablishmentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    var establishment: Establishment? = null
    private val imageView: ImageView = view.findViewById(R.id.list_result_image)
    private val text: TextView = view.findViewById(R.id.list_result_text)
    private val subtitle: TextView = view.findViewById(R.id.list_result_subtitle)

    fun bindItem(establishment: Establishment, searchType : SearchType) {
        this.establishment = establishment
        this.text.text = establishment.business.name
        this.subtitle.text = when (searchType){
            SearchType.local -> String.format(Locale.UK, "%.1f miles, %s", establishment.distance, establishment.address.flatten())
            else -> establishment.address.flatten()
        }
        val drawable = ContextCompat.getDrawable(view.context, establishment.rating.iconId)
        this.imageView.setImageDrawable(drawable)
    }
}
class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val text: TextView = view.findViewById(R.id.list_header)

    fun bindItem(header : String){
        this.text.text = header
    }
}
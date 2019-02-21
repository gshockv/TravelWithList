package com.gshockv.travelwishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_places.view.*

class TravelListAdapter(private var context: Context) : RecyclerView.Adapter<TravelListAdapter.ViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_places, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = PlaceData.placeList()[position]
        holder.bind(place)
    }

    override fun getItemCount() = PlaceData.placeList().size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.placeHolder.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClick(v, adapterPosition)
        }

        fun bind (place: Place) = with(itemView) {
            placeName.text = place.name
            Picasso.with(context)
                .load(place.imageResourceId(context))
                .into(placeImage)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}

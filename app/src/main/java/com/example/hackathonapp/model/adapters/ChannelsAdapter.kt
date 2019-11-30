package com.example.hackathonapp.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hackathonapp.R
import com.example.hackathonapp.model.channels.Channel
import kotlinx.android.synthetic.main.channel_item.view.*

/**
 * Created by Pavel.B on 30.11.2019.
 */



typealias OnItemClickListener = (Int) -> Unit

class ChannelAdapter: RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    var data: List<Channel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.channel_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = data[position]
        holder.title.text = channel.title


        if (channel.thumbnail.isNotEmpty()) {
            Glide.with(holder.thumbnail.context)
                .load(channel.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail)
        }

        if (!channel.isFree){
            holder.status.setImageResource(R.drawable.ic_lock)
        }

    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val title: TextView = view.titleView
        val thumbnail: ImageView = view.imageView
        val status: ImageView = view.statusView

        init{
            view.setOnClickListener {
                itemClickListener?.invoke(adapterPosition)
            }
        }

    }
}
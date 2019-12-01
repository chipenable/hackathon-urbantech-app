package com.example.hackathonapp.model.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
        holder.bind(channel)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val frame: ViewGroup = view.frameView
        private val title: TextView = view.titleView
        private val thumbnail: ImageView = view.imageView
        private val status: ImageView = view.statusView

        fun bind(channel: Channel){

            title.text = channel.title

            channel.thumbnail?.let{
                Glide.with(thumbnail.context)
                    .load(it)
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnail)
            }

            if (!channel.isFree){
                status.visibility = View.VISIBLE
                status.setImageResource(R.drawable.ic_lock)
            }
            else{
                status.visibility = View.INVISIBLE
                status.setImageDrawable(null)
            }

            frame.setOnClickListener {
                itemClickListener?.invoke(adapterPosition)
            }

            frame.setOnTouchListener { _, event ->
                when(event.action){
                    MotionEvent.ACTION_DOWN -> {
                        frame.animate().scaleX(1.01f).scaleY(1.01f).duration = 100
                        false
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        frame.animate().scaleX(1f).scaleY(1f).duration = 100
                        false
                    }

                    else -> false
                }
            }
        }

    }
}
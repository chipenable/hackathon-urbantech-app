package com.example.hackathonapp.ui.fragment

import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.hackathonapp.R
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.viewmodel.PlayerVMFactory
import com.example.hackathonapp.viewmodel.PlayerViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.player_fragment.*

class PlayerFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerFragment()
    }

    private lateinit var viewModel: PlayerViewModel
    private var player: ExoPlayer? = null
    private var mediaSource: MediaSource? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val channelId = arguments?.getInt("channel") ?: 0
        val factory = PlayerVMFactory(mainComponent, channelId)
        viewModel = ViewModelProviders.of(this, factory).get(PlayerViewModel::class.java)
        //viewModel.loadPlaylist()

        player = ExoPlayerFactory.newSimpleInstance(activity)
        playerView.player = player

        viewModel.alertMsg.observe(this, Observer {
            showAlert(it)
        })

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        //hideSystemUI()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }

        viewModel.playlist.observe(this, Observer {
            val uri = Uri.parse(it)
            mediaSource = buildMediaSource(uri)
            player?.prepare(mediaSource)
            player?.playWhenReady = true
        })
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player?.apply {
            playWhenReady = playWhenReady
            seekTo(currentWindow, playbackPosition)
            //prepare(mediaSource, false, false)
        }
    }


    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(activity, "exoplayer")
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun showAlert(title: String){
        alertView.visibility = if (title.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        alertTitle.text = title
    }

}


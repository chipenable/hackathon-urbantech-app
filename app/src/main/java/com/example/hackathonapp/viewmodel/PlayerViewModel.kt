package com.example.hackathonapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.channels.IChannelsInteractor
import com.example.hackathonapp.model.channels.PlaylistResult
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PlayerVMFactory(private val mainComponent: MainComponent, private val channelId: Int): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(mainComponent, channelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class PlayerViewModel(mainComponent: MainComponent, channelId: Int) : ViewModel() {

    @Inject
    lateinit var channelsInteractor: IChannelsInteractor

    val playlist = MutableLiveData<String>()
    val alertMsg = MutableLiveData<String>()

    private var playlistDisp: Disposable? = null

    companion object{
        val TAG = PlayerViewModel::class.java.name
    }

    init {
        mainComponent.inject(this)

        Log.d(TAG, "load playlist")
        playlistDisp = channelsInteractor.getPlaylist(channelId)
            .subscribe(
                this::handleResult,
                { error -> Log.d(TAG, "error: $error")},
                {}
            )
    }

    /*fun loadPlaylist(){
        Log.d(TAG, "load playlist")
        playlistDisp = channelsInteractor.getPlaylist()
            .subscribe(
                this::handleResult,
                { error -> Log.d(TAG, "error: $error")},
                {}
            )
    }*/

    override fun onCleared() {
        super.onCleared()
        playlistDisp?.dispose()
    }

    private fun handleResult(result: PlaylistResult){
        when(result){
            is PlaylistResult.Success -> {
                playlist.value = result.playlist
                alertMsg.value = ""
            }

            is PlaylistResult.Processing -> {
                alertMsg.value = result.message
            }
        }
    }
}

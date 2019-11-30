package com.example.hackathonapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.channels.IChannelsInteractor
import javax.inject.Inject

class PlayerVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class PlayerViewModel(mainComponent: MainComponent) : ViewModel() {

    @Inject
    lateinit var channelsInteractor: IChannelsInteractor

    val playlist = MutableLiveData<String>()

    companion object{
        val TAG = PlayerViewModel::class.java.name
    }

    init {
        mainComponent.inject(this)
    }

    fun loadPlaylist(){
        val playlistDisp = channelsInteractor.getPlaylist()
            .subscribe(
                { it ->
                    Log.d(TAG, "playlist: $it")
                    playlist.value = it
                },
                { error -> },
                {}
            )
    }

}

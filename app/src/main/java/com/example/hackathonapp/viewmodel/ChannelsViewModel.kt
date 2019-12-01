package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.channels.Channel
import com.example.hackathonapp.model.account.IAccountInteractor
import com.example.hackathonapp.model.channels.ChannelEvent
import com.example.hackathonapp.model.channels.IChannelsInteractor
import com.example.hackathonapp.model.util.SingleLiveEvent
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class ChannelsVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelsViewModel::class.java)) {
            return ChannelsViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class ChannelsViewModel(mainComponent: MainComponent) : ViewModel() {

    val channels = MutableLiveData<List<Channel>>()
    val channelEvent = SingleLiveEvent<ChannelEvent>()
    val isAuthorised = SingleLiveEvent<Boolean>()
    val TAG = ChannelsViewModel::class.java.name

    @Inject
    lateinit var accountInteractor: IAccountInteractor

    @Inject
    lateinit var channelsInteractor: IChannelsInteractor

    private var sessionDisp: Disposable? = null
    private var logoutDisp: Disposable? = null

    init{
        mainComponent.inject(this)
    }

    fun loadChannels(){
        sessionDisp = accountInteractor.checkSession()
            .subscribe(this::handleResult, this::handleError)
    }

    fun showChannel(position: Int){
        val channel = channels.value?.get(position)
        channel?.let {
            channelEvent.value = if (it.isFree) {
                ChannelEvent.ShowChannel()
            }
            else{
                ChannelEvent.SuggestLogin()
            }
        }
    }

    fun logout(){
        logoutDisp = accountInteractor.logout()
            .subscribe(this::handleResult, this::handleError)
    }

    override fun onCleared() {
        super.onCleared()
        sessionDisp?.dispose()
        logoutDisp?.dispose()
    }

    private fun updateChannels(){

    }

    private fun handleResult(result: Boolean){
        this.isAuthorised.value = result
        channelsInteractor.getChannels(result)
            .subscribe( {channels.value = it }, {} )
    }

    private fun handleError(error: Throwable){

    }
}

package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.channels.Channel
import com.example.hackathonapp.model.account.IAccountInteractor
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
    val showChannel = SingleLiveEvent<Channel>()
    val isAuthorised = SingleLiveEvent<Boolean>()
    val TAG = ChannelsViewModel::class.java.name

    @Inject
    lateinit var accountInteractor: IAccountInteractor

    private var sessionDisp: Disposable? = null
    private var logoutDisp: Disposable? = null

    init{
        mainComponent.inject(this)

        channels.value = listOf(
            Channel("camera one", "", true),
            Channel("camera two", "", true),
            Channel("camera three", "", false),
            Channel("camera four", "", false)
        )

    }

    fun checkSession(){
        sessionDisp = accountInteractor.checkSession()
            .subscribe(this::handleResult, this::handleError)
    }

    fun showChannel(position: Int){
        val channel = channels.value?.get(position)
        channel?.let {
            if (it.isFree) {
                showChannel.value = it
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

    private fun handleResult(result: Boolean){
        this.isAuthorised.value = result
    }

    private fun handleError(error: Throwable){

    }
}

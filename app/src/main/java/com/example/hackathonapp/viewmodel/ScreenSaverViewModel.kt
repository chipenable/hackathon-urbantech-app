package com.example.hackathonapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.account.IAccountInteractor
import com.example.hackathonapp.model.session.SessionStore
import com.example.hackathonapp.model.util.SingleLiveEvent
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ScreenSaverVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScreenSaverViewModel::class.java)) {
            return ScreenSaverViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class ScreenSaverViewModel(mainComponent: MainComponent) : ViewModel() {

    val isAuthorized = SingleLiveEvent<Boolean>()

    @Inject
    lateinit var sessionStore: SessionStore

    @Inject
    lateinit var accountInteracor: IAccountInteractor

    private var sessionDisp: Disposable? = null

    init {
        mainComponent.inject(this)
    }

    fun checkSession(){
        sessionDisp = accountInteracor.checkSession()
            .subscribe(this::handleResult, this::handleError)

    }

    override fun onCleared() {
        super.onCleared()
        sessionDisp?.dispose()
    }

    private fun handleResult(result: Boolean){
        isAuthorized.value = result
        //isAuthorized.value = true
    }

    private fun handleError(error: Throwable){

    }
}

package com.example.hackathonapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.IAccountInteractor
import com.example.hackathonapp.model.SessionStore
import com.example.hackathonapp.model.util.SingleLiveEvent
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

    init {
        mainComponent.inject(this)
    }

    fun checkSession(){
        isAuthorized.value = sessionStore.isAuthorised

        accountInteracor.checkSession()

    }

}

package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.account.AuthResult
import com.example.hackathonapp.model.account.IAccountInteractor
import com.example.hackathonapp.model.util.SingleLiveEvent
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class LoginViewModel(mainComponent: MainComponent) : ViewModel() {

    val processing = MutableLiveData<Boolean>()
    val signInResult = SingleLiveEvent<AuthResult>()

    @Inject
    lateinit var accountInteractor: IAccountInteractor

    private var signInDisp: Disposable? = null

    init {
        mainComponent.inject(this)
    }

    fun signIn(login: String, password: String){
        signInDisp = accountInteractor.signIn(login, password)
            .subscribe( this::handleResult, this::handleError)
    }

    override fun onCleared() {
        super.onCleared()
        signInDisp?.dispose()
    }

    private fun handleResult(result: AuthResult){
        processing.value = result is AuthResult.Processing
        signInResult.value = result
    }

    private fun handleError(error: Throwable){

    }
}

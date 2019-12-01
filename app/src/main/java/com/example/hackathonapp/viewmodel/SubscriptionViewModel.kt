package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.account.UserSubscription
import com.example.hackathonapp.model.store.PaymentResult
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SubscriptionVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            return SubscriptionViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}


class SubscriptionViewModel(mainComponent: MainComponent) : ViewModel() {

    @Inject
    lateinit var userSubscription: UserSubscription

    @Inject
    lateinit var scheduler: RxSchedulers

    val payment = MutableLiveData<PaymentResult>()
    private var paymentDisp: Disposable? = null

    init {
        mainComponent.inject(this)

    }

    fun agree(){
        paymentDisp = buySubscription()
            .subscribe(
                {
                    userSubscription.hasSubscription = it is PaymentResult.Success
                    payment.value = it
                },
                {}
            )

    }

    override fun onCleared() {
        super.onCleared()
        paymentDisp?.dispose()
    }

    private fun buySubscription(): Observable<PaymentResult> {

        val resultObs: Observable<PaymentResult> = Observable.timer(3, TimeUnit.SECONDS)
            .map { PaymentResult.Success() }

        return resultObs.startWith( PaymentResult.Processing() )
            .compose(scheduler.fromIoToUiSchedulersObs())
    }

}

package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.products.IStoreInteractor
import com.example.hackathonapp.model.products.PaymentResult
import com.example.hackathonapp.model.products.Product
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PaymentVMFactory(private val mainComponent: MainComponent,
                       private val productId: String): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(mainComponent, productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class PaymentViewModel(mainComponent: MainComponent, val productId: String) : ViewModel() {

    @Inject
    lateinit var storeInteractor: IStoreInteractor

    val product = MutableLiveData<Product>()
    val payment = MutableLiveData<PaymentResult>()

    private var productDisp: Disposable? = null
    private var paymentDisp: Disposable? = null

    init {
        mainComponent.inject(this)

        productDisp = storeInteractor.getProduct(productId)
            .subscribe(
                { product.value = it },
                {}
            )

    }

    fun agree(){
        paymentDisp = storeInteractor.makePayment(productId)
            .subscribe( {
                payment.value = it
            }, {} )
    }

    override fun onCleared() {
        super.onCleared()
        productDisp?.dispose()
        paymentDisp?.dispose()
    }
}

package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.R
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.products.IStoreInteractor
import com.example.hackathonapp.model.products.Product
import com.example.hackathonapp.model.util.SingleLiveEvent
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class StoreVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            return StoreViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class StoreViewModel(mainComponent: MainComponent) : ViewModel() {

    @Inject
    lateinit var storeInteractor: IStoreInteractor

    val products = MutableLiveData<List<Product>>()
    val buyProduct = SingleLiveEvent<Product>()

    private var productsDisp: Disposable? = null

    init {
        mainComponent.inject(this)

        productsDisp = storeInteractor.getProducts()
            .subscribe(
                {
                    products.value = it
                },
                {}
            )

    }

    override fun onCleared() {
        super.onCleared()
        productsDisp?.dispose()
    }

    fun buyProduct(position: Int) {
        products.value?.let {
            buyProduct.value = it[position]
        }
    }

}

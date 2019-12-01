package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.R
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.products.Product
import com.example.hackathonapp.model.util.SingleLiveEvent

class StoreVMFactory(private val mainComponent: MainComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            return StoreViewModel(mainComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class StoreViewModel(mainComponent: MainComponent) : ViewModel() {

    val products = MutableLiveData<List<Product>>()
    val buyEvent = SingleLiveEvent<Boolean>()

    init {
        mainComponent.inject(this)

        products.value = listOf(
            Product("Попкорн", R.drawable.product_1),
            Product("Минеральная вода", R.drawable.product_2),
            Product("Powerbank", R.drawable.product_3),
            Product("Хот-дог", R.drawable.product_4),
            Product("Coca-cola", R.drawable.product_5),
            Product("Зонтик", R.drawable.product_6)
        )

    }

    fun buyProduct(position: Int){
        buyEvent.value = true
    }

}

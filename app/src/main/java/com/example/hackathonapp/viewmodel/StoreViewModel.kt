package com.example.hackathonapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hackathonapp.R
import com.example.hackathonapp.di.MainComponent
import com.example.hackathonapp.model.products.Product

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

    init {
        mainComponent.inject(this)

        products.value = listOf(
            Product(R.drawable.product_1),
            Product(R.drawable.product_2),
            Product(R.drawable.product_3),
            Product(R.drawable.product_4),
            Product(R.drawable.product_5),
            Product(R.drawable.product_6)
        )


    }

}

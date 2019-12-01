package com.example.hackathonapp.model.store

/**
 * Created by Pavel.B on 01.12.2019.
 */

class Product(val id: String, val title: String = "", val thumbnail: Int, val price: Int = 5)

sealed class PaymentResult{

    class Success: PaymentResult()
    class Processing: PaymentResult()
    class Error: PaymentResult()

}
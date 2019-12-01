package com.example.hackathonapp.model.products

import com.example.hackathonapp.R
import io.reactivex.Observable
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Pavel.B on 01.12.2019.
 */
interface IStoreInteractor {

    fun getProducts(): Observable<List<Product>>
    fun getProduct(id: String): Observable<Product>
    fun makePayment(id: String): Observable<PaymentResult>

}

class StoreInteractor(val scheduler: RxSchedulers): IStoreInteractor {

    //заглушка
    override fun getProducts(): Observable<List<Product>> {
        val products = listOf(
            Product("1","Попкорн", R.drawable.product_1, 150),
            Product("2", "Вода", R.drawable.product_2, 150),
            Product("3", "Powerbank", R.drawable.product_3, 500),
            Product("4", "Хот-дог", R.drawable.product_4, 200),
            Product("5", "Coca-cola", R.drawable.product_5, 100),
            Product("6", "Зонтик", R.drawable.product_6, 300)
        )

        return Observable.just(products)
    }

    //заглушка
    override fun getProduct(id: String): Observable<Product> {
        return getProducts().map{
            it.find { p -> p.id == id }!!
        }
    }

    //заглушка
    override fun makePayment(id: String): Observable<PaymentResult> {

        val resultObs: Observable<PaymentResult> = Observable.timer(3, TimeUnit.SECONDS)
            .map { PaymentResult.Success() }

        return resultObs.startWith( PaymentResult.Processing() )
            .compose(scheduler.fromIoToUiSchedulersObs())
    }

}
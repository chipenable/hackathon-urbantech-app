package ru.chipenable.hackathonvideoapp.model.util

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulers {

    val compScheduler: Scheduler by lazy {
        Schedulers.computation()
    }

    val ioScheduler: Scheduler by lazy {
        Schedulers.io()
    }

    val uiScheduler: Scheduler by lazy {
        AndroidSchedulers.mainThread()
    }

    val databaseScheduler: Scheduler by lazy {
        Schedulers.newThread()
    }


    fun fromIoToUiSchedulers(): CompletableTransformer {
        return CompletableTransformer {
                upstream -> upstream.subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
        }
    }

    fun <T> fromIoToUiSchedulers(): FlowableTransformer<T, T> {
        return FlowableTransformer {
                upstream -> upstream.subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
        }
    }

    fun <T> fromIoToUiSchedulersObs(): ObservableTransformer<T, T> {
        return ObservableTransformer {
                upstream -> upstream.subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
        }
    }

    fun <T> databaseSchedulersObs(): ObservableTransformer<T, T> {
        return ObservableTransformer {
                upstream -> upstream.subscribeOn(databaseScheduler)
            .observeOn(uiScheduler)
        }
    }

}

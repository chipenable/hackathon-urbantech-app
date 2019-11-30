package com.example.hackathonapp

import android.app.Application
import com.example.hackathonapp.di.DaggerMainComponent
import com.example.hackathonapp.di.MainComponent

/**
 * Created by Pavel.B on 30.11.2019.
 */
class App: Application() {

    lateinit var mainComponent: MainComponent

    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent.builder()
            .build()
    }
}
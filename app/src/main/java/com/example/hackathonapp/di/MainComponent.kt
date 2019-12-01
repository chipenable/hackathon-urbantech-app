package com.example.hackathonapp.di

import com.example.hackathonapp.viewmodel.*
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Pavel.B on 30.11.2019.
 */
@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {

    fun inject(obj: ScreenSaverViewModel)
    fun inject(obj: LoginViewModel)
    fun inject(obj: ChannelsViewModel)
    fun inject(obj: PlayerViewModel)
    fun inject(obj: StoreViewModel)
    fun inject(obj: PaymentViewModel)

}
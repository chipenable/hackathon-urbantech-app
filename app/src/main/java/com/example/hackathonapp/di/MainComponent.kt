package com.example.hackathonapp.di

import com.example.hackathonapp.viewmodel.ChannelsViewModel
import com.example.hackathonapp.viewmodel.LoginViewModel
import com.example.hackathonapp.viewmodel.PlayerViewModel
import com.example.hackathonapp.viewmodel.ScreenSaverViewModel
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

}
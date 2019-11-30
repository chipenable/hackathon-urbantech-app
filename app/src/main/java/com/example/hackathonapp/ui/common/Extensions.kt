package com.example.hackathonapp.ui.common

import androidx.fragment.app.Fragment
import com.example.hackathonapp.App
import com.example.hackathonapp.di.MainComponent

/**
 * Created by Pavel.B on 30.11.2019.
 */

val Fragment.mainComponent: MainComponent
get() = (context!!.applicationContext as App).mainComponent

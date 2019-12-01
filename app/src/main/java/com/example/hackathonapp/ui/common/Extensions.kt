package com.example.hackathonapp.ui.common

import android.util.DisplayMetrics.DENSITY_DEFAULT
import androidx.fragment.app.Fragment
import com.example.hackathonapp.App
import com.example.hackathonapp.di.MainComponent

/**
 * Created by Pavel.B on 30.11.2019.
 */

val Fragment.mainComponent: MainComponent
get() = (context!!.applicationContext as App).mainComponent

fun Fragment.setTitle(title: String){
    activity?.title = title
}

fun Fragment.setTitle(titleId: Int){
    activity?.title = getString(titleId)
}

fun Fragment.dpToPx(dp: Int)
        = (dp * resources.displayMetrics.densityDpi.toFloat()/ DENSITY_DEFAULT.toFloat()).toInt()
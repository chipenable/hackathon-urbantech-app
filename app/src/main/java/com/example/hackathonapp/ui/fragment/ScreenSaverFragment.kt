package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.hackathonapp.R
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.viewmodel.ScreenSaverVMFactory
import com.example.hackathonapp.viewmodel.ScreenSaverViewModel

class ScreenSaverFragment : Fragment() {

    companion object {
        fun newInstance() = ScreenSaverFragment()
    }

    private lateinit var viewModel: ScreenSaverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_saver_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ScreenSaverVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(ScreenSaverViewModel::class.java)
    }

}

package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.hackathonapp.R
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.viewmodel.ChannelsVMFactory
import com.example.hackathonapp.viewmodel.ChannelsViewModel

class ChannelsFragment : Fragment() {

    companion object {
        fun newInstance() = ChannelsFragment()
    }

    private lateinit var viewModel: ChannelsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.channels_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ChannelsVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(ChannelsViewModel::class.java)

    }

}

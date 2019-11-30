package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.hackathonapp.R
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.viewmodel.PlayerVMFactory
import com.example.hackathonapp.viewmodel.PlayerViewModel

class PlayerFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerFragment()
    }

    private lateinit var viewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = PlayerVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(PlayerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

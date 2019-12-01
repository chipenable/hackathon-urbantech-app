package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.hackathonapp.R
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.ui.common.setTitle
import com.example.hackathonapp.viewmodel.PaymentVMFactory
import com.example.hackathonapp.viewmodel.PaymentViewModel

class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(R.string.payment_screen_title)

        val factory = PaymentVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(PaymentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

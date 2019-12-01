package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.hackathonapp.R
import com.example.hackathonapp.model.store.PaymentResult
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.ui.common.setTitle
import com.example.hackathonapp.viewmodel.SubscriptionVMFactory
import com.example.hackathonapp.viewmodel.SubscriptionViewModel
import kotlinx.android.synthetic.main.payment_fragment.*
import org.jetbrains.anko.support.v4.toast

class SubscriptionFragment : Fragment() {

    companion object {
        fun newInstance() = SubscriptionFragment()
    }

    private lateinit var viewModel: SubscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subscription_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = SubscriptionVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(SubscriptionViewModel::class.java)

        setTitle(R.string.subscription)

        viewModel.payment.observe(this, Observer {
            progressBar.visibility = if (it is PaymentResult.Processing) View.VISIBLE else View.INVISIBLE
            if (it is PaymentResult.Success){
                findNavController().navigateUp()
                toast(R.string.you_bought_subscription)
            }
        })

        agreeBut.setOnClickListener {
            viewModel.agree()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.payment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.cancelItem -> {
                findNavController().navigateUp()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

}

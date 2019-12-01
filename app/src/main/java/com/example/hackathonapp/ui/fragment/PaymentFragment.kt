package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.hackathonapp.R
import com.example.hackathonapp.model.products.PaymentResult
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.ui.common.setTitle
import com.example.hackathonapp.viewmodel.PaymentVMFactory
import com.example.hackathonapp.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.payment_fragment.*
import org.jetbrains.anko.support.v4.toast

class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(R.string.payment_screen_title)

        var productId = arguments?.getString("product_id")
        if (productId == null){
            productId = ""
            findNavController().navigateUp()
            toast("Не удается обработать покупку")
        }

        val factory = PaymentVMFactory(mainComponent, productId)
        viewModel = ViewModelProviders.of(this, factory).get(PaymentViewModel::class.java)

        viewModel.product.observe(this, Observer {
            productIconView.setImageResource(it.thumbnail)
        })

        viewModel.payment.observe(this, Observer {
            progressBar.visibility = if (it is PaymentResult.Processing) View.VISIBLE else View.INVISIBLE
            if (it is PaymentResult.Success){
                findNavController().navigateUp()
                toast(R.string.payment_result_is_success)
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

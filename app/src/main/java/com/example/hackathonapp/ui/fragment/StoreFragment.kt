package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.example.hackathonapp.R
import com.example.hackathonapp.model.adapters.ChannelAdapter
import com.example.hackathonapp.model.adapters.ProductAdapter
import com.example.hackathonapp.ui.common.dpToPx
import com.example.hackathonapp.ui.common.enableUpButton
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.ui.common.setTitle
import com.example.hackathonapp.viewmodel.StoreVMFactory
import com.example.hackathonapp.viewmodel.StoreViewModel
import kotlinx.android.synthetic.main.channels_fragment.*
import ru.chipenable.hackathonvideoapp.model.util.GridSpacingItemDecoration

class StoreFragment : Fragment() {

    companion object {
        fun newInstance() = StoreFragment()
    }

    private lateinit var viewModel: StoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.store_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = StoreVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(StoreViewModel::class.java)

        setTitle(R.string.store_screen_title)

        val columns = resources.getInteger(R.integer.store_col)
        listView.layoutManager = GridLayoutManager(context, columns, GridLayoutManager.VERTICAL, false)
        val adapter = ProductAdapter()
        listView.adapter = adapter
        listView.addItemDecoration(GridSpacingItemDecoration(columns, dpToPx(8), dpToPx(8), true))

        adapter.itemClickListener = { position -> viewModel.buyProduct(position) }

        viewModel.products.observe(this, Observer{
            adapter.data = it
        })

        viewModel.buyProduct.observe(this, Observer {
            val bundle = bundleOf("product_id" to it.id)
            findNavController().navigate(R.id.action_storeFragment_to_paymentFragment, bundle)
        })

    }

}

package com.example.hackathonapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.GridLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.example.hackathonapp.R
import com.example.hackathonapp.model.adapters.ChannelAdapter
import com.example.hackathonapp.model.channels.ChannelEvent
import com.example.hackathonapp.ui.common.dpToPx
import com.example.hackathonapp.ui.common.enableUpButton
import com.example.hackathonapp.ui.common.mainComponent
import com.example.hackathonapp.ui.common.setTitle
import com.example.hackathonapp.viewmodel.ChannelsVMFactory
import com.example.hackathonapp.viewmodel.ChannelsViewModel
import kotlinx.android.synthetic.main.channels_fragment.*
import org.jetbrains.anko.support.v4.toast
import ru.chipenable.hackathonvideoapp.model.util.GridSpacingItemDecoration

class ChannelsFragment : Fragment() {

    companion object {
        fun newInstance() = ChannelsFragment()
        var TAG = ChannelsFragment::class.java.name
    }

    private lateinit var viewModel: ChannelsViewModel
    private var userMenuId = R.menu.login_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.channels_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setTitle(R.string.app_name)
        enableUpButton(false)

        val factory = ChannelsVMFactory(mainComponent)
        viewModel = ViewModelProviders.of(this, factory).get(ChannelsViewModel::class.java)

        val columns = resources.getInteger(R.integer.channel_col)
        listView.layoutManager = GridLayoutManager(context, columns, GridLayoutManager.VERTICAL, false)
        val adapter = ChannelAdapter()
        listView.adapter = adapter
        listView.addItemDecoration(GridSpacingItemDecoration(columns, dpToPx(8), dpToPx(8), true))

        adapter.itemClickListener = { position -> viewModel.showChannel(position)}

        viewModel.channels.observe(this, Observer {
            adapter.data = it
        })

        viewModel.isAuthorised.observe(this, Observer {
            updateMenu(it)
        })

        viewModel.channelEvent.observe(this, Observer {
            if (it is ChannelEvent.ShowChannel) {
                val bundle = bundleOf("channel" to it.position)
                findNavController().navigate(R.id.action_channelsFragment_to_playerFragment, bundle)
            }
            else if (it is ChannelEvent.SuggestLogin){
                val bundle = bundleOf("title" to getString(R.string.login_to_have_access))
                findNavController().navigate(R.id.action_channelsFragment_to_loginFragment, bundle)
            }
        })

        viewModel.loadChannels()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(userMenuId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.loginItem -> {
                findNavController().navigate(R.id.loginFragment)
                true
            }

            R.id.logoutItem -> {
                viewModel.logout()
                true
            }

            R.id.storeItem -> {
                findNavController().navigate(R.id.storeFragment)
                true
            }

            R.id.disabledStoreItem -> {
                toast(R.string.login_to_open_store)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun updateMenu(isAuthorised: Boolean){
        userMenuId = if (isAuthorised) R.menu.user_menu else R.menu.login_menu
        activity?.invalidateOptionsMenu()
    }

}

package com.sabgil.bbuckkugi.ui.discovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityDiscoveryBinding
import com.sabgil.bbuckkugi.databinding.ItemDiscoveryRemoteBinding
import com.sabgil.bbuckkugi.service.channel.ConnectionRequestChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel
import com.sabgil.bbuckkugi.ui.send.SendActivity
import com.sabgil.mutiviewtype.multiViewTypeAdapter
import com.sabgil.mutiviewtype.type
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscoveryActivity : BaseActivity<ActivityDiscoveryBinding>(R.layout.activity_discovery) {

    @Inject
    lateinit var discoveryChannel: DiscoveryChannel

    @Inject
    lateinit var connectionRequestChannel: ConnectionRequestChannel

    private val viewModel by viewModelOf<DiscoveryViewModel>()

    private val adapter by lazy {
        multiViewTypeAdapter {
            type<DiscoveryRemoteItem, ItemDiscoveryRemoteBinding> {
                onCreate { binding, _, itemSupplier ->
                    binding.nameTextView.setOnClickListener {
                        val item = itemSupplier() ?: return@setOnClickListener
                        connectionRequestChannel.sendActionForConnection(item.endpointId)
                    }
                }

                onBind { item, binding ->
                    binding.item = item
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupObserver()
        setupChannel()

        discoveryChannel.sendActionForStartDiscovery()
    }

    override fun onDestroy() {
        discoveryChannel.sendActionForStopDiscovery()
        super.onDestroy()
    }

    private fun setupViews() {
        binding.discoveredRemoteRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.discoveredRemotes.observeNonNull {
            adapter.update(it)
        }
    }

    private fun setupChannel() {
        discoveryChannel.registerClient(this) {
            when (it) {
                is Result.Success -> viewModel.discoveryRemote(it.result)
                is Result.Failure -> showErrorMessage(it.exception.message.orEmpty())
            }
        }

        connectionRequestChannel.registerClient(this) {
            when (it) {
                is Result.Success -> SendActivity.startOnHome(this)
                is Result.Failure -> finish() // TODO error popup
            }
        }
    }

    companion object {

        fun start(context: Context) = context.startActivity(
            Intent(context, DiscoveryActivity::class.java)
        )
    }
}
package com.sabgil.bbuckkugi.presentation.ui.discovery

import android.content.Context
import com.sabgil.bbuckkugi.common.doNotAnything
import com.sabgil.bbuckkugi.databinding.ItemDiscoveryEndpointBinding
import com.sabgil.bbuckkugi.databinding.ItemDiscoveryHeaderBinding
import com.sabgil.mutiviewtype.MultiViewTypeAdapter
import com.sabgil.mutiviewtype.type
import com.sabgil.mutiviewtype.viewTypeMapStore

class DiscoveryAdapter(
    context: Context,
    private val handler: DiscoveryActivity.Handler
) : MultiViewTypeAdapter() {

    override val viewTypeMapStore = context.viewTypeMapStore {
        type<DiscoveryItem.Header, ItemDiscoveryHeaderBinding> {
            doNotAnything()
        }

        type<DiscoveryItem.Endpoint, ItemDiscoveryEndpointBinding> {
            onBind { item, binding ->
                binding.handler = handler
                binding.item = item
            }
        }
    }
}
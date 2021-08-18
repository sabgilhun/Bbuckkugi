package com.sabgil.bbuckkugi.presentation.ui.ladder

import android.content.Context
import android.graphics.Typeface
import com.sabgil.bbuckkugi.databinding.ItemLadderResultBinding
import com.sabgil.mutiviewtype.MultiViewTypeAdapter
import com.sabgil.mutiviewtype.ViewTypeMapStore
import com.sabgil.mutiviewtype.type
import com.sabgil.mutiviewtype.viewTypeMapStore

class LadderAdapter(context: Context) : MultiViewTypeAdapter() {
    override val viewTypeMapStore: ViewTypeMapStore = context.viewTypeMapStore {
        type<PlayerResultItem, ItemLadderResultBinding> {
            onBind { item, binding ->
                binding.item = item
                val typeface = if (item.reword == "당첨!") Typeface.BOLD else Typeface.NORMAL
                binding.tvPlayer.setTypeface(null, typeface)
                binding.tvReword.setTypeface(null, typeface)
            }
        }
    }
}
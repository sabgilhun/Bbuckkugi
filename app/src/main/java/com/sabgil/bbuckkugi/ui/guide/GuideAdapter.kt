package com.sabgil.bbuckkugi.ui.guide

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.BindViewHolder
import com.sabgil.bbuckkugi.databinding.ItemGuideBinding

class GuideAdapter(context: Context) : RecyclerView.Adapter<BindViewHolder<ItemGuideBinding>>() {

    private val guides = mutableListOf(
        Triple(
            "❤️",
            "이 앱은...",
            context.getString(R.string.guide_detail_contents_1)
        ),
        Triple(
            "❗️",
            "메시지가 옵니다.",
            context.getString(R.string.guide_detail_contents_2)
        ),
        Triple(
            "\uD83E\uDD1D",
            "신중한 판단을 해주세요.",
            context.getString(R.string.guide_detail_contents_3)
        ),
        Triple(
            "❌",
            "로그인을 하는 이유는...",
            context.getString(R.string.guide_detail_contents_4)
        )
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = BindViewHolder(ItemGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: BindViewHolder<ItemGuideBinding>,
        position: Int
    ) {
        holder.binding.contents = guides[position]
    }

    override fun getItemCount() = guides.size
}
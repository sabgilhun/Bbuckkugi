package com.sabgil.bbuckkugi.presentation.ui.send

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sabgil.bbuckkugi.common.BindViewHolder
import com.sabgil.bbuckkugi.common.MessageCardList
import com.sabgil.bbuckkugi.databinding.ItemSelectMessageCardBinding

class MessageCardAdapter(
    private val context: Context
) : RecyclerView.Adapter<BindViewHolder<ItemSelectMessageCardBinding>>() {

    private val items = MessageCardList.sendMessageCards

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = BindViewHolder(
        ItemSelectMessageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(
        holder: BindViewHolder<ItemSelectMessageCardBinding>,
        position: Int
    ) {
        holder.binding.messageCardImageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                items[position]
            )
        )
    }

    override fun getItemCount() = items.size
}
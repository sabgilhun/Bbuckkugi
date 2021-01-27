package com.sabgil.bbuckkugi.ui.send

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.BindViewHolder
import com.sabgil.bbuckkugi.databinding.ItemSelectMessageCardBinding

class MessageCardAdapter(
    private val context: Context
) : RecyclerView.Adapter<BindViewHolder<ItemSelectMessageCardBinding>>() {

    private val items = listOf(
        R.drawable.message_card1,
        R.drawable.message_card2,
        R.drawable.message_card3,
        R.drawable.message_card4,
        R.drawable.message_card5,
        R.drawable.message_card6,
        R.drawable.message_card7,
        R.drawable.message_card8,
        R.drawable.message_card9,
        R.drawable.message_card10,
        R.drawable.message_card11,
        R.drawable.message_card12
    )

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
        val image = ContextCompat.getDrawable(context, items[position])
        holder.binding.messageCardImageView.setImageDrawable(image)
    }

    override fun getItemCount() = items.size
}
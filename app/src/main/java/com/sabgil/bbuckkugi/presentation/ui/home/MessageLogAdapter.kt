package com.sabgil.bbuckkugi.presentation.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.MessageCardList
import com.sabgil.bbuckkugi.common.ext.layoutInflater
import com.sabgil.bbuckkugi.common.yy_MM_dd_HH_mm
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.databinding.ItemMessageLogBinding
import com.sabgil.mutiviewtype.BindingViewHolder

class MessageLogAdapter : RecyclerView.Adapter<BindingViewHolder>() {
    private val _item = mutableListOf<Message.Reply>()

    fun replaceAll(item: List<Message.Reply>) {
        _item.clear()
        _item.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val binding =
            ItemMessageLogBinding.inflate(parent.context.layoutInflater, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val binding = holder.binding as ItemMessageLogBinding
        val item = _item[position]
        binding.item = item

        val resource = if (item.isAgreed) {
            MessageCardList.acceptMessageCards[item.messageType]
        } else {
            MessageCardList.rejectMessageCard[item.messageType]
        }

        binding.imageView.setImageResource(resource)

        binding.text2.apply {
            val color = if (item.isAgreed) {
                binding.root.context.resources.getColor(R.color.green2)
            } else {
                binding.root.context.resources.getColor(R.color.orange)
            }

            val text = if (item.isAgreed) {
                "수락!"
            } else {
                "거절…"
            }

            setTextColor(color)
            setText(text)
        }

        binding.text3.text = if (item.isAgreed) {
            "했습니다. \uD83D\uDE2D"
        } else {
            "했습니다. \uD83E\uDD73"
        }

        binding.date.text = item.time.yy_MM_dd_HH_mm()
    }

    override fun getItemCount() = _item.size
}
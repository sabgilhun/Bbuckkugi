package com.sabgil.bbuckkugi.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
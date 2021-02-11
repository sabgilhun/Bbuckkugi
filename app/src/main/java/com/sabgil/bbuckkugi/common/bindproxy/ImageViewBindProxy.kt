package com.sabgil.bbuckkugi.common.bindproxy

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sabgil.bbuckkugi.common.ext.setImage

@BindingAdapter("image")
fun ImageView.setImageProxy(uri: String?) = setImage(uri)
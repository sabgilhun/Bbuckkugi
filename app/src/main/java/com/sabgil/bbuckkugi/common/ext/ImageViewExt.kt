package com.sabgil.bbuckkugi.common.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sabgil.bbuckkugi.R

fun ImageView.setImage(uri: String?) {
    if (!uri.isNullOrEmpty()) {
        Glide.with(this)
            .load(uri)
            .into(this)
    } else {
        setImageResource(R.drawable.ic_account_circle_24)
    }
}
package com.sabgil.bbuckkugi.model

import com.sabgil.bbuckkugi.model.enums.Gender

data class UserInfo(
    val id: String,
    val gender: Gender?,
    val name: String?,
    val profileImageUrl: String?
)
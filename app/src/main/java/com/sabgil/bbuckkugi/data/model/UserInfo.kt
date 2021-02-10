package com.sabgil.bbuckkugi.data.model

import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay

data class UserInfo(
    val loginWay: LoginWay,
    val id: String,
    val gender: Gender?,
    val name: String?,
    val profileImageUrl: String?
)
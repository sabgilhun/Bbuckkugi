package com.sabgil.bbuckkugi.model

import com.sabgil.bbuckkugi.model.enums.Gender
import com.sabgil.bbuckkugi.model.enums.LoginWay

data class UserInfo(
    val loginWay: LoginWay,
    val id: String,
    val gender: Gender?,
    val name: String?,
    val profileImageUrl: String?
)
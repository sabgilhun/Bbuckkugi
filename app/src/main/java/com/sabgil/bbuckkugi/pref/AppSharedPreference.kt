package com.sabgil.bbuckkugi.pref

import com.sabgil.bbuckkugi.model.enums.LoginWay
import com.sabgil.bbuckkugi.model.enums.Gender

interface AppSharedPreference {

    var id: String?

    var nickname: String?

    var gender: Gender?

    var loginWay: LoginWay?
}
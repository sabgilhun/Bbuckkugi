package com.sabgil.bbuckkugi.pref

import com.sabgil.bbuckkugi.model.enums.Gender
import com.sabgil.bbuckkugi.model.enums.LoginWay

interface AppSharedPreference {

    var loginWay: LoginWay?

    var id: String?

    var gender: Gender?

    var name: String?

    var profileImageUrl: String?
}
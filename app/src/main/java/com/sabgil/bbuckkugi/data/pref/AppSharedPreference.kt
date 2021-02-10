package com.sabgil.bbuckkugi.data.pref

import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay

interface AppSharedPreference {

    var loginWay: LoginWay?

    var id: String?

    var gender: Gender?

    var name: String?

    var profileImageUrl: String?
}
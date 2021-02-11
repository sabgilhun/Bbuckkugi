package com.sabgil.bbuckkugi.presentation.ui.discovery

import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.mutiviewtype.BaseItem

sealed class DiscoveryItem(id: String) : BaseItem(id) {

    object Header : DiscoveryItem(Header::class.java.simpleName)

    class Endpoint(
        val endpointId: String,
        val name: String,
        val gender: Gender,
        val profileImageUrl: String?
    ) : DiscoveryItem(endpointId) {

        val genderSign = if (gender == Gender.FEMALE) {
            "♀️"
        } else {
            "♂️"
        }
    }
}
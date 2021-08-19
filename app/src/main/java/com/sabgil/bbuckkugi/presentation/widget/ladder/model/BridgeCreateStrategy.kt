package com.sabgil.bbuckkugi.presentation.widget.ladder.model

interface BridgeCreateStrategy {

    fun createBridgeList(numberOfLadder: Int): List<Bridge>
}
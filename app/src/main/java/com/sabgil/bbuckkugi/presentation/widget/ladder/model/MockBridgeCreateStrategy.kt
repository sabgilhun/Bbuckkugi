package com.sabgil.bbuckkugi.presentation.widget.ladder.model

class MockBridgeCreateStrategy : BridgeCreateStrategy {

    override fun createBridgeList(numberOfLadder: Int): List<Bridge> {
        return listOf(
            Bridge(0, 0),
            Bridge(1, 0),
            Bridge(2, 0),
            Bridge(0, 1),
            Bridge(1, 1),
            Bridge(1, 2),
            Bridge(0, 2),
            Bridge(2, 1),
            Bridge(0, 2),
            Bridge(1, 3),
        )
    }
}
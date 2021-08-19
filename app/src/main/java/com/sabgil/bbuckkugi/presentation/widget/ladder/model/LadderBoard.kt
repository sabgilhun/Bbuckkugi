package com.sabgil.bbuckkugi.presentation.widget.ladder.model

class LadderBoard(
    numberOfPlayer: Int,
    private val bridgeCreateStrategy: BridgeCreateStrategy = RandomBridgeCreateStrategy(),
) {
    private val _bridgeList = mutableListOf<Bridge>()
    val bridgeList: List<Bridge> = _bridgeList

    init {
        _bridgeList.addAll(createBridges(numberOfPlayer))
    }

    private fun createBridges(numberOfPlayer: Int): List<Bridge> {
        return bridgeCreateStrategy.createBridgeList(numberOfPlayer)
    }
}
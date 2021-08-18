package com.sabgil.bbuckkugi.presentation.widget.ladder.model

import kotlin.random.Random

class RandomBridgeCreateStrategy : BridgeCreateStrategy {

    override fun createBridgeList(numberOfLadder: Int): List<Bridge> {
        val bridgeList = mutableListOf<Bridge>()

        for (i in 0 until numberOfLadder - 1) {
            val bridgeCount = Random.nextInt(2, 5)
            repeat(bridgeCount) {
                bridgeList.add(Bridge(i, it))
            }
        }

        return bridgeList.shuffled()
    }
}
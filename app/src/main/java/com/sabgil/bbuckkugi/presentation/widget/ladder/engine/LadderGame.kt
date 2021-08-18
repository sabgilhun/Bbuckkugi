package com.sabgil.bbuckkugi.presentation.widget.ladder.engine

import com.sabgil.bbuckkugi.presentation.widget.ladder.model.Bridge
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.BridgeCreateStrategy
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.LadderBoard
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.Player
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.RandomBridgeCreateStrategy
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.Reword

class LadderGame(
    private val bridgeCreateStrategy: BridgeCreateStrategy = RandomBridgeCreateStrategy(),
) {
    private val playerList = mutableListOf<Player>()
    private val rewordList = mutableListOf<Reword>()

    private lateinit var board: LadderBoard

    val bridgeList: List<Bridge>
        get() = board.bridgeList

    fun setUp(playerList: List<Player>, rewordList: List<Reword>) {
        this.playerList.clear()
        this.playerList.addAll(playerList)
        this.rewordList.clear()
        this.rewordList.addAll(rewordList)

        board = LadderBoard(playerList.size, bridgeCreateStrategy)
    }

    fun comeDownLadder(player: Player) {
        if (player.isArrived()) return

        var currentColumn = playerList.indexOf(player)

        for (i in board.bridgeList.indices) {
            val bridge = board.bridgeList[i]
            if (currentColumn == bridge.column) {
                player.addCrossedBridge(bridge)
                currentColumn++
            } else if (currentColumn == bridge.column + 1) {
                player.addCrossedBridge(bridge)
                currentColumn--
            }
        }

        val reword = rewordList[currentColumn]
        player.saveArrivedColumn(currentColumn)
        player.saveReword(reword)
    }
}
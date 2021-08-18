package com.sabgil.bbuckkugi.presentation.widget.ladder.model

class Player(val name: String) {

    private val _crossedBridgeList = mutableListOf<Bridge>()
    val crossedBridgeList: List<Bridge> = _crossedBridgeList

    var arrivedColumn: Int = -1
        private set

    private var _reword: Reword? = null
    val reword: Reword?
        get() = _reword

    fun addCrossedBridge(bridge: Bridge) {
        _crossedBridgeList.add(bridge)
    }

    fun saveReword(reword: Reword) {
        _reword = reword
    }

    fun clear() {
        _crossedBridgeList.clear()
        _reword = null
        arrivedColumn = -1
    }

    fun isArrived() = reword != null

    fun saveArrivedColumn(arrivedColumn: Int) {
        this.arrivedColumn = arrivedColumn
    }
}
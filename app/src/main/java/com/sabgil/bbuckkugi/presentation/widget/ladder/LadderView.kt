package com.sabgil.bbuckkugi.presentation.widget.ladder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.sabgil.bbuckkugi.common.ext.dpToPx
import com.sabgil.bbuckkugi.presentation.widget.ladder.engine.LadderGame
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.Bridge
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.Player
import com.sabgil.bbuckkugi.presentation.widget.ladder.model.Reword
import com.sabgil.bbuckkugi.presentation.widget.ladder.util.darken
import kotlin.random.Random

class LadderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), AnimationManager.AnimationListener {

    private var playerCount: Int = 4
        set(value) {
            field = value
            replacePlayerAndReword(value)
            ladderGame.setUp(playerList, rewordList)
            setUp()
            resetCache()
            requestLayout()
        }

    private var ladderYBottom: Float = 0f
    private var ladderYTop: Float = 0f
    private val ladderXList = mutableListOf<Float>()
    private var ladderViewWidth = 0

    private val boxPadding: Float = context.dpToPx(10)
    private val outerPadding: Float = context.dpToPx(32)
    private val innerPadding: Float = context.dpToPx(48)
    private val boxRadius: Float = context.dpToPx(8)

    private val ladderGame = LadderGame()

    private val drawingTool = LadderDrawingTool(context)

    private val playerList = mutableListOf<Player>()
    private val playerNameCoordinatesList = mutableListOf<PointF>()
    private val playerNameBoxList = mutableListOf<Rect>()

    private val rewordList = mutableListOf<Reword>()
    private val rewordNameCoordinatesList = mutableListOf<PointF>()
    private val rewordNameBoxList = mutableListOf<Rect>()

    private val bridgeCoordinatesMap = hashMapOf<Bridge, BridgeCoordinates>()
    private val playerCrossedLineMap = hashMapOf<Int, List<RectF>>()

    private val animationManager = AnimationManager(this)
    private val animationCompletedMap = hashMapOf<Int, Boolean>()
    private var animationValueMap = hashMapOf<Int, AnimationValue>()
    private var animationRunningColumnList = mutableListOf<Int>()

    var onPlayerResultListener: OnPlayerResultListener? = null

    init {
        playerCount = 4
        ladderGame.setUp(playerList, rewordList)
        setUp()
    }

    fun gameStart(playerCount: Int) {
        this.playerCount = playerCount
    }

    fun animateAll() {
        for (i in playerList.indices) {
            val player = playerList[i]
            val rewordColumn = player.arrivedColumn
            val isAnimationCompleted = animationCompletedMap[rewordColumn] ?: false

            if (!isAnimationCompleted) {
                animationRunningColumnList.add(i)
                ladderGame.comeDownLadder(player)
                val lineList = playerCrossedLineMap.getOrPut(i) { createPlayerCrossedLineList(i) }
                animationManager.animate(i, lineList, false)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animationManager.onLadderScrollChangeListener = parent as? OnLadderScrollChangeListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = ladderViewWidth
        var parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (parentHeight == 0) {
            // custom view 의 높이를 설정한다.
            parentHeight = context.dpToPx(300).toInt()
        }
        setMeasuredDimension(width, parentHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        setUpLadderLine()
        setUpRewordNameTextView()
        setUpBridgeList()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        drawPlayer(canvas)
        drawLadder(canvas)
        drawReword(canvas)
        drawBridge(canvas)
        drawArrivedBridge(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true
        performClick()

        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_UP -> {
                val column = findTouchedColumn(x.toInt(), y.toInt()) ?: return true
                if (animationManager.isRunning()) return true

                onLadderColumnTouchEvent(column)
            }
            MotionEvent.ACTION_MOVE -> {
                super.onTouchEvent(event)
            }
        }

        return true
    }

    private fun findTouchedColumn(x: Int, y: Int): Int? {
        playerNameBoxList.forEachIndexed { index, rect ->
            if (rect.contains(x, y)) {
                return index
            }
        }
        return null
    }

    private fun onLadderColumnTouchEvent(column: Int) {
        animationRunningColumnList.clear()
        animationRunningColumnList.add(column)

        val player = playerList[column]
        ladderGame.comeDownLadder(player)

        val lineList = playerCrossedLineMap.getOrPut(column) { createPlayerCrossedLineList(column) }

        if (animationCompletedMap[player.arrivedColumn] == true) {
            invalidate()
        } else {
            animationManager.animate(column, lineList, true)
        }
    }

    @Suppress("RedundantOverride")
    override fun performClick(): Boolean {
        // onTouchEvent 를 오버라이딩 했기 때문에 performClick 도 오버라이딩함
        return super.performClick()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw IllegalStateException("must not call setOnClickListener.")
    }

    override fun onAnimationUpdate(column: Int, animationValue: AnimationValue) {
        animationValueMap[column] = animationValue
        invalidate()
    }

    override fun onAnimationComplete(column: Int) {
        val player = playerList[column]
        val rewordColumn = player.arrivedColumn
        val reword = rewordList[rewordColumn]
        animationCompletedMap[rewordColumn] = true
        invalidate()

        onPlayerResultListener?.onResult(player.name, reword.name)
    }

    private fun replacePlayerAndReword(numberOfPlayer: Int) {
        val playerList = MutableList(numberOfPlayer) { Player("참여${it + 1}") }
        this.playerList.clear()
        this.playerList.addAll(playerList)

        val rewordList = MutableList(numberOfPlayer) { Reword("꽝") }
        val index = Random.nextInt(0, numberOfPlayer)
        rewordList[index] = Reword("당첨!")
        this.rewordList.clear()
        this.rewordList.addAll(rewordList)
    }

    private fun resetCache() {
        bridgeCoordinatesMap.clear()
        playerCrossedLineMap.clear()
        animationCompletedMap.clear()
        animationRunningColumnList.clear()

        setUpBridgeList()
    }

    private fun createPlayerCrossedLineList(column: Int): List<RectF> {
        if (0 > column || playerList.size <= column) return emptyList()

        val player = playerList[column]
        val lineList = mutableListOf<RectF>()

        var tempColumn = column
        var tempX = ladderXList[tempColumn]
        var tempYTop = ladderYTop

        player.crossedBridgeList.forEach { bridge ->
            val coordinates = bridgeCoordinatesMap[bridge] ?: return@forEach

            coordinates.run {
                lineList.add(RectF(tempX, tempYTop, tempX, y))

                // 좌 -> 우
                tempX = if (bridge.column == tempColumn) {
                    lineList.add(RectF(startX, y, endX, y))
                    tempColumn++
                    endX
                    // 좌 <- 우
                } else {
                    lineList.add(RectF(endX, y, startX, y))
                    tempColumn--
                    startX
                }
                tempYTop = y
            }
        }

        lineList.add(RectF(tempX, tempYTop, tempX, ladderYBottom))

        return lineList
    }

    private fun drawPlayer(canvas: Canvas) {
        for (i in playerList.indices) {
            val player = playerList[i]
            val name = player.name
            val coordinates = playerNameCoordinatesList[i]
            val rect = playerNameBoxList[i]

            if (player.isArrived()) {
                drawingTool.boxPaint.style = Paint.Style.FILL
                drawingTool.boxPaint.color = getPlayerColor(player).darken
            } else {
                drawingTool.boxPaint.style = Paint.Style.FILL
                drawingTool.boxPaint.color = getPlayerColor(player)
            }

            canvas.drawRoundRect(
                rect.left.toFloat(),
                rect.top.toFloat(),
                rect.right.toFloat(),
                rect.bottom.toFloat(),
                boxRadius,
                boxRadius,
                drawingTool.boxPaint
            )

            drawingTool.textPaint.color = Color.WHITE
            canvas.drawText(name, coordinates.x, coordinates.y, drawingTool.textPaint)
        }
    }

    private fun drawLadder(canvas: Canvas) {
        for (i in ladderXList) {
            // 수직 사다리
            canvas.drawLine(i, ladderYTop, i, ladderYBottom, drawingTool.linePaint)
        }
    }

    private fun drawReword(canvas: Canvas) {
        for (i in rewordList.indices) {
            val name = rewordList[i].name
            val coordinates = rewordNameCoordinatesList[i]

            val rect = rewordNameBoxList[i]

            val arrivedPlayer = searchArrivedPlayer(i)

            // 애니메이션 종료 여부
            val isAnimationCompleted = animationCompletedMap[i] ?: false

            if (arrivedPlayer != null && isAnimationCompleted) {
                drawingTool.boxPaint.style = Paint.Style.STROKE
                drawingTool.boxPaint.color = getPlayerColor(arrivedPlayer)
            } else {
                drawingTool.boxPaint.style = Paint.Style.STROKE
                drawingTool.boxPaint.color = drawingTool.defaultColor
            }

            val top = rect.top + ladderYBottom.toInt()
            val bottom = rect.bottom + ladderYBottom.toInt()

            canvas.drawRoundRect(
                rect.left.toFloat(),
                top.toFloat(),
                rect.right.toFloat(),
                bottom.toFloat(),
                boxRadius,
                boxRadius,
                drawingTool.boxPaint
            )

            drawingTool.textPaint.color = Color.BLACK
            canvas.drawText(name, coordinates.x, coordinates.y, drawingTool.textPaint)
        }
    }

    private fun drawBridge(canvas: Canvas) {
        bridgeCoordinatesMap.values.forEach {
            canvas.drawLine(
                it.startX,
                it.y,
                it.endX,
                it.y,
                drawingTool.linePaint
            )
        }
    }

    private fun drawArrivedBridge(canvas: Canvas) {
        if (animationRunningColumnList.isEmpty()) return

        for (column in animationRunningColumnList) {
            val player = playerList[column]
            drawingTool.bridgePaint.color = getPlayerColor(player)

            val lineList = playerCrossedLineMap[column] ?: return

            val animationValue = animationValueMap[column]
            var runningAnimationPosition =
                animationValue?.runningAnimationPosition ?: AnimationManager.VALUE_NONE
            if (animationCompletedMap[player.arrivedColumn] == true) {
                runningAnimationPosition = lineList.size
            }

            for (i in 0 until runningAnimationPosition) {
                if (i < lineList.size) {
                    val rect = lineList[i]
                    canvas.drawLine(
                        rect.left,
                        rect.top,
                        rect.right,
                        rect.bottom,
                        drawingTool.bridgePaint
                    )
                }
            }

            if (runningAnimationPosition > AnimationManager.VALUE_NONE && runningAnimationPosition < lineList.size) {
                val rect = lineList[runningAnimationPosition]

                val stopX = animationValue?.x ?: return
                val stopY = animationValue.y

                canvas.drawLine(
                    rect.left,
                    rect.top,
                    stopX.toFloat(),
                    stopY.toFloat(),
                    drawingTool.bridgePaint
                )
            }
        }
    }

    private fun searchArrivedPlayer(columnIndex: Int): Player? {
        for (player in playerList) {
            if (player.isArrived() && columnIndex == player.arrivedColumn) {
                return player
            } else {
                continue
            }
        }
        return null
    }

    @ColorInt
    private fun getPlayerColor(player: Player): Int {
        if (playerList.isNotEmpty()) {
            val i = playerList.indexOf(player)
            return drawingTool.getColor(i)
        }
        return 0
    }

    private fun setUp() {
        setUpPlayerNameTextView()
        setUpLadderXList()
        setUpRewordNameTextView()
        setUpWidth()
    }

    private fun setUpRewordNameTextView() {
        // 리워드 텍스트 너비
        val rewordNameWidthList = rewordList.map { drawingTool.textPaint.measureText(it.name) }
        val rewordNameBoxWidth = (rewordNameWidthList.maxOrNull() ?: 0f) + (boxPadding * 2)

        // 리워드 텍스트 위치
        val rewordNameCoordinatesList = rewordNameWidthList.mapIndexed { i, _ ->
            val x = ladderXList[i] - (rewordNameWidthList[i] / 2)
            val y = ladderYBottom + boxPadding + innerPadding / 2

            PointF(x, y)
        }

        this.rewordNameCoordinatesList.clear()
        this.rewordNameCoordinatesList.addAll(rewordNameCoordinatesList)

        val rewordNameBoxList = MutableList(rewordList.size) {
            val rect = Rect()
            val name = rewordList[it].name
            drawingTool.textPaint.getTextBounds(name, 0, name.length, rect)

            val left = ladderXList[it].toInt() - (rewordNameBoxWidth / 2).toInt()

            Rect(
                left,
                outerPadding.toInt() + rect.top - boxPadding.toInt(),
                left + rewordNameBoxWidth.toInt(),
                outerPadding.toInt() + rect.bottom + boxPadding.toInt(),
            )
        }

        this.rewordNameBoxList.clear()
        this.rewordNameBoxList.addAll(rewordNameBoxList)
    }

    private fun setUpWidth() {
        val lastBoxRight = playerNameBoxList.lastOrNull()?.right
        ladderViewWidth = (lastBoxRight ?: 0) + outerPadding.toInt()
    }

    private fun setUpLadderXList() {
        if (playerNameBoxList.isNotEmpty()) {
            val playerNameWidth = playerNameBoxList[0].right - playerNameBoxList[0].left
            val ladderXList = playerNameBoxList.map { it.left.toFloat() + playerNameWidth / 2 }

            this.ladderXList.clear()
            this.ladderXList.addAll(ladderXList)
        }
    }

    private fun setUpLadderLine() {
        if (playerNameBoxList.isNotEmpty()) {
            ladderYTop = playerNameBoxList[0].bottom.toFloat() + innerPadding / 2
            ladderYBottom = ladderYTop + (height / 1.5).toFloat()
        }
    }

    private fun setUpPlayerNameTextView() {
        // 플레이어 텍스트 너비
        val playerNameWidthList = playerList.map { drawingTool.textPaint.measureText(it.name) }
        val playerNameWidth = (playerNameWidthList.maxOrNull() ?: 0f) + (boxPadding * 2)

        val playerNameCoordinatesList = playerNameWidthList.mapIndexed { i, width ->
            val namePaddingSize = (playerNameWidth - width) / 2
            PointF(
                outerPadding + (i * playerNameWidth) + (i * innerPadding) + namePaddingSize,
                outerPadding + boxPadding
            )
        }

        this.playerNameCoordinatesList.clear()
        this.playerNameCoordinatesList.addAll(playerNameCoordinatesList)

        // 플레이어 박스 사이즈
        val playerNameBoxList = MutableList(playerList.size) {
            val rect = Rect()
            val name = playerList[it].name
            drawingTool.textPaint.getTextBounds(name, 0, name.length, rect)

            val x = outerPadding + (it * playerNameWidth) + (it * innerPadding)

            Rect(
                x.toInt(),
                outerPadding.toInt() + rect.top,
                x.toInt() + playerNameWidth.toInt(),
                outerPadding.toInt() + rect.bottom + (boxPadding.toInt() * 2),
            )
        }

        this.playerNameBoxList.clear()
        this.playerNameBoxList.addAll(playerNameBoxList)
    }

    private fun setUpBridgeList() {
        // 가로 다리 생성
        val interval = (ladderYBottom - ladderYTop) / (ladderGame.bridgeList.size + 1)
        var y = ladderYTop + interval

        bridgeCoordinatesMap.clear()

        ladderGame.bridgeList.forEach { bridge ->
            bridgeCoordinatesMap[bridge] = BridgeCoordinates(
                ladderXList[bridge.column],
                ladderXList[bridge.column + 1],
                y
            )

            y += interval
        }
    }

    fun interface OnPlayerResultListener {
        fun onResult(player: String, reword: String)
    }
}
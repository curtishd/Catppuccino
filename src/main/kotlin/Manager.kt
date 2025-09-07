package me.cdh

import me.cdh.CatAnimationManager.currentAction
import me.cdh.CatAnimationManager.state
import me.cdh.CatAnimationManager.wanderLoc
import me.cdh.CatAnimationManager.win
import java.awt.Point
import java.awt.Toolkit
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.random.Random

object CatAnimationManager {
    val win = CatWindow()
    private val frames by lazy { ResourceLoader.loadAllFrames<Behave>() }
    private val bubbleFrames by lazy { ResourceLoader.loadAllFrames<BubbleState>() }
    var frameNum = 0
    var currentAction = Behave.SLEEP
    lateinit var currFrames: List<BufferedImage>
    lateinit var currBubbleFrames: List<BufferedImage>
    var layingDir = Direction.RIGHT
    var state = State.DEFAULT
    var wanderLoc = Point(0, 0)
    var bubbleState = BubbleState.NONE
    var bubbleFrame = 0
    var bubbleSteps = 0
    var animationSteps = 0

    init {
        ResourceLoader.preloadCommonResources()
    }

    fun changeAction(behave: Behave): Boolean = if (currentAction != behave) {
        currentAction = behave
        currFrames = frames[currentAction.name]!!
        true
    } else {
        false
    }

    fun updateAnimation() {
        animationSteps++
        if (animationSteps >= currentAction.delay) {
            when (currentAction) {
                Behave.LAYING if frameNum == currentAction.frame - 1 -> {
                    if ((animationSteps - currentAction.delay) > 40) {
                        animationSteps = 0
                        frameNum = 0
                        if (Random.nextBoolean()) changeAction(Behave.CURLED) else changeAction(Behave.SLEEP)
                    }
                }

                Behave.SITTING if frameNum == currentAction.frame - 1 -> {
                    changeAction(Behave.LICKING)
                    animationSteps = 0
                    frameNum = 0
                }

                else -> {
                    frameNum++
                    animationSteps = 0
                }
            }

        }
        if (frameNum >= currentAction.frame) frameNum = 0
    }

    fun manageBubbleState() {
        if (bubbleState != BubbleState.HEART) {
            if (currentAction == Behave.SLEEP || currentAction == Behave.CURLED) bubbleState = BubbleState.ZZZ
            else if (currentAction != Behave.SITTING) bubbleState = BubbleState.NONE
        }
        bubbleSteps++
        currBubbleFrames = bubbleFrames.getOrDefault(bubbleState.name, bubbleFrames[BubbleState.HEART.name])!!
        if (bubbleSteps >= bubbleState.delay) {
            bubbleFrame++
            bubbleSteps = 0
        }
        if (bubbleFrame >= bubbleState.frame) {
            bubbleFrame = 0
            if (bubbleState == BubbleState.HEART) bubbleState = BubbleState.NONE
        }
    }

    fun handleFrames() {
        if (currentAction != Behave.RISING) {
            if (state == State.WANDER) {
                val curPos = win.locationOnScreen
                if (abs(curPos.x - wanderLoc.x) >= 3) {
                    if (curPos.x > wanderLoc.x) changeAction(Behave.LEFT)
                    else changeAction(Behave.RIGHT)

                } else {
                    if (curPos.y > wanderLoc.y) changeAction(Behave.UP)
                    else changeAction(Behave.DOWN)
                }
                state = if (wanderLoc.distance(curPos) < 3) State.DEFAULT else State.WANDER
            }
            var flag = false
            when {
                currentAction == Behave.LEFT -> layingDir = Direction.LEFT
                currentAction == Behave.RIGHT -> layingDir = Direction.RIGHT
                state != State.WANDER && (currentAction == Behave.UP || currentAction == Behave.DOWN) -> flag =
                    if (Random.nextInt(3) >= 1) changeAction(Behave.LAYING)
                    else changeAction(Behave.SITTING)
            }
            if (flag) frameNum = 0
        }
    }
}

object CatMovementManager {

    fun performMovement() {
        val loc = win.location
        when (currentAction) {
            Behave.RIGHT -> loc.translate(1, 0)
            Behave.LEFT -> loc.translate(-1, 0)
            Behave.UP -> loc.translate(0, -1)
            Behave.DOWN -> loc.translate(0, 1)
            else -> {}
        }
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        when {
            loc.x > screenSize.width - win.width -> loc.setLocation(screenSize.width - win.width, loc.y)
            loc.x < -10 -> loc.setLocation(-10, loc.y)
            loc.y > screenSize.height - win.height -> loc.setLocation(loc.x, screenSize.height - win.height)
            loc.y < -35 -> loc.setLocation(loc.x, -35)
        }
        win.location = loc
    }

    fun tryWandering() {
        if (Random.nextBoolean()) return
        state = State.WANDER
        val screenLoc = win.locationOnScreen
        var loc: Point
        do {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            loc = Point(
                Random.nextInt(screenSize.width - win.width - 20) + 10,
                Random.nextInt(screenSize.height - win.height - 20) + 10
            )
        } while (abs(screenLoc.y - loc.y) <= 400 && abs(screenLoc.x - loc.x) <= 400)
        wanderLoc = loc
    }
}
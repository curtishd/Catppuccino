package me.cdh.conf

import me.cdh.Behave
import me.cdh.BubbleState
import me.cdh.Direction
import me.cdh.State
import java.awt.Point
import java.awt.image.BufferedImage

object CatProp {
    val win = CatWindow()
    val frames = ResourceLoader.loadAllFrames<Behave>()
    val bubbleFrames = ResourceLoader.loadAllFrames<BubbleState>()
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
}

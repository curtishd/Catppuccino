package me.cdh

import java.awt.Point
import java.awt.image.BufferedImage

object CatApp {
    val win = CatWindow()
    val resourceLoader = ResourceLoader()
    val frames = resourceLoader.loadFrames<Behave>()
    val bubbleFrames = resourceLoader.loadFrames<BubbleState>()
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

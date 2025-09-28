package me.cdh.conf

import me.cdh.conf.CatProp.animationSteps
import me.cdh.conf.CatProp.bubbleFrame
import me.cdh.conf.CatProp.bubbleFrames
import me.cdh.conf.CatProp.bubbleState
import me.cdh.conf.CatProp.bubbleSteps
import me.cdh.conf.CatProp.currBubbleFrames
import me.cdh.conf.CatProp.currFrames
import me.cdh.conf.CatProp.currentAction
import me.cdh.conf.CatProp.frameNum
import me.cdh.conf.CatProp.frames
import me.cdh.conf.CatProp.layingDir
import me.cdh.conf.CatProp.state
import me.cdh.conf.CatProp.wanderLoc
import me.cdh.conf.CatProp.win
import me.cdh.Behave
import me.cdh.BubbleState
import me.cdh.Direction
import me.cdh.State
import me.cdh.Random
import kotlin.math.abs

object CatAnimationManager {

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
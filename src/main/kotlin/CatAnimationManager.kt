package me.cdh

import kotlin.collections.getOrDefault
import kotlin.math.abs
import kotlin.random.Random

object CatAnimationManager {

    fun changeAction(behave: Behave): Boolean = if (CatApp.currentAction != behave) {
        CatApp.currentAction = behave
        CatApp.currFrames = CatApp.frames[CatApp.currentAction.name]!!
        true
    } else {
        false
    }

    fun updateAnimation() {
        CatApp.animationSteps++
        if (CatApp.animationSteps >= CatApp.currentAction.delay) {
            when (CatApp.currentAction) {
                Behave.LAYING if CatApp.frameNum == CatApp.currentAction.frame - 1 -> {
                    if ((CatApp.animationSteps - CatApp.currentAction.delay) > 40) {
                        CatApp.animationSteps = 0
                        CatApp.frameNum = 0
                        if (Random.nextBoolean()) changeAction(Behave.CURLED) else changeAction(Behave.SLEEP)
                    }
                }

                Behave.SITTING if CatApp.frameNum == CatApp.currentAction.frame - 1 -> {
                    changeAction(Behave.LICKING)
                    CatApp.animationSteps = 0
                    CatApp.frameNum = 0
                }

                else -> {
                    CatApp.frameNum++
                    CatApp.animationSteps = 0
                }
            }

        }
        if (CatApp.frameNum >= CatApp.currentAction.frame) CatApp.frameNum = 0
    }

    fun manageBubbleState() {
        if (CatApp.bubbleState != BubbleState.HEART) {
            if (CatApp.currentAction == Behave.SLEEP || CatApp.currentAction == Behave.CURLED) CatApp.bubbleState = BubbleState.ZZZ
            else if (CatApp.currentAction != Behave.SITTING) CatApp.bubbleState = BubbleState.NONE
        }
        CatApp.bubbleSteps++
        CatApp.currBubbleFrames = CatApp.bubbleFrames.getOrDefault(CatApp.bubbleState.name, CatApp.bubbleFrames[BubbleState.HEART.name])!!
        if (CatApp.bubbleSteps >= CatApp.bubbleState.delay) {
            CatApp.bubbleFrame++
            CatApp.bubbleSteps = 0
        }
        if (CatApp.bubbleFrame >= CatApp.bubbleState.frame) {
            CatApp.bubbleFrame = 0
            if (CatApp.bubbleState == BubbleState.HEART) CatApp.bubbleState = BubbleState.NONE
        }
    }

    fun handleFrames() {
        if (CatApp.currentAction != Behave.RISING) {
            if (CatApp.state == State.WANDER) {
                val curPos = CatApp.win.locationOnScreen
                if (abs(curPos.x - CatApp.wanderLoc.x) >= 3) {
                    if (curPos.x > CatApp.wanderLoc.x) changeAction(Behave.LEFT)
                    else changeAction(Behave.RIGHT)

                } else {
                    if (curPos.y > CatApp.wanderLoc.y) changeAction(Behave.UP)
                    else changeAction(Behave.DOWN)
                }
                CatApp.state = if (CatApp.wanderLoc.distance(curPos) < 3) State.DEFAULT else State.WANDER
            }
            var flag = false
            when {
                CatApp.currentAction == Behave.LEFT -> CatApp.layingDir = Direction.LEFT
                CatApp.currentAction == Behave.RIGHT -> CatApp.layingDir = Direction.RIGHT
                CatApp.state != State.WANDER && (CatApp.currentAction == Behave.UP || CatApp.currentAction == Behave.DOWN) -> flag =
                    if (Random.nextInt(3) >= 1) changeAction(Behave.LAYING)
                    else changeAction(Behave.SITTING)
            }
            if (flag) CatApp.frameNum = 0
        }
    }
}
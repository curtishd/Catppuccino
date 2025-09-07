package me.cdh

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.time.LocalDateTime

fun main() {
    runBlocking {
        launch(Dispatchers.Swing) {
            initSystemTray()
            CatAnimationManager.changeAction(Behave.CURLED)
        }
        launch {
            delay(10000L)
            System.gc()
        }
        launch {
            while (isActive) {
                CatAnimationManager.handleFrames()
                CatMovementManager.performMovement()
                CatAnimationManager.updateAnimation()
                CatAnimationManager.manageBubbleState()
                CatAnimationManager.win.repaint()
                delay(ANIMATION_UPDATE_DELAY)
            }
        }
        launch {
            val wanderInterval =
                if (LocalDateTime.now().hour in 8..18)
                    DAYTIME_WANDER_INTERVAL
                else
                    NIGHTTIME_WANDER_INTERVAL
            while (isActive) {
                CatMovementManager.tryWandering()
                delay(wanderInterval)
            }
        }
    }
}
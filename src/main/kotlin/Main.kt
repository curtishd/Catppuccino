package me.cdh

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import me.cdh.conf.CatAnimationManager
import me.cdh.conf.CatMovementManager
import me.cdh.constant.Behave
import java.time.LocalDateTime

fun main() {
    runBlocking {
        launch(Dispatchers.Swing) {
            initSystemTray()
            CatAnimationManager.changeAction(Behave.CURLED)
        }
        launch {
            while (isActive) {
                System.gc()
                delay(10000L)
            }
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
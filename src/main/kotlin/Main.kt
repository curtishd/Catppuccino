package me.cdh

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import me.cdh.conf.CatAnimationManager
import me.cdh.conf.CatMovementManager
import me.cdh.conf.CatProp
import java.time.LocalDateTime


object Main {
    private fun initSystemTrayAndCat() {
        initSystemTray()
        CatAnimationManager.changeAction(Behave.CURLED)
    }

    private suspend fun CoroutineScope.startMainAnimationLoop() {
        while (isActive) {
            CatAnimationManager.handleFrames()
            CatMovementManager.performMovement()
            CatAnimationManager.updateAnimation()
            CatAnimationManager.manageBubbleState()
            CatProp.win.repaint()
            delay(ANIMATION_UPDATE_DELAY)
        }
    }

    private suspend fun CoroutineScope.startWanderingBehavior() {
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

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            launch(Dispatchers.Swing.limitedParallelism(1)) { initSystemTrayAndCat() }
            launch { startMainAnimationLoop() }
            launch { startWanderingBehavior() }
        }
    }
}
package me.cdh

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import me.cdh.conf.*
import java.time.LocalDateTime

private fun initSystemTrayAndCat() {
    initSystemTray()
    changeAction(Behave.CURLED)
}

private suspend fun CoroutineScope.startMainAnimationLoop() {
    while (isActive) {
        handleFrames()
        performMovement()
        updateAnimation()
        manageBubbleState()
        window.repaint()
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
        tryWandering()
        delay(wanderInterval)
    }
}

fun main() {
    runBlocking {
        launch(Dispatchers.Swing.limitedParallelism(1)) { initSystemTrayAndCat() }
        launch { startMainAnimationLoop() }
        launch { startWanderingBehavior() }
    }
}
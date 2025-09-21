package me.cdh.conf

import me.cdh.constant.Behave
import me.cdh.constant.State
import java.awt.Point
import java.awt.Toolkit
import kotlin.math.abs
import kotlin.random.Random

object CatMovementManager {

    fun performMovement() {
        val loc = CatAnimationManager.win.location
        when (CatAnimationManager.currentAction) {
            Behave.RIGHT -> loc.translate(1, 0)
            Behave.LEFT -> loc.translate(-1, 0)
            Behave.UP -> loc.translate(0, -1)
            Behave.DOWN -> loc.translate(0, 1)
            else -> {}
        }
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        when {
            loc.x > screenSize.width - CatAnimationManager.win.width -> loc.setLocation(screenSize.width - CatAnimationManager.win.width, loc.y)
            loc.x < -10 -> loc.setLocation(-10, loc.y)
            loc.y > screenSize.height - CatAnimationManager.win.height -> loc.setLocation(loc.x, screenSize.height - CatAnimationManager.win.height)
            loc.y < -35 -> loc.setLocation(loc.x, -35)
        }
        CatAnimationManager.win.location = loc
    }

    fun tryWandering() {
        if (Random.nextBoolean()) return
        CatAnimationManager.state = State.WANDER
        val screenLoc = CatAnimationManager.win.locationOnScreen
        var loc: Point
        do {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            loc = Point(
                Random.nextInt(screenSize.width - CatAnimationManager.win.width - 20) + 10,
                Random.nextInt(screenSize.height - CatAnimationManager.win.height - 20) + 10
            )
        } while (abs(screenLoc.y - loc.y) <= 400 && abs(screenLoc.x - loc.x) <= 400)
        CatAnimationManager.wanderLoc = loc
    }
}
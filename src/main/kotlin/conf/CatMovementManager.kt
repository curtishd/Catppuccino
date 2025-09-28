package me.cdh.conf

import me.cdh.conf.CatProp.currentAction
import me.cdh.conf.CatProp.state
import me.cdh.conf.CatProp.wanderLoc
import me.cdh.Behave
import me.cdh.State
import me.cdh.conf.CatProp.win
import me.cdh.Random
import java.awt.Point
import java.awt.Toolkit
import kotlin.math.abs

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
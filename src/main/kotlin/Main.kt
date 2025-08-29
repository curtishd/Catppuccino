package me.cdh

import me.cdh.enumerate.Behave
import javax.swing.SwingUtilities
import kotlin.concurrent.timer

fun main() {
    SwingUtilities.invokeLater {
        initSystemTray()
        Compose.changeAction(Behave.CURLED)
        timer(period = 10L, action = {
            Compose.updateBehave()
            Compose.perform()
            Compose.updateAnimation()
            Compose.manageBubbleState()
            Compose.win.repaint()
        })
        if (!isDaytime()) timer(period = 30000L, action = { Compose.tryWandering() })
        else timer(period = 6000L, action = { Compose.tryWandering() })
    }
}
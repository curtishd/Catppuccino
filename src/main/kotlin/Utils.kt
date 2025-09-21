package me.cdh

import me.cdh.conf.Stage
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.system.exitProcess

const val PROJECT_NAME = "catppuccino"
const val WINDOW_WIDTH = 100
const val WINDOW_HEIGHT = 100
const val ANIMATION_UPDATE_DELAY = 10L
const val DAYTIME_WANDER_INTERVAL = 6000L
const val NIGHTTIME_WANDER_INTERVAL = 30000L
const val BUBBLE_SIZE = 30

fun flipImg(img: BufferedImage): BufferedImage {
    val imageToFlip = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB)
    val g2d = imageToFlip.createGraphics()
    g2d.drawImage(img, img.width, 0, -img.width, img.height, null)
    g2d.dispose()
    return imageToFlip
}

fun initSystemTray() {
    if (!SystemTray.isSupported()) return
    val iconSize = SystemTray.getSystemTray().trayIconSize
    val trayIcon = TrayIcon(
        ImageIO.read(Stage::class.java.classLoader.getResourceAsStream("$PROJECT_NAME.png")).getScaledInstance(
            iconSize.width, iconSize.height, Image.SCALE_SMOOTH
        ), PROJECT_NAME
    )
    val popupMenu = PopupMenu()
    val exit = MenuItem("Exit")
    exit.addActionListener { exitProcess(0) }
    popupMenu.add(exit)
    trayIcon.popupMenu = popupMenu
    SystemTray.getSystemTray().add(trayIcon)
}
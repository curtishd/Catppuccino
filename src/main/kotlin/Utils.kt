package me.cdh

import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.system.exitProcess

fun flipImg(img: BufferedImage): BufferedImage =
    BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB).apply {
        val g2d = createGraphics()
        g2d.drawImage(img, img.width, 0, -img.width, img.height, null)
        g2d.dispose()

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
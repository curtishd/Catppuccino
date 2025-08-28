package me.cdh

import java.awt.Image
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.time.LocalDateTime
import javax.imageio.ImageIO
import kotlin.system.exitProcess

fun flipImg(img: BufferedImage): BufferedImage {
    val mirror = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB)
    val graphics = mirror.createGraphics()
    val trans = AffineTransform()
    trans.concatenate(AffineTransform.getScaleInstance(-1.0, 1.0))
    trans.concatenate(AffineTransform.getTranslateInstance(-img.width.toDouble(), 0.0))
    graphics.transform(trans)
    graphics.drawImage(img, 0, 0, null)
    graphics.dispose()
    return mirror
}


fun initSystemTray() {
    if (!SystemTray.isSupported()) return
    val iconSize = SystemTray.getSystemTray().trayIconSize
    val trayIcon = TrayIcon(
        ImageIO.read(Showcase::class.java.classLoader.getResourceAsStream("kitty.png")).getScaledInstance(
            iconSize.width, iconSize.height,
            Image.SCALE_SMOOTH
        ), "kitty"
    )
    val popupMenu = PopupMenu()
    val exit = MenuItem("Exit")
    exit.addActionListener { exitProcess(0) }
    popupMenu.add(exit)
    trayIcon.popupMenu = popupMenu
    SystemTray.getSystemTray().add(trayIcon)
}

fun isDaytime(): Boolean = LocalDateTime.now().hour in 8..<18
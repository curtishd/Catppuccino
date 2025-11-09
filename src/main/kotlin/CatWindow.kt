package me.cdh

import me.cdh.CatAnimationManager.changeAction
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JWindow

class CatWindow : JWindow() {
    init {
        type = Type.UTILITY
        val dim = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        preferredSize = dim
        minimumSize = dim
        setLocationRelativeTo(null)
        isAlwaysOnTop = true
        val mouseAdapter = object : MouseAdapter() {
            private var dragOffset: Point? = null
            override fun mousePressed(e: MouseEvent) {
                super.mousePressed(e)
                dragOffset = Point(e.x, e.y)
            }

            override fun mouseDragged(e: MouseEvent) {
                super.mouseDragged(e)
                dragOffset?.let {
                    setLocation(e.locationOnScreen.x - it.x, e.locationOnScreen.y - it.y)
                }
                if (changeAction(Behave.RISING)) CatApp.frameNum = 0
            }

            override fun mouseReleased(e: MouseEvent) {
                super.mouseReleased(e)
                if (CatApp.currentAction == Behave.RISING) {
                    changeAction(Behave.LAYING)
                    CatApp.frameNum = 0
                }
            }

            override fun mouseClicked(e: MouseEvent) {
                super.mouseClicked(e)
                CatApp.bubbleState = BubbleState.HEART
                CatApp.bubbleFrame = 0
            }
        }
        addMouseMotionListener(mouseAdapter)
        addMouseListener(mouseAdapter)
        background = Color(1.0f, 1.0f, 1.0f, 0.0f)
        isVisible = true
        add(Stage())
    }
}
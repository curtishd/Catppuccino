package me.cdh

import me.cdh.CatAnimationManager.bubbleFrame
import me.cdh.CatAnimationManager.bubbleState
import me.cdh.CatAnimationManager.changeAction
import me.cdh.CatAnimationManager.currentAction
import me.cdh.CatAnimationManager.frameNum
import java.awt.Color
import java.awt.Dimension
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
        addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent?) {
                setLocation(e!!.locationOnScreen.x - width / 2, e.locationOnScreen.y - height / 2)
                if (changeAction(Behave.RISING)) frameNum = 0
            }
        })
        addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent?) {
                if (currentAction == Behave.RISING) {
                    changeAction(Behave.LAYING)
                    frameNum = 0
                }
            }

            override fun mouseClicked(e: MouseEvent?) {
                bubbleState = BubbleState.HEART
                bubbleFrame = 0
            }
        })
        background = Color(1.0f, 1.0f, 1.0f, 0.0f)
        isVisible = true
        add(Stage)
    }
}
package me.cdh.conf

import me.cdh.WINDOW_HEIGHT
import me.cdh.WINDOW_WIDTH
import me.cdh.conf.CatAnimationManager.changeAction
import me.cdh.conf.CatProp.bubbleFrame
import me.cdh.conf.CatProp.bubbleState
import me.cdh.conf.CatProp.currentAction
import me.cdh.conf.CatProp.frameNum
import me.cdh.constant.Behave
import me.cdh.constant.BubbleState
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
                e ?: return
                setLocation(e.locationOnScreen.x - width / 2, e.locationOnScreen.y - height / 2)
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
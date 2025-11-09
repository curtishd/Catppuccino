package me.cdh

import java.awt.Graphics
import java.awt.Point
import javax.swing.JPanel

class Stage : JPanel() {

    init {
        isOpaque = false
    }

    private fun needsFlipping(behave: Behave, direction: Direction) =
        (behave == Behave.LAYING || behave == Behave.RISING || behave == Behave.SLEEP)
                && direction == Direction.LEFT
                || behave == Behave.CURLED
                && direction == Direction.RIGHT

    private fun calculateBubblePosition(action: Behave, direction: Direction): Point {
        return when (action) {
            Behave.SLEEP, Behave.LAYING, Behave.LEFT, Behave.RIGHT -> {
                val x = if (direction == Direction.LEFT) 0 else 30 + 30
                Point(x, 40)
            }

            Behave.UP, Behave.LICKING, Behave.SITTING -> {
                Point(30, 40 - 25)
            }

            else -> Point(30, 40)
        }
    }

    override fun paintComponent(g: Graphics?) {
        val img = CatApp.currFrames.getOrNull(CatApp.frameNum) ?: return
        val flippedImage = if (needsFlipping(CatApp.currentAction, CatApp.layingDir)) flipImg(img) else img
        g?.drawImage(flippedImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null) ?: return
        if (CatApp.bubbleState != BubbleState.NONE) {
            CatApp.currBubbleFrames.getOrNull(CatApp.bubbleFrame)?.let { bubbleImg ->
                val position = calculateBubblePosition(CatApp.currentAction, CatApp.layingDir)
                g.drawImage(bubbleImg, position.x, position.y, BUBBLE_SIZE, BUBBLE_SIZE, null)
            }
        }
    }
}
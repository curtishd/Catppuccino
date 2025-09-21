package me.cdh.conf

import me.cdh.BUBBLE_SIZE
import me.cdh.WINDOW_HEIGHT
import me.cdh.WINDOW_WIDTH
import me.cdh.constant.Behave
import me.cdh.constant.BubbleState
import me.cdh.constant.Direction
import me.cdh.flipImg
import java.awt.Graphics
import java.awt.Point
import javax.swing.JPanel

object Stage : JPanel() {

    const val BASE_X = 30
    const val BASE_Y = 40

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
                val x = if (direction == Direction.LEFT) 0 else BASE_X + 30
                Point(x, BASE_Y)
            }

            Behave.UP, Behave.LICKING, Behave.SITTING -> {
                Point(BASE_X, BASE_Y - 25)
            }

            else -> Point(BASE_X, BASE_Y)
        }
    }

    override fun paintComponent(g: Graphics?) {
        val manager = CatAnimationManager
        val img = manager.currFrames.getOrNull(CatAnimationManager.frameNum) ?: return
        val flippedImage = if (needsFlipping(manager.currentAction, manager.layingDir)) flipImg(img) else img
        g?.drawImage(flippedImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null) ?: return
        if (manager.bubbleState != BubbleState.NONE) {
            manager.currBubbleFrames.getOrNull(CatAnimationManager.bubbleFrame)?.let { bubbleImg ->
                val position = calculateBubblePosition(manager.currentAction, manager.layingDir)
                g.drawImage(bubbleImg, position.x, position.y, BUBBLE_SIZE, BUBBLE_SIZE, null)
            }
        }
    }

    @Suppress
    private fun readResolve(): Any = Stage
}
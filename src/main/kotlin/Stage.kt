package me.cdh

import java.awt.Graphics
import java.awt.Point
import java.awt.image.BufferedImage
import javax.swing.JPanel

object Stage : JPanel() {

    private val CatAnimationManager.currentImage: BufferedImage?
        get() = currFrames.getOrNull(frameNum)

    private val CatAnimationManager.currentBubbleImage: BufferedImage?
        get() = currBubbleFrames.getOrNull(bubbleFrame)

    const val BASE_X = 30
    const val BASE_Y = 40

    init {
        isOpaque = false
    }

    private fun Graphics.drawCatImage(img: BufferedImage) =
        drawImage(img, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null)

    private fun Graphics.drawBubbleImage(img: BufferedImage, position: Point) =
        drawImage(img, position.x, position.y, BUBBLE_SIZE, BUBBLE_SIZE, null)

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
        val img = manager.currentImage ?: return
        val flippedImage = if (needsFlipping(manager.currentAction, manager.layingDir)) flipImg(img) else img
        g?.drawCatImage(flippedImage) ?: return
        if (manager.bubbleState != BubbleState.NONE) {
            manager.currentBubbleImage?.let { bubbleImg ->
                val position = calculateBubblePosition(manager.currentAction, manager.layingDir)
                g.drawBubbleImage(bubbleImg, position)
            }
        }
    }

    @Suppress
    private fun readResolve(): Any = Stage
}
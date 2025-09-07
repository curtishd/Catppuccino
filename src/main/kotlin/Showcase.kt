package me.cdh

import java.awt.Graphics
import javax.swing.JPanel

object Showcase : JPanel() {
    @Suppress
    private fun readResolve(): Any = Showcase

    init {
        isOpaque = false
    }

    override fun paintComponent(g: Graphics?) {
        var image = CatAnimationManager.currFrames[CatAnimationManager.frameNum]
        if ((CatAnimationManager.currentAction == Behave.LAYING || CatAnimationManager.currentAction == Behave.RISING || CatAnimationManager.currentAction == Behave.SLEEP)
            && CatAnimationManager.layingDir == Direction.LEFT
            || CatAnimationManager.currentAction == Behave.CURLED
            && CatAnimationManager.layingDir == Direction.RIGHT
        )
            image = flipImg(image)
        g!!.drawImage(image, 0, 0, 100, 100, null)
        if (CatAnimationManager.bubbleState != BubbleState.NONE) {
            val curImg = CatAnimationManager.currBubbleFrames[CatAnimationManager.bubbleFrame]
            var x = 30
            var y = 40
            when (CatAnimationManager.currentAction) {
                Behave.SLEEP, Behave.LAYING, Behave.LEFT, Behave.RIGHT -> x =
                    if (CatAnimationManager.layingDir == Direction.LEFT) 0 else x + 30

                Behave.UP, Behave.LICKING, Behave.SITTING -> y -= 25
                else -> {}
            }
            g.drawImage(curImg, x, y, BUBBLE_SIZE, BUBBLE_SIZE, null)
        }
    }
}
package me.cdh

import me.cdh.enum.Behave
import me.cdh.enum.BubbleState
import me.cdh.enum.Direction
import java.awt.Graphics
import javax.swing.JPanel

object Showcase : JPanel() {
    @Suppress
    private fun readResolve(): Any = Showcase

    init {
        isOpaque = false
    }

    override fun paintComponent(g: Graphics?) {
        var image = Compose.currFrames[Compose.frameNum]
        if ((Compose.action == Behave.LAYING || Compose.action == Behave.RISING || Compose.action == Behave.SLEEP)
            && Compose.layingDir == Direction.LEFT
            || Compose.action == Behave.CURLED
            && Compose.layingDir == Direction.RIGHT
        )
            image = flipImg(image)
        g!!.drawImage(image, 0, 0, 100, 100, null)
        if (Compose.bubbleState != BubbleState.NONE) {
            val curImg = Compose.currBubbleFrames[Compose.bubbleFrameIdx]
            var x = 30
            var y = 40
            when (Compose.action) {
                Behave.SLEEP, Behave.LAYING, Behave.LEFT, Behave.RIGHT -> x =
                    if (Compose.layingDir == Direction.LEFT) 0 else x + 30

                Behave.UP, Behave.LICKING, Behave.SITTING -> y -= 25
                else -> {}
            }
            g.drawImage(curImg, x, y, 30, 30, null)
        }
    }
}
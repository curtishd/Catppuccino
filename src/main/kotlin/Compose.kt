package me.cdh

import me.cdh.enum.Behave
import me.cdh.enum.BubbleState
import me.cdh.enum.Direction
import me.cdh.enum.State
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.enums.EnumEntries
import kotlin.math.abs
import kotlin.random.Random

object Compose {
    val win = JFrame().apply {
        type = Window.Type.UTILITY
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isUndecorated = true
        val dim = Dimension(100, 100)
        preferredSize = dim
        minimumSize = dim
        setLocationRelativeTo(null)
        isAlwaysOnTop = true
        isResizable = false
        addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent?) {
                setLocation(e!!.locationOnScreen.x - width / 2, e.locationOnScreen.y - height / 2)
                if (changeAction(Behave.RISING)) frameNum = 0
            }
        })
        addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent?) {
                if (action == Behave.RISING) {
                    changeAction(Behave.LAYING)
                    frameNum = 0
                }
            }

            override fun mouseClicked(e: MouseEvent?) {
                bubbleState = BubbleState.HEART
                bubbleFrameIdx = 0
            }
        })
        background = Color(1.0f, 1.0f, 1.0f, 0.0f)
        isVisible = true
        add(Showcase)
    }
    private val frames = Loader.loadRes(Behave.entries)
    private val bubbleFrames = Loader.loadRes(BubbleState.entries)
    var frameNum = 0
    var action = Behave.SLEEP
    lateinit var currFrames: List<BufferedImage>
    lateinit var currBubbleFrames: List<BufferedImage>
    var layingDir = Direction.RIGHT
    var state = State.DEFAULT
    var wanderLoc = Point(0, 0)
    var bubbleState = BubbleState.NONE
    var bubbleFrameIdx = 0
    var bubbleSteps = 0
    var animationSteps = 0

    private object Loader {
        fun <T> loadRes(entries: EnumEntries<T>): Map<String, List<BufferedImage>>
                where T : Enum<T>, T : Animate {
            val map = HashMap<String, List<BufferedImage>>()
            val catVarious = when (Random.nextInt(0, 4)) {
                0 -> "calico_cat"
                1 -> "grey_tabby_cat"
                2 -> "orange_cat"
                3 -> "white_cat"
                else -> throw IllegalStateException()
            }
            for (e in entries) {
                if (e.frame <= 0) continue
                val list = mutableListOf<BufferedImage>()
                map[e.name] = list
                for (i in 1..e.frame) {
                    Loader.javaClass.classLoader.getResourceAsStream("$catVarious/${e.name.lowercase()}/${e.name.lowercase()}_$i.png")
                        .use {
                            list.add(ImageIO.read(it))
                        }
                }
            }
            return map
        }
    }

    fun tryWandering() {
        if (Random.nextBoolean()) return
        state = State.WANDER
        val screenLoc = win.locationOnScreen
        var loc: Point
        do {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            loc = Point(
                Random.nextInt(screenSize.width - win.width - 20) + 10,
                Random.nextInt(screenSize.height - win.height - 20) + 10
            )
        } while (abs(screenLoc.y - loc.y) <= 400 && abs(screenLoc.x - loc.x) <= 400)
        wanderLoc = loc
    }

    fun changeAction(behave: Behave): Boolean =
        if (action != behave) {
            action = behave
            currFrames = frames[action.name]!!
            true
        } else {
            false
        }

    fun perform() {
        val loc = win.location
        when (action) {
            Behave.RIGHT -> loc.translate(1, 0)
            Behave.LEFT -> loc.translate(-1, 0)
            Behave.UP -> loc.translate(0, -1)
            Behave.DOWN -> loc.translate(0, 1)
            else -> {}
        }
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        when {
            loc.x > screenSize.width - win.width -> loc.setLocation(screenSize.width - win.width, loc.y)
            loc.x < -10 -> loc.setLocation(-10, loc.y)
            loc.y > screenSize.height - win.height -> loc.setLocation(loc.x, screenSize.height - win.height)
            loc.y < -35 -> loc.setLocation(loc.x, -35)
        }
        win.location = loc
    }

    fun updateAnimation() {
        animationSteps++
        if (animationSteps >= action.delay) {
            if (action == Behave.LAYING && frameNum == action.frame - 1) {
                if ((animationSteps - action.delay) > 40) {
                    animationSteps = 0
                    frameNum = 0
                    if (Random.nextBoolean()) changeAction(Behave.CURLED) else changeAction(Behave.SLEEP)
                }
            } else if (action == Behave.SITTING && frameNum == action.frame - 1) {
                changeAction(Behave.LICKING)
                animationSteps = 0
                frameNum = 0
            } else {
                frameNum++
                animationSteps = 0
            }
        }
        if (frameNum >= action.frame) frameNum = 0
    }

    fun manageBubbleState() {
        if (bubbleState != BubbleState.HEART) {
            if (action == Behave.SLEEP || action == Behave.CURLED) bubbleState = BubbleState.ZZZ
            else if (action != Behave.SITTING) bubbleState = BubbleState.NONE
        }
        bubbleSteps++
        currBubbleFrames = bubbleFrames.getOrDefault(bubbleState.name, bubbleFrames[BubbleState.HEART.name])!!
        if (bubbleSteps >= bubbleState.delay) {
            bubbleFrameIdx++
            bubbleSteps = 0
        }
        if (bubbleFrameIdx >= bubbleState.frame) {
            bubbleFrameIdx = 0
            if (bubbleState == BubbleState.HEART) bubbleState = BubbleState.NONE
        }
    }

    fun updateBehave() {
        if (action != Behave.RISING) {
            if (state == State.WANDER) {
                val curPos = win.locationOnScreen
                if (abs(curPos.x - wanderLoc.x) >= 3) {
                    if (curPos.x > wanderLoc.x)
                        changeAction(Behave.LEFT)
                    else
                        changeAction(Behave.RIGHT)

                } else {
                    if (curPos.y > wanderLoc.y)
                        changeAction(Behave.UP)
                    else
                        changeAction(Behave.DOWN)
                }
                state = if (wanderLoc.distance(curPos) < 3) State.DEFAULT else State.WANDER
            }
            var flag = false
            when {
                action == Behave.LEFT -> layingDir = Direction.LEFT
                action == Behave.RIGHT -> layingDir = Direction.RIGHT
                state != State.WANDER && (action == Behave.UP || action == Behave.DOWN) -> flag =
                    if (Random.nextInt(3) >= 1)
                        changeAction(Behave.LAYING)
                    else
                        changeAction(Behave.SITTING)
            }
            if (flag) frameNum = 0
        }
    }
}
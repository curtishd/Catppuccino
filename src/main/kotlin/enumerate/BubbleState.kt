package me.cdh.enumerate

import me.cdh.Animate

enum class BubbleState(
    override val frame: Int,
    override val delay: Int
) : Animate {
    DIZZY(4, 30),
    ZZZ(4, 30),
    HEART(4, 50),
    NONE(-1, -1);
}
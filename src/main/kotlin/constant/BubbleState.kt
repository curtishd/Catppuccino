package me.cdh.constant

import me.cdh.Animate

enum class BubbleState(
    override val frame: Int,
    override val delay: Int
) : Animate {
    ZZZ(4, 30),
    HEART(4, 50),
    NONE(-1, -1);
}
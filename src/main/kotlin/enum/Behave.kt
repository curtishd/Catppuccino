package me.cdh.enum

import me.cdh.Animate

enum class Behave(
    override val frame: Int,
    override val delay: Int
) : Animate {
    UP(4, 10),
    DOWN(4, 10),
    LEFT(4, 10),
    RIGHT(4, 10),
    CURLED(2, 40),
    LAYING(4, 20),
    SITTING(4, 20),

    LICKING(4, 40),
    RISING(2, 40),
    SLEEP(1, 10);
}
package me.cdh

interface Animate {
    val delay:Int
    val frame: Int
}

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
    SLEEP(1, 10)
}

enum class BubbleState(
    override val frame: Int,
    override val delay: Int
) : Animate {
    ZZZ(4, 30),
    HEART(4, 50),
    NONE(-1, -1);
}

enum class State { DEFAULT, WANDER }

enum class Direction { RIGHT, LEFT }


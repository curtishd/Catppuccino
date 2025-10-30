package me.cdh.conf

import me.cdh.Animate
import java.awt.image.BufferedImage
import java.util.EnumSet
import javax.imageio.ImageIO
import kotlin.random.Random
import kotlin.reflect.KClass

private object ResourceLoader {
    private var catType = listOf(
        "calico_cat",
        "grey_tabby_cat",
        "orange_cat",
        "white_cat",
    )
    private val selectedCatType =
        catType[Random.nextInt(0, catType.size)]

    fun loadFrames(actionName: String, frameCount: Int): List<BufferedImage> = (1..frameCount).mapNotNull { frameNum ->
        javaClass.classLoader.getResourceAsStream(
            "$selectedCatType/${actionName.lowercase()}/${actionName.lowercase()}_$frameNum.png"
        )?.use { ImageIO.read(it) }
    }
}

fun <T> loadAllRes(clazz: KClass<T>): Map<String, List<BufferedImage>>
        where T : Animate, T : Enum<T> = EnumSet.allOf(clazz.java).associate { entry -> entry.name to ResourceLoader.loadFrames(entry.name, entry.frame) }
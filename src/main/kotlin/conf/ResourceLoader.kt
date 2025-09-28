package me.cdh.conf

import me.cdh.Animate
import me.cdh.Random
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object ResourceLoader {
    var catType = listOf(
        "calico_cat",
        "grey_tabby_cat",
        "orange_cat",
        "white_cat",
    )
    private val selectedCatType = catType[Random.nextInt(0, catType.size)]

    fun loadFrames(actionName: String, frameCount: Int): List<BufferedImage> = (1..frameCount).mapNotNull { frameNum ->
        javaClass.classLoader.getResourceAsStream(
            "$selectedCatType/${actionName.lowercase()}/${actionName.lowercase()}_$frameNum.png"
        )?.use { ImageIO.read(it) }
    }

    inline fun <reified T> loadAllFrames(): Map<String, List<BufferedImage>> where T : Enum<T>, T : Animate =
        enumValues<T>().associate { entry -> entry.name to loadFrames(entry.name, entry.frame) }
}
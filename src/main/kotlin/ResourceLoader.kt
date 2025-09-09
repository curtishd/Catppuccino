package me.cdh

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object ResourceLoader {
    private val cachedResources = mutableMapOf<String, List<BufferedImage>>()
    private val catType = listOf("calico_cat", "grey_tabby_cat", "orange_cat", "white_cat")
    private val selectedCatType = catType.random()

    fun loadFrame(actionName: String, frameCount: Int): List<BufferedImage> = (1..frameCount).mapNotNull { frameNum ->
        javaClass.classLoader.getResourceAsStream(
            "$selectedCatType/${actionName.lowercase()}/${actionName.lowercase()}_$frameNum.png"
        )?.use { ImageIO.read(it) }
    }

    fun <T> loadFrames(entries: T): List<BufferedImage> where T : Enum<T>, T : Animate =
        cachedResources.getOrPut(entries.name) {
            if (entries.frame <= 0) return emptyList()
            loadFrame(entries.name, entries.frame)
        }

    inline fun <reified T> loadAllFrames(): Map<String, List<BufferedImage>> where T : Enum<T>, T : Animate =
        enumValues<T>().associate { entry -> entry.name to loadFrames(entry) }
}
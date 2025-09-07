package me.cdh

import java.awt.image.BufferedImage
import java.util.WeakHashMap
import javax.imageio.ImageIO
import kotlin.collections.set
import kotlin.enums.EnumEntries
import kotlin.random.Random

object ResourceLoader {
    fun <T> loadRes(entries: EnumEntries<T>): Map<String, List<BufferedImage>> where T : Enum<T>, T : Animate {
        val map = WeakHashMap<String, List<BufferedImage>>()
        val catVarious = when (Random.nextInt(0, 4)) {
            0 -> "calico_cat"
            1 -> "grey_tabby_cat"
            2 -> "orange_cat"
            3 -> "white_cat"
            else -> throw IllegalStateException()
        }
        for (entry in entries) {
            if (entry.frame <= 0) continue
            val list = mutableListOf<BufferedImage>()
            map[entry.name] = list
            for (i in 1..entry.frame) {
                ResourceLoader.javaClass.classLoader.getResourceAsStream("$catVarious/${entry.name.lowercase()}/${entry.name.lowercase()}_$i.png")
                    .use {
                        list.add(ImageIO.read(it))
                    }
            }
        }
        return map
    }
}
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow

/**
 * @author Anton Kurinnoy
 */
fun preprocessImage(image: BufferedImage): Array<Array<FloatArray>> {
    val width = image.width
    val height = image.height
    val rgbArray = Array(height) { _ ->
        Array(width) { _ ->
            floatArrayOf(0.0f, 0.0f, 0.0f)
        }
    }
    for (y in 0..<height) {
        for (x in 0..<width) {

            val pixel = image.getRGB(x, y)
            val color = Color(pixel)

            val red = color.red / 255.0
            val green = color.green / 255.0
            val blue = color.blue / 255.0

            val red1 = red - 0.5
            val green1 = green - 0.5
            val blue1 = blue - 0.5

            val red2 = red1 / 0.5
            val green2 = green1 / 0.5
            val blue2 = blue1 / 0.5

            rgbArray[y][x] = floatArrayOf(red2.toFloat(), green2.toFloat(), blue2.toFloat())
        }
    }
    return rgbArray
}


fun Double.round(decimals: Int): Double {
    val multiplier = 10.0.pow(decimals.toDouble())
    return kotlin.math.round(this * multiplier) / multiplier
}

fun calculateMean(array: FloatArray): Float {
    var sum = 0.0f
    var count = 0

    for (element in array) {
        sum += element
        count++
    }

    return if (count > 0) sum / count else 0.0f
}

fun resizeImage(image: BufferedImage, newWidth: Int, newHeight: Int): BufferedImage {
    val resizedImage = BufferedImage(newWidth, newHeight, image.type)
    val g = resizedImage.createGraphics()
    g.drawImage(image, 0, 0, newWidth, newHeight, null)
    g.dispose()

    return resizedImage
}

fun makeFloatArrayImage(image: Array<Array<FloatArray>>): FloatArray {
    val width = image[0].size
    val height = image.size
    val floatArray = FloatArray(3 * width * height)
    for (y in 0..<height) {
        for (x in 0..<width) {
            val red = image[y][x][0]
            val green = image[y][x][1]
            val blue = image[y][x][2]

            floatArray[y * width + x] = red
            floatArray[width * height + y * width + x] = green
            floatArray[2 * width * height + y * width + x] = blue
        }
    }
    return floatArray
}
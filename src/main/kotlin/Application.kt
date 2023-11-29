import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import java.nio.FloatBuffer
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * @author Anton Kurinnoy
 */
fun main() {

    val imagePath = Paths.get("image.file")

    val modelPath = "deePix.onnx"
    val env = OrtEnvironment.getEnvironment()
    val session = env.createSession(modelPath)


    val imageStart = ImageIO.read(imagePath.toFile())
    val resizedImage = resizeImage(imageStart, 224, 224)
    val preprocessImage = preprocessImage(resizedImage)
    val floatArrImage = makeFloatArrayImage(preprocessImage)


    val inputTensor = OnnxTensor.createTensor(
        env,
        FloatBuffer.wrap(floatArrImage),
        longArrayOf(1, 3, resizedImage.height.toLong(), resizedImage.width.toLong())
    )
    val inputMap = mapOf(session.inputNames.first() to inputTensor)
    val outputs = session.run(inputMap)

//    println(session.outputNames)
//    outputs.forEachIndexed { index, mutableEntry ->
//        println(mutableEntry)
//    }

    val mask = (outputs[0] as OnnxTensor).floatBuffer.array()
    val binary = (outputs[1] as OnnxTensor).floatBuffer.array()

    mask.forEachIndexed { index, value ->
        print("${value.toDouble().round(5)} ")
        if ((index + 1) % 14 == 0) println()
    }

    println("mean ${calculateMean(mask)}")
    binary.forEach { println("binary $it") }
}

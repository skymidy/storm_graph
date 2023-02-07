package com.example.stormpr.util

import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.image.Image
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import java.io.ByteArrayInputStream

fun localToScenePoint(node: Node): Point2D{
    return node.localToScene(node.translateX, node.translateY)
}
fun matToImage(mat: Mat): Image{
    val byteMat = MatOfByte()
    Imgcodecs.imencode(".bmp", mat, byteMat)
    return Image(ByteArrayInputStream(byteMat.toArray()))
}
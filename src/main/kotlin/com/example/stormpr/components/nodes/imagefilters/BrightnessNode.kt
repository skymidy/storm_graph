package com.example.stormpr.components.nodes.imagefilters

import com.example.stormpr.components.props.DoubleProp
import com.example.stormpr.util.saturate
import javafx.geometry.Point2D
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class BrightnessNode(position: Point2D) :
    FilterNode(position, "Brightness Filter") {
    private val alphaProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 1.0, name = "alpha:")
    private val betaProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 1.0, name = "beta:")

    init {
        addProps(alphaProp, betaProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {
        val newImage = Mat()
        mat.copyTo(newImage)

        val imageData = ByteArray(((mat.total() * mat.channels()).toInt()))
        mat.get(0, 0, imageData)
        val newImageData = ByteArray((newImage.total() * newImage.channels()).toInt())
        for (y in 0 until mat.rows()) {
            for (x in 0 until mat.cols()) {
                for (c in 0 until mat.channels()) {
                    var pixelValue = imageData[(y * mat.cols() + x) * mat.channels() + c].toDouble()
                    pixelValue = if (pixelValue < 0) pixelValue + 256 else pixelValue
                    newImageData[(y * mat.cols() + x) * mat.channels() + c] =
                        saturate(alphaProp.getValue() * pixelValue + betaProp.getValue())
                }
            }
        }
        newImage.put(0, 0, newImageData)
        return newImage
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        alphaProp.getValueProperty().addListener { _ ->
            update()
        }
        betaProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
package com.example.stormpr.components.nodes.imagefilters

import com.example.stormpr.components.props.DoubleProp
import com.example.stormpr.components.props.IntegerProp
import com.example.stormpr.util.saturate
import javafx.geometry.Point2D
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class GaussNode(position: Point2D) :
    FilterNode(position, "Gauss Filter") {
    private val kernelSizeProp: IntegerProp = IntegerProp(hasIn = true, hasOut = true, baseValue = 1, name = "kernel size:")

    init {
        addProps(kernelSizeProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {

        val kernelSize = kernelSizeProp.getValue() * 2 + 1
        if (kernelSize <= 0 || kernelSize > 100)
            return mat


        val newImage = Mat()
        mat.copyTo(newImage)

        Imgproc.GaussianBlur(mat, newImage, Size(kernelSize.toDouble(), kernelSize.toDouble()), 0.0)
        return newImage
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        kernelSizeProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
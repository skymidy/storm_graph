package com.example.stormpr.components.nodes.imagefilters

import javafx.geometry.Point2D
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class GreyNode(position: Point2D) :
    FilterNode(position, "Grey Filter") {

    init {
        initPropsListeners()
    }
    override fun filter(mat: Mat): Mat {
        val mat2 = Mat()
        mat.copyTo(mat2)
        Imgproc.cvtColor(mat, mat2, Imgproc.COLOR_BGR2GRAY)

        val mat3 = Mat()

        Core.merge(List(3) { mat2 }, mat3)
        return mat3
    }


}
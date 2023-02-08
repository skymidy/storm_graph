package com.example.stormpr.components.nodes.imagefilters

import javafx.geometry.Point2D
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

class SepiaNode(position: Point2D) :
    FilterNode(position, "Sepia Filter") {
    init {
        initPropsListeners()
    }
    override fun filter(mat: Mat): Mat {
        val colMat = Mat(3, 3, CvType.CV_64FC1)
        val row = 0
        val col = 0
        colMat.put(
            row, col, 0.272, 0.534, 0.131, 0.349, 0.686, 0.168, 0.393, 0.769, 0.189
        )
        val mat2 = Mat()
        mat.copyTo(mat2)

        Core.transform(mat, mat2, colMat)
        return mat2
    }


}
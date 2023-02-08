package com.example.stormpr.components.nodes.imagefilters.transform

import com.example.stormpr.components.nodes.imagefilters.FilterNode
import com.example.stormpr.components.props.DoubleProp
import javafx.geometry.Point2D
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class MoveNode(position: Point2D) :
    FilterNode(position, "Scale") {
    private val translateXProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "x (%):")
    private val translateYProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "y (%):")

    init {
        addProps(translateXProp,translateYProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {

        val x = translateXProp.getValue()
        val y = translateYProp.getValue()

        val mat2 = Mat()
        mat.copyTo(mat2)

        val moveMat = Mat(2, 3, CvType.CV_64FC1)
        val row = 0
        val col = 0
        moveMat.put(
            row, col, 1.0, 0.0, (mat.cols() * x / 100.0), 0.0, 1.0, (mat.rows() * y / 100.0)
        )

        Imgproc.warpAffine(mat, mat2, moveMat, Size(mat.cols().toDouble(), mat.rows().toDouble()))

        return mat2
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        translateXProp.getValueProperty().addListener { _ ->
            update()
        }
        translateYProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
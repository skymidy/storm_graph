package com.example.stormpr.components.nodes.imagefilters.transform

import com.example.stormpr.components.nodes.imagefilters.FilterNode
import com.example.stormpr.components.props.DoubleProp
import javafx.geometry.Point2D
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class ScaleNode(position: Point2D) :
    FilterNode(position, "Scale") {
    private val widthScaleProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "x scale:")
    private val heightScaleProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "y scale:")

    init {
        addProps(widthScaleProp,heightScaleProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {

        val x = mat.cols() * widthScaleProp.getValue() / 100
        val y = mat.rows() * heightScaleProp.getValue() / 100

//        if (x <= 0 || y <= 0)
//            return mat

        val mat2 = Mat()
        mat.copyTo(mat2)
        Imgproc.resize(mat, mat2, Size(x, y))

        return mat2
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        widthScaleProp.getValueProperty().addListener { _ ->
            update()
        }
        heightScaleProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
package com.example.stormpr.components.nodes.imagefilters.transform

import com.example.stormpr.components.nodes.imagefilters.FilterNode
import com.example.stormpr.components.props.DoubleProp
import javafx.geometry.Point2D
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class RotateNode(position: Point2D) :
    FilterNode(position, "Rotate") {
    private val angleProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "angle:")

    init {
        addProps(angleProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {

        val newMat = Mat()
        mat.copyTo(newMat)

        val rotor = Imgproc.getRotationMatrix2D(Point(mat.cols() / 2.0, mat.rows() / 2.0), angleProp.getValue(), 1.0)

        Imgproc.warpAffine(mat, newMat, rotor, Size(mat.cols().toDouble(), mat.rows().toDouble()))
        return newMat
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        angleProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
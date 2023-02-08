package com.example.stormpr.components.nodes.imagefilters

import com.example.stormpr.components.props.DoubleProp
import com.example.stormpr.components.props.StringProp
import javafx.geometry.Point2D
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class AddTextNode(position: Point2D) :
    FilterNode(position, "Add Text") {
    private val translateXProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "x (%):")
    private val translateYProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 0.0, name = "y (%):")
    private val textProp: StringProp = StringProp(hasIn = true, hasOut = true, baseValue = "input text", name = "text:")
    private val fontSizeProp: DoubleProp = DoubleProp(hasIn = true, hasOut = true, baseValue = 12.0, name = "font size:")

    init {
        addProps(translateXProp,translateYProp,textProp,fontSizeProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {

        val x = translateXProp.getValue()
        val y = translateYProp.getValue()
        val text = textProp.getValue()
        val size = fontSizeProp.getValue()

        val mat2 = Mat()
        mat.copyTo(mat2)

        Imgproc.putText(
            mat2,
            text,
            Point(mat.cols() * x / 100.0, mat.rows() * y / 100.0),
            0,
            size,
            Scalar(255.0, 255.0, 255.0),
            5
        )


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
        textProp.getValueProperty().addListener { _ ->
            update()
        }
        fontSizeProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
package com.example.stormpr.components.nodes.imagefilters

import com.example.stormpr.components.props.ImageProp
import com.example.stormpr.components.props.IntegerProp
import javafx.geometry.Point2D
import org.opencv.core.Mat

class MergeNode(position: Point2D) :
    FilterNode(position, "Merge", "a:") {
    private val secondImageProp: ImageProp = ImageProp(hasIn = true, hasOut = true, "b:")
    private val secondImageXProp: IntegerProp = IntegerProp(hasIn = true, hasOut = true, baseValue = 0, name = "bx:")
    private val secondImageYProp: IntegerProp = IntegerProp(hasIn = true, hasOut = true, baseValue = 0, name = "by:")

    init {
        addProps(secondImageProp,secondImageXProp, secondImageYProp)
        initPropsListeners()
    }

    override fun filter(mat: Mat): Mat {
        val newMat = Mat()
        mat.copyTo(newMat)

        val mat2 = secondImageProp.getValue()
        val bx = secondImageXProp.getValue()
        val by = secondImageYProp.getValue()

        val imageData = ByteArray(((mat.total() * mat.channels()).toInt()))
        mat.get(0, 0, imageData)
        val imageData2 = ByteArray(((mat.total() * mat.channels()).toInt()))
        mat2.get(0, 0, imageData2)

        for (y in 0 until mat2.rows()) {
            for (x in 0 until mat2.cols()) {
                for (c in 0 until mat2.channels()) {
                    if (0 <= by + y && by + y < mat.rows() && 0 <= bx + x && bx + x < mat.cols()) {
                        imageData[((by + y) * mat.cols() + bx + x) * mat.channels() + c] =
                            imageData2[(y * mat2.cols() + x) * mat2.channels() + c]
                    }
                }
            }
        }
        newMat.put(0, 0, imageData)
        return newMat
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        secondImageProp.getValueProperty().addListener { _ ->
            update()
        }
        secondImageXProp.getValueProperty().addListener { _ ->
            update()
        }
        secondImageYProp.getValueProperty().addListener { _ ->
            update()
        }
    }


}
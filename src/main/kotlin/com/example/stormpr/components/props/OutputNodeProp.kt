package com.example.stormpr.components.props

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import org.opencv.core.Mat


class OutputNodeProp() : BaseProp<Mat>(hasIn = true, hasOut = false, ) {
    override val type: types = types.IMAGE
    override var value: Property<Mat> = SimpleObjectProperty(Mat.eye(220,100, 0xFFFF0000.toInt()))

    override fun abortIn() {
        super.abortIn()
        value.value = Mat.eye(220,100, 0xFFFF0000.toInt())
    }
}
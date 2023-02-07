package com.example.stormpr.components

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import org.opencv.core.Mat


class OutputNodeProp(parent: BaseGraphNode) : BaseProp<Mat>(hasIn = true, hasOut = false, parent) {
    override val type: types = types.IMAGE
    override var value: Property<Mat> = SimpleObjectProperty()
}
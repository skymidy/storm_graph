package com.example.stormpr.components.nodes

import com.example.stormpr.components.props.DoubleProp
import javafx.geometry.Point2D

class DoubleNode(position: Point2D) : BaseGraphNode("Double", position) {
    private var mainProp: DoubleProp = DoubleProp(hasIn = false, hasOut = true)
    init {
        addProp(mainProp)
    }
}
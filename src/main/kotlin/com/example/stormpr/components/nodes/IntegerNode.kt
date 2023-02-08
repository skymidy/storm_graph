package com.example.stormpr.components.nodes;

import com.example.stormpr.components.props.IntegerProp
import javafx.geometry.Point2D

class IntegerNode(position: Point2D) : BaseGraphNode("Integer", position) {
    private var mainProp: IntegerProp = IntegerProp(hasIn = true, hasOut = true)

    init {
        addProp(mainProp)
    }
}
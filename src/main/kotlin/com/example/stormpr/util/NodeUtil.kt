package com.example.stormpr.util

import javafx.geometry.Point2D
import javafx.scene.Node

fun localToScenePoint(node: Node): Point2D{
    return node.localToScene(node.translateX, node.translateY)
}
package com.example.stormpr.components

import javafx.geometry.Insets
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class BaseGraphNode(nodeName: String) : VBox() {

    val baseWidth: Double = 230.0
    protected val nodeHeader: VBox
    private val nodeLabel: Label
    protected val nodeBody: VBox

    init {
        //root setup
        minWidth = baseWidth
        prefWidth = baseWidth
        maxWidth = baseWidth

        minHeight = USE_COMPUTED_SIZE
        prefHeight = USE_COMPUTED_SIZE
        maxHeight = Double.MAX_VALUE

        styleClass.setAll("node-graph")

        //header setup
        nodeLabel = Label()
        nodeLabel.font = Font.font("System", FontWeight.NORMAL, 14.0)
        nodeLabel.textFill =  Paint.valueOf("#FFF")
        nodeLabel.text = nodeName

        nodeHeader = VBox()
        nodeHeader.padding = Insets(5.0, 5.0, 5.0, 5.0)
        nodeHeader.children.add(nodeLabel)
        nodeHeader.styleClass.setAll("node-graph-header")

        //body setup
        nodeBody = VBox()
        nodeBody.spacing = 5.0
        nodeBody.padding = Insets(6.0, .0, 6.0, 6.0)

        //temp porps setup
        val checkBox = CheckBox("Check box")
        val hBox = HBox()
        hBox.children.add(checkBox)

        val checkBox_ = CheckBox("Check box")
        val hBox_ = HBox()
        hBox_.children.add(checkBox_)

        nodeBody.children.addAll(hBox, hBox_)


        //setup draggable
        draggableFiltersSetUp()

        children.addAll(nodeHeader, nodeBody)
    }

    fun draggableFiltersSetUp() {

        val dragContext = DragContext()

        nodeHeader.addEventFilter(MouseEvent.ANY) { mouseEvent ->
            mouseEvent.consume()
        }
        nodeHeader.addEventFilter(MouseEvent.MOUSE_PRESSED) { mouseEvent ->
            // remember initial mouse cursor coordinates
            // and node position
            dragContext.mouseAnchorX = mouseEvent.sceneX
            dragContext.mouseAnchorY = mouseEvent.sceneY
            dragContext.initialTranslateX = translateX
            dragContext.initialTranslateY = translateY
        }
        nodeHeader.addEventFilter(MouseEvent.MOUSE_DRAGGED) { mouseEvent ->
            // shift node from its initial position by delta
            // calculated from mouse cursor movement
            translateX = (dragContext.initialTranslateX + mouseEvent.sceneX - dragContext.mouseAnchorX)
            translateY = (dragContext.initialTranslateY + mouseEvent.sceneY - dragContext.mouseAnchorY)
        }
    }

    private class DragContext {
        var mouseAnchorX = 0.0
        var mouseAnchorY = 0.0
        var initialTranslateX = 0.0
        var initialTranslateY = 0.0
    }
}
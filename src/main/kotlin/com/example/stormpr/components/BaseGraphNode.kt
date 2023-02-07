package com.example.stormpr.components

import com.example.stormpr.util.DragContext
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
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
        nodeLabel.textFill = Paint.valueOf("#FFF")
        nodeLabel.text = nodeName

        nodeHeader = VBox()
        nodeHeader.padding = Insets(5.0, 5.0, 5.0, 5.0)
        nodeHeader.children.add(nodeLabel)
        nodeHeader.styleClass.setAll("node-graph-header")

        //body setup
        nodeBody = VBox()
        nodeBody.spacing = 5.0
        nodeBody.padding = Insets(6.0, .0, 6.0, .0)

        //temp porps setup
        val prop1 = BaseProp<Number,Number>(true,true,this) {
            it
        }
        val prop2 = BaseProp<Number,Number>(true,false,this) {
            it
        }
        val prop3 = BaseProp<Number,Number>(false,true,this) {
            it
        }
        val prop4 = BaseProp<Number,Number>(false,false,this) {
            it
        }

        nodeBody.children.addAll(prop1,prop2,prop3,prop4)


        //setup draggable
        draggableFiltersSetUp()

        children.addAll(nodeHeader, nodeBody)
    }

    private fun onNodeTranslate(){
        nodeBody.children.forEach { node ->
            (node as BaseProp<*,*>).updateLines()
        }
    }

    private fun draggableFiltersSetUp() {

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
            val newTranslateX = (dragContext.initialTranslateX + mouseEvent.sceneX - dragContext.mouseAnchorX)
            val newTranslateY = (dragContext.initialTranslateY + mouseEvent.sceneY - dragContext.mouseAnchorY)
            if (newTranslateX >= 0) {
                translateX = newTranslateX
            }
            if (newTranslateY >= 0) {
                translateY = newTranslateY
            }
            onNodeTranslate()
        }
    }
}
package com.example.stormpr.components

import com.example.stormpr.util.DragContext
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
open class BaseGraphNode(nodeName: String, position: Point2D = Point2D(.0, .0)) : VBox() {

    private val baseWidth: Double = 230.0
    protected val nodeHeader: VBox
    protected val nodeLabel: Label
    private val nodeBody: VBox
    private var props: List<BaseProp<*>> = listOf()

    init {
        //root setup
        minWidth = baseWidth
        prefWidth = baseWidth
        maxWidth = baseWidth

        minHeight = USE_COMPUTED_SIZE
        prefHeight = USE_COMPUTED_SIZE
        maxHeight = Double.MAX_VALUE

        translateX = position.x
        translateY = position.y

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

        draggableFiltersSetUp()

        children.addAll(nodeHeader, nodeBody)
    }
    //handler function for calculation propose
    open fun update(){  }
    open fun initPropsListeners(){

    }
    open fun unsubPropsListeners(){

    }
    protected fun addProp(prop: BaseProp<*>) {
        props = props + prop
        nodeBody.children.add(prop)
    }
    protected fun addProps(vararg propsList: BaseProp<*>){
        props = props + propsList
        nodeBody.children.addAll(propsList)
    }
    protected fun removeProp(prop: BaseProp<*>) {
        props = props.filter { it != prop }
        nodeBody.children.remove(prop)
    }
    protected fun removeProps(vararg propsList: BaseProp<*>){
        props = props.filter { it !in propsList }
        nodeBody.children.removeAll(propsList.toSet())
    }
    protected fun getPropsList(): List<BaseProp<*>>{
        return props
    }

    //updating lines coordinates
    private fun onNodeTranslate() {
        props.forEach { it.updateLines() }
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
package com.example.stormpr.components.nodes

import com.example.stormpr.components.props.BaseProp
import com.example.stormpr.util.DragContext
import javafx.scene.layout.AnchorPane

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
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
        nodeLabel.prefWidth = 200.0

        val deleteButton = Button("x")
        deleteButton.prefWidth = 20.0
        deleteButton.prefHeight = 20.0
        deleteButton.styleClass += "node-button"
        deleteButton.textFill = Color.WHITE
        deleteButton.onMouseClicked = EventHandler { it.consume(); removeSelf() }
        val hBox = HBox()
        hBox.alignment = Pos.CENTER_RIGHT
        hBox.children.addAll(nodeLabel,deleteButton)


        nodeHeader = VBox()
        nodeHeader.padding = Insets(5.0, 5.0, 5.0, 5.0)
        nodeHeader.children.add(hBox)
        nodeHeader.styleClass.setAll("node-graph-header")

        //body setup
        nodeBody = VBox()
        nodeBody.spacing = 5.0
        nodeBody.padding = Insets(6.0, .0, 6.0, .0)

        draggableFiltersSetUp()

        children.addAll(nodeHeader, nodeBody)
    }

    //handler function for calculation propose
    open fun update() {}
    open fun initPropsListeners() {

    }

    open fun unsubPropsListeners() {

    }

    open fun removeSelf() {
        unsubPropsListeners()
        removeProps()
        (parent as AnchorPane).children.remove(this)
    }

    protected fun addProp(prop: BaseProp<*>) {
        props = props + prop
        nodeBody.children.add(prop)
    }

    protected fun addProps(vararg propsList: BaseProp<*>) {
        props = props + propsList
        nodeBody.children.addAll(propsList)
    }

    protected fun removeProp(prop: BaseProp<*>) {
        props = props.filter { it != prop }
        prop.abortAll()
        nodeBody.children.remove(prop)
    }

    protected fun removeProps(vararg propsList: BaseProp<*>) {

        props = props.filter { it !in propsList }

        if (propsList.isEmpty()) props.forEach { it.abortAll() }
        else propsList.forEach { it.abortAll() }

        nodeBody.children.removeAll(propsList.toSet())
    }

    protected fun getPropsList(): List<BaseProp<*>> {
        return props
    }

    //updating lines coordinates
    private fun onNodeTranslate() {
        props.forEach { it.updateLines() }
    }

    private fun draggableFiltersSetUp() {

        val dragContext = DragContext()

        onContextMenuRequested = EventHandler { it.consume() }

        nodeHeader.addEventFilter(MouseEvent.ANY) { mouseEvent ->
            if(mouseEvent.eventType == MouseEvent.MOUSE_CLICKED) return@addEventFilter
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
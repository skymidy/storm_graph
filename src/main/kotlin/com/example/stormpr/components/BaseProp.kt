package com.example.stormpr.components

import com.example.stormpr.util.localToScenePoint
import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseDragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Polygon


abstract class BaseProp<TYPE>(
    val hasIn: Boolean, val hasOut: Boolean, parentNode: BaseGraphNode) : HBox() {

    protected abstract val type: types

    protected abstract val value: Property<TYPE>

    private var parent: BaseGraphNode = parentNode

    private var inputLine: PropLine? = null
    private var outputLines: MutableList<PropLine> = mutableListOf()

    protected var inDot: Circle = Circle()
    protected var outDot: Polygon = Polygon()

    protected val body = HBox()

    init {
        //root setup
        minHeight = 20.0
        prefHeight = 20.0
        maxHeight = 20.0

        minWidth = USE_COMPUTED_SIZE
        prefWidth = USE_COMPUTED_SIZE
        maxWidth = USE_COMPUTED_SIZE

        alignment = Pos.CENTER_LEFT
        styleClass.setAll("node-prop")

        //dots setup
        inDot.radius = 6.0
        inDot.fill = Paint.valueOf("blue")
        inDot.stroke = Color.BLACK
        inDot.strokeWidth = 2.0
        inDot.translateX -= 8.0
        inDot.id = "in_dot_prop"
        if (!hasIn) inDot.isVisible = false
        else initInput()

        outDot.points.addAll(
            *arrayOf(
                0.0, -7.0,
                14.0, 0.0,
                0.0, 7.0
            )
        )
        outDot.fill = Paint.valueOf("blue")
        outDot.stroke = Color.BLACK
        outDot.strokeWidth = 2.0
        outDot.translateX += 13.0
        outDot.id = "out_dot_prop"
        if (!hasOut) outDot.isVisible = false
        else initOutput()

        //body setup
        body.minWidth = USE_COMPUTED_SIZE
        body.prefWidth = 400.0
        body.maxWidth = USE_COMPUTED_SIZE

        body.minHeight = USE_COMPUTED_SIZE
        body.prefHeight = 20.0
        body.maxHeight = USE_COMPUTED_SIZE

        body.alignment = Pos.CENTER_LEFT

        children.addAll(inDot, body, outDot)
    }

    fun getValue(): TYPE{
        return value.value
    }
    fun getValueProperty(): Property<TYPE>{
        return value
    }

    fun updateLines() {
        inputLine?.updateStartPoint()
        inputLine?.updateEndPoint()
        outputLines.forEach {
            it.updateStartPoint()
            it.updateEndPoint()
        }
    }

    private fun initIO(node: Node) {
        node.addEventFilter(MouseEvent.ANY) { mouseEvent ->

            //Preventing all events on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter

            mouseEvent.consume()
        }
        node.addEventFilter(MouseEvent.MOUSE_PRESSED) { mouseEvent ->
            mouseEvent.consume()
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter

            //setup of companion
            grabbedProp = this
            activeDot = node
            //creating line adding to group on canvas
            activeLine = PropLine()

            //adding line on scene
            (scene.lookup("#lines_group") as Group).children.add(activeLine)

            //line dots init
            activeLine?.lateInit(node, mouseEvent)

            //enabling drag detection
            mouseEvent.isDragDetect = true
        }

        node.addEventFilter(MouseEvent.MOUSE_CLICKED) { mouseEvent ->
            //Removing connection on the middle mouse button
            if (mouseEvent.button != MouseButton.MIDDLE) return@addEventFilter

            if (node == inDot) inputLine?.abortConnection()
            if (node == outDot && outputLines.isNotEmpty()) {
                var lenght = outputLines.count()
                while (lenght > 0){
                    outputLines[--lenght].abortConnection()
                }
            }

            mouseEvent.consume()
        }
        node.addEventFilter(MouseEvent.MOUSE_DRAGGED) { mouseEvent ->
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter
            mouseEvent.consume()
            activeLine?.translateLineEnd(mouseEvent)
        }
        node.addEventFilter(MouseEvent.DRAG_DETECTED) { mouseEvent ->
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter

            //starting press-drag-release event
            node.startFullDrag()

            mouseEvent.consume()
        }
        node.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET) { mouseEvent ->
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter

            if(activeDot?.id == node.id || grabbedProp?.type != type) return@addEventFilter

            //Dragged line over target
            targetProp = this
            targetPropParent = parent

            mouseEvent.consume()
        }
        node.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED_TARGET) { mouseEvent ->
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter

            //Dragged line escaped target
            targetProp = null
            targetPropParent = null

            mouseEvent.consume()
        }
    }

    private fun initInput() {

        initIO(inDot)

        inDot.addEventFilter(MouseEvent.MOUSE_RELEASED) { mouseEvent ->
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter


            //abort connection if line was released midair or targeting itself
            if (targetProp == null || targetPropParent == parent || !(targetProp?.hasOut)!!) {
                activeLine?.removeSelf()
                activeLine = null
                return@addEventFilter
            }

            //removing existing in connection if it exists
            if (inputLine != null) inputLine?.abortConnection()

            //establishing line connection
            activeLine?.outProp = this
            activeLine?.inProp = targetProp!!
            activeLine?.updateStartPoint()
            activeLine?.updateEndPoint()
            activeLine?.initEventHandler()
            value.bind(targetProp?.value as ObservableValue<out Nothing>)
            inputLine = activeLine
            targetProp?.outputLines?.add(activeLine!!)

            //clean up companion
            clearCompanion()

            mouseEvent.consume()
        }
    }

    private fun initOutput() {

        initIO(outDot)

        outDot.addEventFilter(MouseEvent.MOUSE_RELEASED) { mouseEvent ->
            //Preventing all events that not on left mouse button
            if (mouseEvent.button != MouseButton.PRIMARY) return@addEventFilter


            //abort connection if line was released midair or targeting itself
            if (targetProp == null
                || targetPropParent == parent
                || !(targetProp?.hasIn)!!
//                || ( targetProp!!.value.value?.javaClass != (value.value as Any).javaClass )
            ) {
                activeLine?.removeSelf()
                activeLine = null
                return@addEventFilter
            }

            //removing existing in connection if it exists
            if (targetProp?.inputLine != null) targetProp?.inputLine?.abortConnection()

            //establishing line connection
            activeLine?.outProp = targetProp!!
            activeLine?.inProp = this
            activeLine?.updateEndPoint()
            activeLine?.initEventHandler()
            targetProp?.value?.bind(value as ObservableValue<out Nothing>)
            targetProp?.inputLine = activeLine
            outputLines.add(activeLine!!)

            //clean up companion
            clearCompanion()

            mouseEvent.consume()
        }
    }

    private fun abortOut(line: PropLine) {
        outputLines.remove(line)
    }

    private fun abortIn() {
        inputLine = null
        println( value.isBound)
        value.unbind()
    }

    private class PropLine() : Line() {
        lateinit var outProp: BaseProp<*>
        lateinit var inProp: BaseProp<*>

        init {
            stroke = Color.DODGERBLUE
            strokeWidth = 2.0

            isMouseTransparent = true
        }

        fun setStart(point: Point2D) {
            startX = point.x
            startY = point.y
        }

        fun setEnd(point: Point2D) {
            endX = point.x
            endY = point.y
        }

        fun lateInit(startDot: Node, event: MouseEvent) {
            setStart(sceneToLocal(localToScenePoint(startDot)))
            translateLineEnd(event)
        }

        fun initEventHandler() {
            isMouseTransparent = false

            addEventHandler(MouseEvent.MOUSE_CLICKED) { mouseEvent ->
                //Removing connection on the middle mouse button
                if (mouseEvent.button == MouseButton.MIDDLE) {
                    abortConnection()
                }
            }
        }

        fun updateStartPoint() {
            setStart(sceneToLocal(localToScenePoint(inProp.outDot)))
        }

        fun updateEndPoint() {
            setEnd(sceneToLocal(localToScenePoint(outProp.inDot)))
        }

        fun translateLineEnd(event: MouseEvent) {
            setEnd(sceneToLocal(event.sceneX, event.sceneY))
        }

        fun abortConnection() {
            inProp.abortOut(this)
            outProp.abortIn()
            removeSelf()
        }

        fun removeSelf() {
            val group = scene.lookup("#lines_group") as Group
            group.children.remove(this)
        }

    }


    private companion object {
        var grabbedProp: BaseProp<*>? = null
        var targetProp: BaseProp<*>? = null
        var targetPropParent: BaseGraphNode? = null
        var activeLine: PropLine? = null
        var activeDot: Node? = null

        fun clearCompanion() {
            grabbedProp = null
            targetProp = null
            targetPropParent = null
            activeLine = null
            activeDot = null
        }
    }

    enum class types {

        ANY,
        FLOAT,
        INTEGER,
        STRING,
        IMAGE

    }

}


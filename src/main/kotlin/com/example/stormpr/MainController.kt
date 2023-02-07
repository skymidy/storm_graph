package com.example.stormpr

import com.example.stormpr.components.OutputNode
import com.example.stormpr.components.SimpleImageNode
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.Slider
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.AnchorPane


class MainController {

    lateinit var node_canvas: AnchorPane

    @FXML
    private fun onCreateNode(event: MouseEvent) {
        val slider = Slider()

        val progressIndicator = ProgressIndicator(0.0)
        progressIndicator.progressProperty().bind(
            Bindings.divide(
                slider.valueProperty(), slider.maxProperty()
            )
        )

        val testNode = OutputNode(Point2D(400.0,400.0))
        node_canvas.children.addAll(testNode)
    }

    fun onWorkspaceDragDropped(event: DragEvent) {
        val dragBoard = event.dragboard
        var success = false
        if (dragBoard.hasFiles()) {
            println(dragBoard.files.last().toString().substringAfterLast("\\"))
            success = if(dragBoard.files.last().extension.matches("(?i)jpeg|jpg|png|bmp".toRegex())) {
                val newImageNode = SimpleImageNode(dragBoard.files.last(), Point2D( event.x, event.y))
                node_canvas.children.add(newImageNode)
                true
            }else {
                val newOutputNode = OutputNode(Point2D( event.x, event.y))
                node_canvas.children.add(newOutputNode)
                true
            }
        }
        event.isDropCompleted = success

        event.consume()
    }

    fun onWorkspaceDragOver(event: DragEvent) {
        if (event.gestureSource != node_canvas
            && event.dragboard.hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
        }
        event.consume()
    }
}
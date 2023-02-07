package com.example.stormpr

import com.example.stormpr.components.BaseGraphNode
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.Slider
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane


class MenubarController {

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

        val testNode = BaseGraphNode("Test")
        node_canvas.children.addAll(testNode)
    }
}
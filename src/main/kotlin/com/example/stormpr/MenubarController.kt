package com.example.stormpr

import com.example.stormpr.components.BaseGraphNode
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.Slider
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane


class MenubarController {

    @FXML
    private fun onCreateNode(event: MouseEvent) {
        val button = event.source as Button
        val canvas = button.scene.root.childrenUnmodifiable.find { it.id == "node_canvas" } as Pane

        val slider = Slider()

        val progressIndicator = ProgressIndicator(0.0)
        progressIndicator.progressProperty().bind(
            Bindings.divide(
                slider.valueProperty(), slider.maxProperty()
            )
        )

        val testNode = BaseGraphNode("Test")

        canvas.children.addAll(testNode)
    }
}
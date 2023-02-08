package com.example.stormpr

import com.example.stormpr.components.nodes.DoubleNode
import com.example.stormpr.components.nodes.OutputNode
import com.example.stormpr.util.localToScenePoint
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.opencv.core.Core
import org.opencv.osgi.OpenCVNativeLoader

class App : Application() {
    override fun start(stage: Stage) {
        OpenCVNativeLoader().init()
        println(Core.VERSION)
        val fxmlLoader = FXMLLoader(App::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load())

        stage.title = "Hello!"
        stage.scene = scene
        stage.minWidth = 900.0
        stage.minHeight = 600.0
        stage.centerOnScreen()

        stage.show()

        val node = scene.lookup("#node_canvas") as AnchorPane
        val outputNode = OutputNode(Point2D(600.0, 250.0))



//        node.setOnContextMenuRequested { nodeContextMenu.show(stage) }

        node.children.add(outputNode)
    }



}

fun main() {
    Application.launch(App::class.java)
}
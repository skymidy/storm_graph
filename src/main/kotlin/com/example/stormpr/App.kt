package com.example.stormpr

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
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

        stage.show()
    }

}

fun main() {
    Application.launch(App::class.java)
}
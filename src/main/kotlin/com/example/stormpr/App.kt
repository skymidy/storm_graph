package com.example.stormpr

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class App : Application() {
    override fun start(stage: Stage) {
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
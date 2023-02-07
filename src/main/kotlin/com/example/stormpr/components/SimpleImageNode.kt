package com.example.stormpr.components

import com.example.stormpr.util.matToImage
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import org.opencv.core.Mat
import java.io.File
import kotlin.math.floor


class SimpleImageNode(imageFile: File, position: Point2D) :
    BaseGraphNode("Image", position) {
    private val imageView: ImageView = ImageView()
    private var mainProp: ImageProp = ImageProp(this, hasIn = false, hasOut = true)

    init {
        //porps init

        initPropsListeners()
        mainProp.setFile(imageFile)
        addProp(mainProp)

        //node init
        imageView.fitWidth = 220.0
        imageView.fitHeight = 100.0
        imageView.isPreserveRatio = false
        imageView.styleClass += "node-image-view"
        nodeHeader.children.add(imageView)
    }

    override fun update() {
        super.update()
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        mainProp.getValueProperty().addListener { _, _, new ->
            redrawImageView(new)
        }
    }

    private fun redrawImageView(mat: Mat) {
        imageView.image = matToImage(mat)
        imageView.viewport = Rectangle2D(
            .0,
            imageView.image.height / 2,
            imageView.image.width,
            floor((imageView.image.height * 100) / 220)
        )
    }

}
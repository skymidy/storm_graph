package com.example.stormpr.components.nodes

import com.example.stormpr.components.props.ImageProp
import com.example.stormpr.util.matToImage
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import org.opencv.core.Mat
import java.io.File
import kotlin.math.floor


class SimpleImageNode(position: Point2D, imageFile: File? = null) :
    BaseGraphNode("Image", position) {
    private val imageView: ImageView = ImageView()
    private var mainProp: ImageProp = ImageProp(hasIn = false, hasOut = true)

    init {
        //porps init

        initPropsListeners()
        if(imageFile !=null)
            mainProp.setFile(imageFile)
        addProp(mainProp)

        //node init
        imageView.fitWidth = 220.0
        imageView.fitHeight = 100.0
        imageView.isPreserveRatio = false
        imageView.styleClass += "node-image-view"
        nodeHeader.children.add(imageView)
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
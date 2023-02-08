package com.example.stormpr.components.nodes

import com.example.stormpr.components.props.OutputNodeProp
import com.example.stormpr.util.matToImage
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import org.opencv.core.Mat
import kotlin.math.floor

class OutputNode(position: Point2D) : BaseGraphNode("Out", position) {
    private val imageView: ImageView = ImageView()
    private var mainProp: OutputNodeProp = OutputNodeProp()
    private val startPoint: Point2D = position

    init {
        //porps init

        initPropsListeners()
        addProp(mainProp)

        nodeHeader.children.removeAt(0)
        nodeHeader.children.add(nodeLabel)

        //node init
        id = "output_node"

        imageView.fitWidth = 220.0
        imageView.fitHeight = 100.0
        imageView.isPreserveRatio = false
        imageView.styleClass += "node-image-view"
        nodeHeader.children.add(imageView)
    }

    fun getFinalMat(): Mat {
        return mainProp.getValue()
    }

    fun translateToStartPosition() {
        translateX = (scene.width / 4) * 3
        translateY = (scene.height / 2) - 100
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
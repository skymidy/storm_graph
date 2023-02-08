package com.example.stormpr.components.nodes.imagefilters

import com.example.stormpr.components.nodes.BaseGraphNode
import com.example.stormpr.components.props.ImageOutputProp
import com.example.stormpr.components.props.ImageProp
import com.example.stormpr.util.matToImage
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import org.opencv.core.Mat
import java.lang.Exception
import kotlin.math.floor

abstract class FilterNode(position: Point2D, nodeName: String, imagePropName: String = "src:") :
    BaseGraphNode(nodeName, position) {
    private val imageView: ImageView = ImageView()
    private val mainProp: ImageOutputProp = ImageOutputProp()
    protected val imageProp: ImageProp = ImageProp(hasIn = true,hasOut = false, imagePropName)



    init {
        //porps init
        addProps(mainProp,imageProp)

        //node init
        imageView.fitWidth = 220.0
        imageView.fitHeight = 100.0
        imageView.isPreserveRatio = false
        imageView.styleClass += "node-image-view"
        nodeHeader.children.add(imageView)
    }

    override fun update() {
        super.update()
        var newMat: Mat? = null
        try {
            newMat = filter(imageProp.getValue())
        }catch (_:Exception){}
        if(newMat != null) {
            mainProp.getValueProperty().value = newMat
            redrawImageView(newMat)
        }
        else{
            redrawImageView( mainProp.getValueProperty().value)
        }
    }
    open fun filter(mat:Mat):Mat{
        return mat
    }

    override fun initPropsListeners() {
        super.initPropsListeners()
        imageProp.getValueProperty().addListener  { _, _, new ->
            mainProp.getValueProperty().value = new
            update()
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
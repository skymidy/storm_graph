package com.example.stormpr.components

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent
import javafx.stage.FileChooser
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.io.File





class ImageProp(parent: BaseGraphNode, hasIn: Boolean, hasOut: Boolean) : BaseProp<Mat>(hasIn, hasOut, parent) {
    override val type: types = types.IMAGE
    override val value: Property<Mat> = SimpleObjectProperty()

    val button:Button = Button("...")

    init {
        button.styleClass += "node-button"
        button.prefWidth = 200.0
        button.minWidth = 200.0
        button.maxWidth = 200.0

        val fileChooser = FileChooser()
        fileChooser.title = "View Pictures"
        fileChooser.initialDirectory = File(System.getProperty("user.home"))
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("All Images", "*.bmp;*.dib;*.jpeg;*.jpg;*.jpe;*.jp2;*.png;*.webp;*.pbm;*.pgm;*.ppm;*.pxm;*.pnm;*.pfm;*.sr;*.ras;*.tiff;*.tif;*.exr;*.hdr;*.pic"),
            FileChooser.ExtensionFilter("JPEG", "*.bmp;*.dib"),
            FileChooser.ExtensionFilter("JPG", "*.jpeg;*.jpg;*.jpe"),
            FileChooser.ExtensionFilter("JPEG 2000", "*.jp2"),
            FileChooser.ExtensionFilter("PNG", "*.png"),
            FileChooser.ExtensionFilter("WebP", "*.webp"),
            FileChooser.ExtensionFilter("Portable image format", "*.pbm;*.pgm;*.ppm;*.pxm;*.pnm"),
            FileChooser.ExtensionFilter("PFM", "*.pfm"),
            FileChooser.ExtensionFilter("TIFF", "*.tiff;*.tif"),
            FileChooser.ExtensionFilter("OpenEXR", "*.exr"),
            FileChooser.ExtensionFilter("HDR", "*.hdr;*.pic"),
        )

        button.addEventHandler(MouseEvent.MOUSE_CLICKED) { mouseEvent ->
            val file: File? = fileChooser.showOpenDialog(this.scene.window)
            if (file != null) {
                setFile(file)
            }
        }
        body.children.add(button)
    }
    fun setFile(file:File){
        if(file.extension.matches("(?i)jpeg|jpg|png|bmp".toRegex())) {
            value.value = Imgcodecs.imread(file.path)
            button.text = file.name
        }


    }
}
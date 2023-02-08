package com.example.stormpr.components.props

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent
import javafx.stage.FileChooser
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


class ImageProp(hasIn: Boolean, hasOut: Boolean, name:String = "src:") : BaseProp<Mat>(hasIn, hasOut, name) {
    override val type: types = types.IMAGE
    override val value: Property<Mat> = SimpleObjectProperty(Mat.eye(220,100, 0xFFFF0000.toInt()))

    val button:Button = Button("...")

    init {
        button.styleClass += "node-button"
        button.prefWidth = 125.0
        button.minWidth = 125.0
        button.maxWidth = 125.0

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
        var image = Imgcodecs.imread(file.path)
        if(image.empty()){
            //file name error
            val path = Files.copy(Path.of(file.path), Path.of(file.parent+"temp"), StandardCopyOption.REPLACE_EXISTING)
            image = Imgcodecs.imread(path.toString())
            Files.delete(path)
        }
        value.value = image
        button.text = file.name
    }

    override fun abortIn() {
        super.abortIn()
        value.value = Mat(220, 100, CvType.CV_8UC1, Scalar(0.0, 0.0, 255.0))
        button.text = "choose file"
    }
}
package com.example.stormpr

import com.example.stormpr.components.nodes.*
import com.example.stormpr.components.nodes.imagefilters.*
import com.example.stormpr.components.nodes.imagefilters.transform.MoveNode
import com.example.stormpr.components.nodes.imagefilters.transform.RotateNode
import com.example.stormpr.components.nodes.imagefilters.transform.ScaleNode
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import org.opencv.imgcodecs.Imgcodecs
import java.io.IOException


class MainController {

    lateinit var node_canvas: AnchorPane

    var workspaceContextMenu: ContextMenu? = null


    fun onMenuAction(event: ActionEvent) {
        when ((event.target as MenuItem).text) {
            "New" -> clearWorkSpace()
            "Save" -> saveFile()
            else -> {}
        }
    }
    private fun saveFile(){
        val fileChooser = FileChooser()
        fileChooser.title = "Save Image"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("PNG", "*.png"),
            FileChooser.ExtensionFilter("JPG", "*.jpg"),
            FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        )
        val file = fileChooser.showSaveDialog(node_canvas.scene.window)
        if (file != null) {
            try {
                val mat = (node_canvas.lookup("#output_node") as OutputNode).getFinalMat()
                Imgcodecs.imwrite(file.absolutePath, mat)
            } catch (_: IOException) { }
        }
    }
    private fun clearWorkSpace(){

        node_canvas.children.filter { !listOf<String>("output_node","lines_group").contains(it.id)  }.forEach { (it as BaseGraphNode).removeSelf() }
        (node_canvas.lookup("#output_node") as OutputNode).translateToStartPosition()
    }

    fun onWorkspaceDragDropped(event: DragEvent) {
        val dragBoard = event.dragboard
        var success = false
        if (dragBoard.hasFiles()) {
            println(dragBoard.files.last().toString().substringAfterLast("\\"))
            success = if(dragBoard.files.last().extension.matches("(?i)jpeg|jpg|png|bmp".toRegex())) {
                val newImageNode = SimpleImageNode( Point2D( event.x, event.y), dragBoard.files.last())
                node_canvas.children.add(newImageNode)
                true
            }else {
                val newOutputNode = OutputNode(Point2D( event.x, event.y))
                node_canvas.children.add(newOutputNode)
                true
            }
        }
        event.isDropCompleted = success

        event.consume()
    }

    fun onWorkspaceDragOver(event: DragEvent) {
        if (event.gestureSource != node_canvas
            && event.dragboard.hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
        }
        event.consume()
    }

    fun onWorkspaceContextMenu(event: ContextMenuEvent) {

        if (workspaceContextMenu == null){
            workspaceContextMenuInit()
        }

        workspaceContextMenu?.show(node_canvas, event.screenX, event.screenY)
    }
    private fun workspaceContextMenuInit(){
        val nameItem = MenuItem("Add node")
        nameItem.isDisable = true

        val filterSubMenu = Menu("Filters")
        filterSubMenu.items.addAll(
            MenuItem("Brightness"),
            MenuItem("Invert"),
            MenuItem("Gauss"),
            MenuItem("Grey"),
            MenuItem("Sepia"),
        )
        filterSubMenu.onAction = EventHandler {
            workspaceContextMenuHandler(it)
        }

        val transformSubMenu = Menu("Transform")
        transformSubMenu.items.addAll(
            MenuItem("Rotate"),
            MenuItem("Scale"),
            MenuItem("Move"),
        )
        transformSubMenu.onAction = EventHandler {
            workspaceContextMenuHandler(it)
        }
        workspaceContextMenu = ContextMenu(
            nameItem,
            MenuItem("Double"),
            MenuItem("Int"),
            MenuItem("String"),
            MenuItem("Image"),
            MenuItem("Merge"),
            MenuItem("Add Text"),
            )
        workspaceContextMenu!!.items.addAll(filterSubMenu,transformSubMenu)
        workspaceContextMenu!!.onAction = EventHandler {
            workspaceContextMenuHandler(it)
        }
    }

    private fun workspaceContextMenuHandler(event: ActionEvent) {
        val point = node_canvas.screenToLocal( workspaceContextMenu?.x ?: 0.0, workspaceContextMenu?.y ?: 0.0)
        val node: Node? = when ((event.target as MenuItem).text) {
            "Double" -> DoubleNode(point)
            "Int" -> IntegerNode(point)
            "String" -> StringNode(point)
            "Image" -> SimpleImageNode(point)
            "Merge" -> MergeNode(point)
            "Add Text" -> AddTextNode(point)
            //filters
            "Invert" -> InvertNode(point)
            "Gauss" -> GaussNode(point)
            "Grey" -> GreyNode(point)
            "Sepia" -> SepiaNode(point)
            "Brightness" -> BrightnessNode(point)
            //transform
            "Rotate" -> RotateNode(point)
            "Scale" -> ScaleNode(point)
            "Move" -> MoveNode(point)

            else -> null
        }
        if (node != null)
            node_canvas.children.add(node)
    }

    fun onMouseClick(event: MouseEvent) {
        if (workspaceContextMenu?.isShowing == true){
            workspaceContextMenu?.hide()
        }

    }
}
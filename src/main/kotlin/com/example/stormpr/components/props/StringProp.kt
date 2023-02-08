package com.example.stormpr.components.props

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.function.UnaryOperator
import java.util.regex.Pattern

class StringProp(hasIn: Boolean, hasOut: Boolean, baseValue: String = "", name: String = "value:") :
    BaseProp<String>(hasIn, hasOut, name) {
    override val type: types = types.STRING
    override val value: Property<String> = SimpleObjectProperty(baseValue)

    private val field: TextField = TextField("")

    //
    init {
        field.prefWidth = 125.0
        val validEditingState: Pattern = Pattern.compile(".*")

        val filter = UnaryOperator { c: TextFormatter.Change? ->
            val text = c!!.controlNewText
            if (validEditingState.matcher(text).matches()) {
                return@UnaryOperator c
            } else {
                return@UnaryOperator null
            }
        }

        val converter: StringConverter<String> = object : StringConverter<String>() {
            override fun fromString(s: String): String? {
                return s
            }

            override fun toString(d: String): String? {
                return d
            }
        }
        val textFormatter = TextFormatter(converter, "", filter)

        textFormatter.valueProperty()
            .addListener { _, _, newValue ->
                value.value = newValue
            }
        field.textFormatter = textFormatter

        body.children.add(field)
    }

    override fun abortIn() {
        super.abortIn()
        field.text = value.value
    }
}
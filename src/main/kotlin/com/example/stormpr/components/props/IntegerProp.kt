package com.example.stormpr.components.props

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.function.UnaryOperator
import java.util.regex.Pattern

class IntegerProp(hasIn: Boolean, hasOut: Boolean, baseValue: Int = 0, name: String = "x:") :
    BaseProp<Int>(hasIn, hasOut, name) {
    override val type: types = types.INTEGER
    override val value: Property<Int> = SimpleObjectProperty(baseValue)

    private val field: TextField = TextField("0")

    //
    init {
        field.prefWidth = 125.0
        val validEditingState: Pattern = Pattern.compile("-?(([1-9][0-9]*)|0)?")

        val filter = UnaryOperator { c: TextFormatter.Change? ->
            val text = c!!.controlNewText
            if (validEditingState.matcher(text).matches()) {
                return@UnaryOperator c
            } else {
                return@UnaryOperator null
            }
        }

        val converter: StringConverter<Int> = object : StringConverter<Int>() {
            override fun fromString(s: String): Int? {
                return if (s.isEmpty() || "-" == s || "." == s || "-." == s) {
                    0
                } else {
                    Integer.valueOf(s)
                }
            }

            override fun toString(d: Int): String? {
                return d.toString()
            }
        }
        val textFormatter = TextFormatter(converter, 0, filter)

        textFormatter.valueProperty()
            .addListener { _, _, newValue ->
                value.value = newValue
            }
        field.textFormatter = textFormatter

        body.children.add(field)
    }

    override fun abortIn() {
        super.abortIn()
        field.text = value.value.toString()
    }
}